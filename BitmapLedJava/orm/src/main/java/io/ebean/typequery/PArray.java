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

/**
 * Array property with E as the element type.
 *
 * @param <R> the root query bean type
 * @param <E> the element type of the DbArray
 */
public final class PArray<R, E> extends TQPropertyBase<R, E> {

  /**
   * Construct with a property name and root instance.
   *
   * @param name property name
   * @param root the root query bean instance
   */
  public PArray(String name, R root) {
    super(name, root);
  }

  /**
   * Construct with additional path prefix.
   */
  public PArray(String name, R root, String prefix) {
    super(name, root, prefix);
  }

  /**
   * ARRAY contains the values.
   * <p>
   * <pre>{@code
   *
   *   new QContact()
   *    .phoneNumbers.contains("4321")
   *    .findList();
   *
   * }</pre>
   *
   * @param values The values that should be contained in the array
   */
  @SafeVarargs
  public final R contains(E... values) {
    expr().arrayContains(_name, (Object[]) values);
    return _root;
  }

  /**
   * ARRAY does not contain the values.
   * <p>
   * <pre>{@code
   *
   *   new QContact()
   *    .phoneNumbers.notContains("4321")
   *    .findList();
   *
   * }</pre>
   *
   * @param values The values that should not be contained in the array
   */
  @SafeVarargs
  public final R notContains(E... values) {
    expr().arrayNotContains(_name, (Object[]) values);
    return _root;
  }

  /**
   * ARRAY is empty.
   * <p>
   * <pre>{@code
   *
   *   new QContact()
   *    .phoneNumbers.isEmpty()
   *    .findList();
   *
   * }</pre>
   */
  public R isEmpty() {
    expr().arrayIsEmpty(_name);
    return _root;
  }

  /**
   * ARRAY is not empty.
   * <p>
   * <pre>{@code
   *
   *   new QContact()
   *    .phoneNumbers.isNotEmpty()
   *    .findList();
   *
   * }</pre>
   */
  public R isNotEmpty() {
    expr().arrayIsNotEmpty(_name);
    return _root;
  }

}
