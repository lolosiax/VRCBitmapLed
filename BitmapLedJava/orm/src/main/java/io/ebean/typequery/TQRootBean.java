/*
 * Copyright (c) 2025 Lolosia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.ebean.typequery;

import io.avaje.lang.NonNullApi;
import io.avaje.lang.Nullable;
import io.ebean.*;
import io.ebean.service.SpiFetchGroupQuery;
import io.ebeaninternal.api.SpiQueryFetch;
import io.ebeaninternal.server.util.ArrayStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base root query bean providing common features for all root query beans.
 * <p>
 * For each entity bean querybean-generator generates a query bean that extends TQRootBean.
 *
 * <h2>Example - usage of QCustomer</h2>
 * <pre>{@code
 *
 *  Date fiveDaysAgo = ...
 *
 *  List<Customer> customers =
 *      new QCustomer()
 *        .name.ilike("rob")
 *        .status.equalTo(Customer.Status.GOOD)
 *        .registered.after(fiveDaysAgo)
 *        .contacts.email.endsWith("@foo.com")
 *        .orderBy()
 *          .name.asc()
 *          .registered.desc()
 *        .findList();
 *
 * }</pre>
 * <p>
 * <h2>Resulting SQL where</h2>
 * <p>
 * <pre>{@code
 *
 *   where lower(t0.name) like ?  and t0.status = ?  and t0.registered > ?  and u1.email like ?
 *   order by t0.name, t0.registered desc;
 *
 *   --bind(rob,GOOD,Mon Jul 27 12:05:37 NZST 2015,%@foo.com)
 *
 * }</pre>
 *
 * @param <T> the entity bean type (normal entity bean type e.g. Customer)
 * @param <R> the specific root query bean type (e.g. QCustomer)
 */
@NonNullApi
@SuppressWarnings("DataFlowIssue")
public abstract class TQRootBean<T, R> implements IQueryBean<T, R> {

  private static Function<Object[], Object> databaseCallback;
  private static final Logger logger = LoggerFactory.getLogger(TQRootBean.class);

  static {
      try {
        Class<?> aClass = Class.forName("top.lolosia.vrc.led.util.ebean.AbstractModel");
        Field field = aClass.getDeclaredField("databaseCallback");
        field.setAccessible(true);
        // noinspection unchecked
        databaseCallback = (Function<Object[], Object>) field.get(null);
      } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
          logger.warn(e.getMessage(), e);
      }
  }

  /**
   * The underlying query.
   */
  private final Query<T> query;

  /**
   * The root query bean instance. Used to provide fluid query construction.
   */
  private final R root;
  private Database database;

  /**
   * The underlying expression lists held as a stack. Pushed and popped based on and/or (conjunction/disjunction).
   */
  private ArrayStack<ExpressionList<T>> whereStack;

  /**
   * Construct using the type of bean to query on and the default database.
   */
  public TQRootBean(Class<T> beanType) {
    this(beanType, DB.getDefault());
  }

  /**
   * Construct using the type of bean to query on and a given database.
   */
  public TQRootBean(Class<T> beanType, Database database) {
    this(database.find(beanType));
  }

  /**
   * Construct with a transaction.
   */
  protected TQRootBean(Class<T> beanType, Transaction transaction) {
    this(beanType);
    query.usingTransaction(transaction);
  }

  /**
   * Construct with a database and transaction.
   */
  protected TQRootBean(Class<T> beanType, Database database, Transaction transaction) {
    this(beanType, database);
    query.usingTransaction(transaction);
  }

  /**
   * Construct using a query.
   */
  @SuppressWarnings("unchecked")
  public TQRootBean(Query<T> query) {
    this.query = query;
    this.root = (R) this;
  }

  /**
   * Construct for using as an 'Alias' to use the properties as known string
   * values for select() and fetch().
   */
  @SuppressWarnings("unchecked")
  public TQRootBean(boolean aliasDummy) {
    this.query = null;
    this.root = (R) this;
  }

  /** Construct for FilterMany */
  protected TQRootBean(ExpressionList<T> filter) {
    this.query = null;
    this.root = null;
    this.whereStack = new ArrayStack<>();
    whereStack.push(filter);
  }

  /**
   * The enhancement will no longer use this method. Will be removed once the new IntelliJ plugin is released.
   */
  protected void setRoot(R root) {
    // do nothing, remove this method shortly.
  }

  @Override
  public FetchGroup<T> buildFetchGroup() {
    return ((SpiFetchGroupQuery<T>) query()).buildFetchGroup();
  }

  @Override
  public Query<T> query() {
    return query;
  }

  @Override
  public R select(String properties) {
    query.select(properties);
    return root;
  }

  @Override
  public R select(FetchGroup<T> fetchGroup) {
    query.select(fetchGroup);
    return root;
  }

  @Override
  @SafeVarargs
  public final R select(TQProperty<R, ?>... properties) {
    ((SpiQueryFetch) query).selectProperties(properties(properties));
    return root;
  }

  private Set<String> properties(Query.Property<?>[] properties) {
    Set<String> props = new LinkedHashSet<>();
    for (Query.Property<?> property : properties) {
      props.add(property.toString());
    }
    return props;
  }

  @Override
  public final R select(Query.Property<?>... properties) {
    ((SpiQueryFetch) query).selectProperties(properties(properties));
    return root;
  }

  @Override
  public final R fetch(String path) {
    query.fetch(path);
    return root;
  }

  @Override
  public final R fetchQuery(String path) {
    query.fetchQuery(path);
    return root;
  }

  @Override
  public final R fetchCache(String path) {
    query.fetchCache(path);
    return root;
  }

  @Override
  public final R fetchQuery(String path, String properties) {
    query.fetchQuery(path, properties);
    return root;
  }

  @Override
  public final R fetchCache(String path, String properties) {
    query.fetchCache(path, properties);
    return root;
  }

  @Override
  public final R fetch(String path, String properties) {
    query.fetch(path, properties);
    return root;
  }

  @Override
  public final R fetch(String path, String properties, FetchConfig fetchConfig) {
    query.fetch(path, properties, fetchConfig);
    return root;
  }

  @Override
  public final R fetch(String path, FetchConfig fetchConfig) {
    query.fetch(path, fetchConfig);
    return root;
  }

  @Override
  public R fetchLazy(String path, String fetchProperties) {
    query.fetchLazy(path, fetchProperties);
    return root;
  }

  @Override
  public R fetchLazy(String path) {
    query.fetchLazy(path);
    return root;
  }

  @Override
  public final R apply(FetchPath pathProperties) {
    query.apply(pathProperties);
    return root;
  }

  @Override
  public final R alsoIf(BooleanSupplier predicate, Consumer<R> apply) {
    if (predicate.getAsBoolean()) {
      apply.accept(root);
    }
    return root;
  }

  @Override
  public final R asOf(Timestamp asOf) {
    query.asOf(asOf);
    return root;
  }

  @Override
  public final R setIncludeSoftDeletes() {
    query.setIncludeSoftDeletes();
    return root;
  }

  @Override
  public final R add(Expression expression) {
    peekExprList().add(expression);
    return root;
  }

  @Override
  public final R alias(String alias) {
    query.alias(alias);
    return root;
  }

  @Override
  public final R setMaxRows(int maxRows) {
    query.setMaxRows(maxRows);
    return root;
  }

  @Override
  public final R setFirstRow(int firstRow) {
    query.setFirstRow(firstRow);
    return root;
  }

  @Override
  public final R setAllowLoadErrors() {
    query.setAllowLoadErrors();
    return root;
  }

  @Override
  public final R setAutoTune(boolean autoTune) {
    query.setAutoTune(autoTune);
    return root;
  }

  @Override
  public final R setBufferFetchSizeHint(int fetchSize) {
    query.setBufferFetchSizeHint(fetchSize);
    return root;
  }

  @Override
  public final R setDistinct(boolean distinct) {
    query.setDistinct(distinct);
    return root;
  }

  @Override
  public final R setBaseTable(String baseTable) {
    query.setBaseTable(baseTable);
    return root;
  }

  @Override
  public final R withLock(Query.LockType lockType) {
    query.withLock(lockType);
    return root;
  }

  @Override
  public final R withLock(Query.LockType lockType, Query.LockWait lockWait) {
    query.withLock(lockType, lockWait);
    return root;
  }

  @Override
  public final R exists(Query<?> subQuery) {
    peekExprList().exists(subQuery);
    return root;
  }

  @Override
  public final R notExists(Query<?> subQuery) {
    peekExprList().notExists(subQuery);
    return root;
  }

  @Override
  public final R exists(String sqlSubQuery, Object... bindValues) {
    peekExprList().exists(sqlSubQuery, bindValues);
    return root;
  }

  @Override
  public final R notExists(String sqlSubQuery, Object... bindValues) {
    peekExprList().notExists(sqlSubQuery, bindValues);
    return root;
  }

  @Override
  public final R forUpdate() {
    query.forUpdate();
    return root;
  }

  @Override
  public final R forUpdateNoWait() {
    query.forUpdateNoWait();
    return root;
  }

  @Override
  public final R forUpdateSkipLocked() {
    query.forUpdateSkipLocked();
    return root;
  }

  @Override
  public final UpdateQuery<T> asUpdate() {
    return query.asUpdate();
  }

  @Override
  public final <D> DtoQuery<D> asDto(Class<D> dtoClass) {
    return query.asDto(dtoClass);
  }

  @Override
  public final R setId(Object id) {
    query.setId(id);
    return root;
  }

  @Override
  public final R setIdIn(Object... ids) {
    query.where().idIn(ids);
    return root;
  }

  @Override
  public final R setIdIn(Collection<?> ids) {
    query.where().idIn(ids);
    return root;
  }

  @Override
  public final R setLabel(String label) {
    query.setLabel(label);
    return root;
  }

  @Override
  public final R setHint(String hint) {
    query.setHint(hint);
    return root;
  }

  @Override
  public final R setProfileLocation(ProfileLocation profileLocation) {
    query.setProfileLocation(profileLocation);
    return root;
  }

  @Override
  public final R setLazyLoadBatchSize(int lazyLoadBatchSize) {
    query.setLazyLoadBatchSize(lazyLoadBatchSize);
    return root;
  }

  @Override
  public final R setMapKey(String mapKey) {
    query.setMapKey(mapKey);
    return root;
  }

  @Override
  public final R setPersistenceContextScope(PersistenceContextScope scope) {
    query.setPersistenceContextScope(scope);
    return root;
  }

  @Override
  public R setCountDistinct(CountDistinctOrder orderBy) {
    query.setCountDistinct(orderBy);
    return root;
  }

  @Override
  public final R setRawSql(RawSql rawSql) {
    query.setRawSql(rawSql);
    return root;
  }

  @Override
  public final R setReadOnly(boolean readOnly) {
    query.setReadOnly(readOnly);
    return root;
  }

  @Override
  public final R setBeanCacheMode(CacheMode beanCacheMode) {
    query.setBeanCacheMode(beanCacheMode);
    return root;
  }

  @Override
  public final R setDisableLazyLoading(boolean disableLazyLoading) {
    query.setDisableLazyLoading(disableLazyLoading);
    return root;
  }

  @Override
  public final R setUseQueryCache(CacheMode cacheMode) {
    query.setUseQueryCache(cacheMode);
    return root;
  }

  @Override
  public final R setTimeout(int secs) {
    query.setTimeout(secs);
    return root;
  }

  @Override
  public final Set<String> validate() {
    return query.validate();
  }

  @Override
  public final R raw(String rawExpression) {
    peekExprList().raw(rawExpression);
    return root;
  }

  @Override
  public final R raw(String rawExpression, Object... bindValues) {
    peekExprList().raw(rawExpression, bindValues);
    return root;
  }

  @Override
  public final R rawOrEmpty(String raw, Collection<?> values) {
    peekExprList().rawOrEmpty(raw, values);
    return root;
  }

  @Override
  public final R raw(String rawExpression, Object bindValue) {
    peekExprList().raw(rawExpression, bindValue);
    return root;
  }

  @Override
  public final R inTuples(InTuples inTuples) {
    peekExprList().inTuples(inTuples);
    return root;
  }

  @Override
  public final R orderBy() {
    // Yes this does not actually do anything! We include it because style wise it makes
    // the query nicer to read and suggests that order by definitions are added after this
    return root;
  }

  @Override
  @SuppressWarnings("removal")
  @Deprecated(since = "13.19", forRemoval = true)
  public final R order() {
    return root;
  }

  @Override
  public final R orderBy(String orderByClause) {
    query.orderBy(orderByClause);
    return root;
  }

  @Override
  public R setOrderBy(OrderBy<T> orderBy) {
    query.setOrderBy(orderBy);
    return root;
  }

  @Override
  public R orderById(boolean orderById) {
    query.orderById(orderById);
    return root;
  }

  @Override
  @SuppressWarnings("removal")
  @Deprecated(since = "13.19", forRemoval = true)
  public final R order(String orderByClause) {
    return orderBy(orderByClause);
  }

  @Override
  public final R or() {
    pushExprList(peekExprList().or());
    return root;
  }

  @Override
  public final R and() {
    pushExprList(peekExprList().and());
    return root;
  }

  @Override
  public final R not() {
    pushExprList(peekExprList().not());
    return root;
  }


  @Override
  public final R endJunction() {
    whereStack.pop();
    return root;
  }

  @Override
  public final R endOr() {
    return endJunction();
  }

  @Override
  public final R endAnd() {
    return endJunction();
  }

  @Override
  public final R endNot() {
    return endJunction();
  }

  /**
   * Push the expression list onto the appropriate stack.
   */
  private R pushExprList(ExpressionList<T> list) {
    whereStack.push(list);
    return root;
  }

  @Override
  public final R where() {
    return root;
  }

  @Override
  public final R usingTransaction(Transaction transaction) {
    query.usingTransaction(transaction);
    return root;
  }

  @Override
  public final R usingConnection(Connection connection) {
    query.usingConnection(connection);
    return root;
  }

  @Override
  public R usingDatabase(Database database) {
    query.usingDatabase(database);
    this.database = database;
    return root;
  }

  @Override
  public final R usingMaster() {
    query.usingMaster();
    return root;
  }

  @Override
  public final boolean exists() {
    return query.exists();
  }

  /**
   * 对实体设置数据库信息
   * @see top.lolosia.vrc.led.util.ebean.AbstractModel#databaseCallback
   * @param obj 实体
   * @return 实体
   */
  @SuppressWarnings("JavadocReference")
  private T database(T obj){
    if (databaseCallback != null){
      databaseCallback.apply(new Object[]{obj, database});
    }
    return obj;
  }

  @Override
  @Nullable
  public final T findOne() {
    return database(query.findOne());
  }

  @Override
  public final Optional<T> findOneOrEmpty() {
    return query.findOneOrEmpty().map(this::database);
  }

  @Override
  public final List<T> findList() {
    return query.findList().stream().map(this::database).toList();
  }

  @Override
  public final Stream<T> findStream() {
    return query.findStream().map(this::database);
  }

  @Override
  public final Set<T> findSet() {
    return query.findSet().stream().map(this::database).collect(Collectors.toSet());
  }

  @Override
  public final <A> List<A> findIds() {
    return query.findIds();
  }

  @Override
  public final <K> Map<K, T> findMap() {
    Map<K, T> map = query.findMap();
    //noinspection ResultOfMethodCallIgnored
    map.values().stream().map(this::database).toList();
    return map;
  }

  @Override
  public final QueryIterator<T> findIterate() {
      try (QueryIterator<T> iterate = query.findIterate()) {
          return new QueryIterator<>() {
              @Override
              public boolean hasNext() {
                  return iterate.hasNext();
              }

              @Override
              public T next() {
                  return database(iterate.next());
              }

              @Override
              public void close() {
                  iterate.close();
              }
          };
      }
  }

  @Override
  public final <A> List<A> findSingleAttributeList() {
    return query.findSingleAttributeList();
  }

  @Override
  public final <A> Set<A> findSingleAttributeSet() {
    return query.findSingleAttributeSet();
  }

  @Override
  @Nullable
  public final <A> A findSingleAttribute() {
    return query.findSingleAttribute();
  }

  @Override
  public final <A> Optional<A> findSingleAttributeOrEmpty() {
    return query.findSingleAttributeOrEmpty();
  }

  @Override
  public final void findEach(Consumer<T> consumer) {
    query.findEach(t -> consumer.accept(database(t)));
  }

  @Override
  public final void findEach(int batch, Consumer<List<T>> consumer) {
    query.findEach(batch, t -> consumer.accept(t.stream().map(this::database).toList()));
  }

  @Override
  public final void findEachWhile(Predicate<T> consumer) {
    query.findEachWhile(t -> consumer.test(database(t)));
  }

  @Override
  public final List<Version<T>> findVersions() {
    return query.findVersions().stream().peek(t -> database(t.getBean())).toList();
  }

  @Override
  public final List<Version<T>> findVersionsBetween(Timestamp start, Timestamp end) {
    return query.findVersionsBetween(start, end).stream().peek(t -> database(t.getBean())).toList();
  }

  @Override
  public final int findCount() {
    return query.findCount();
  }

  @Override
  public final FutureRowCount<T> findFutureCount() {
    return query.findFutureCount();
  }

  @Override
  public final FutureIds<T> findFutureIds() {
    return query.findFutureIds();
  }

  @Override
  public final FutureList<T> findFutureList() {
    FutureList<T> list = query.findFutureList();
    return new FutureList<>() {
        @Override
        public Query<T> getQuery() {
            return list.getQuery();
        }

        @Override
        public List<T> getUnchecked() {
            return list.getUnchecked().stream().map(TQRootBean.this::database).toList();
        }

        @Override
        public List<T> getUnchecked(long timeout, TimeUnit unit) throws TimeoutException {
            return list.getUnchecked(timeout, unit).stream().map(TQRootBean.this::database).toList();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return list.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return list.isCancelled();
        }

        @Override
        public boolean isDone() {
            return list.isDone();
        }

        @Override
        public List<T> get() throws InterruptedException, ExecutionException {
            return list.get().stream().map(TQRootBean.this::database).toList();
        }

        @Override
        public List<T> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return list.get(timeout, unit).stream().map(TQRootBean.this::database).toList();
        }
    };
  }

  @Override
  public final PagedList<T> findPagedList() {
    PagedList<T> list = query.findPagedList();
    return new PagedList<>() {
        @Override
        public void loadCount() {
            list.loadCount();
        }

        @Override
        public Future<Integer> getFutureCount() {
            return list.getFutureCount();
        }

        @Override
        public List<T> getList() {
            return list.getList().stream().map(TQRootBean.this::database).toList();
        }

        @Override
        public int getTotalCount() {
            return list.getTotalCount();
        }

        @Override
        public int getTotalPageCount() {
            return list.getTotalPageCount();
        }

        @Override
        public int getPageSize() {
            return list.getPageSize();
        }

        @Override
        public int getPageIndex() {
            return list.getPageIndex();
        }

        @Override
        public boolean hasNext() {
            return list.hasNext();
        }

        @Override
        public boolean hasPrev() {
            return list.hasPrev();
        }

        @Override
        public String getDisplayXtoYofZ(String to, String of) {
            return list.getDisplayXtoYofZ(to, of);
        }
    };
  }

  @Override
  public final int delete() {
    return query.delete();
  }

  @Override
  public final String getGeneratedSql() {
    return query.getGeneratedSql();
  }

  @Override
  public final Class<T> getBeanType() {
    return query.getBeanType();
  }

  @Override
  public final ExpressionList<T> getExpressionList() {
    return query.where();
  }

  @Override
  public final R having() {
    if (whereStack == null) {
      whereStack = new ArrayStack<>();
    }
    // effectively putting having expression list onto stack
    // such that expression now add to the having clause
    whereStack.push(query.having());
    return root;
  }

  @Override
  public final ExpressionList<T> havingClause() {
    return query.having();
  }

  /**
   * Return the current expression list that expressions should be added to.
   */
  protected final ExpressionList<T> peekExprList() {
    if (whereStack == null) {
      whereStack = new ArrayStack<>();
      whereStack.push(query.where());
    }
    // return the current expression list
    return whereStack.peek();
  }
}
