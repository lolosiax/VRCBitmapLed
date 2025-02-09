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

import io.avaje.lang.Nullable;

import java.util.Collection;

/**
 * Base type for associated beans.
 *
 * @param <T> the entity bean type (normal entity bean type e.g. Customer)
 * @param <R> the specific root query bean type (e.g. QCustomer)
 */
public abstract class TQAssoc<T, R> extends TQProperty<R, Object> {

  /**
   * Construct with a property name and root instance.
   *
   * @param name the name of the property
   * @param root the root query bean instance
   */
  public TQAssoc(String name, R root) {
    this(name, root, null);
  }

  /**
   * Construct with additional path prefix.
   */
  public TQAssoc(String name, R root, String prefix) {
    super(name, root, prefix);
  }

  /**
   * Is equal to by ID property.
   */
  public final R eq(T other) {
    expr().eq(_name, other);
    return _root;
  }

  /**
   * Is EQUAL TO if value is non-null and otherwise no expression is added to the query.
   * <p>
   * This is effectively a helper method that allows a query to be built in fluid style where some predicates are
   * effectively optional. We can use <code>eqIfPresent()</code> rather than having a separate if block.
   */
  public final R eqIfPresent(@Nullable T other) {
    expr().eqIfPresent(_name, other);
    return _root;
  }

  /**
   * Is equal to by ID property.
   */
  public final R equalTo(T other) {
    return eq(other);
  }

  /**
   * Is not equal to by ID property.
   */
  public final R ne(T other) {
    expr().ne(_name, other);
    return _root;
  }

  /**
   * Is not equal to by ID property.
   */
  public final R notEqualTo(T other) {
    return ne(other);
  }

  /**
   * Is in a list of values.
   *
   * @param values the list of values for the predicate
   * @return the root query bean instance
   */
  @SafeVarargs
  public final R in(T... values) {
    expr().in(_name, (Object[]) values);
    return _root;
  }

  /**
   * Is in a list of values.
   *
   * @param values the list of values for the predicate
   * @return the root query bean instance
   */
  public final R in(Collection<T> values) {
    expr().in(_name, values);
    return _root;
  }

  /**
   * In where null or empty values means that no predicate is added to the query.
   * <p>
   * That is, only add the IN predicate if the values are not null or empty.
   * <p>
   * Without this we typically need to code an <code>if</code> block to only add
   * the IN predicate if the collection is not empty like:
   * </p>
   *
   * <h3>Without inOrEmpty()</h3>
   * <pre>{@code
   *
   *   List<String> names = Arrays.asList("foo", "bar");
   *
   *   QCustomer query = new QCustomer()
   *       .registered.before(LocalDate.now())
   *
   *   // conditionally add the IN expression to the query
   *   if (names != null && !names.isEmpty()) {
   *       query.name.in(names)
   *   }
   *
   *   query.findList();
   *
   * }</pre>
   *
   * <h3>Using inOrEmpty()</h3>
   * <pre>{@code
   *
   *   List<String> names = Arrays.asList("foo", "bar");
   *
   *   new QCustomer()
   *       .registered.before(LocalDate.now())
   *       .name.inOrEmpty(names)
   *       .findList();
   *
   * }</pre>
   */
  public final R inOrEmpty(Collection<T> values) {
    expr().inOrEmpty(_name, values);
    return _root;
  }

  /**
   * Is NOT in a list of values.
   *
   * @param values the list of values for the predicate
   * @return the root query bean instance
   */
  public final R notIn(Collection<T> values) {
    expr().notIn(_name, values);
    return _root;
  }

  /**
   * Is NOT in a list of values.
   *
   * @param values the list of values for the predicate
   * @return the root query bean instance
   */
  @SafeVarargs
  public final R notIn(T... values) {
    expr().notIn(_name, (Object[]) values);
    return _root;
  }

}
