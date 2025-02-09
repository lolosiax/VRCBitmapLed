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

import io.ebean.ExpressionList;
import io.ebean.Query;

/**
 * A property used in type query.
 *
 * @param <R> The type of the owning root bean
 * @param <T> The property type
 */
public class TQProperty<R, T> implements Query.Property<T> {

  protected final String _name;
  protected final R _root;

  /**
   * Construct with a property name and root instance.
   *
   * @param name the name of the property
   * @param root the root query bean instance
   */
  public TQProperty(String name, R root) {
    this(name, root, null);
  }

  /**
   * Construct with additional path prefix.
   */
  public TQProperty(String name, R root, String prefix) {
    this._root = root;
    this._name = TQPath.add(prefix, name);
  }

  @Override
  public String toString() {
    return _name;
  }

  /**
   * Internal method to return the underlying expression list.
   */
  protected final ExpressionList<?> expr() {
    return ((TQRootBean<?, ?>) _root).peekExprList();
  }

  /**
   * Return the property name.
   */
  protected final String propertyName() {
    return _name;
  }

  /**
   * Is null.
   */
  public final R isNull() {
    expr().isNull(_name);
    return _root;
  }

  /**
   * Is not null.
   */
  public final R isNotNull() {
    expr().isNotNull(_name);
    return _root;
  }

}
