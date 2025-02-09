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
 * String property.
 *
 * @param <R> the root query bean type
 */
public final class PString<R> extends PBaseComparable<R, String> {

  /**
   * Construct with a property name and root instance.
   *
   * @param name property name
   * @param root the root query bean instance
   */
  public PString(String name, R root) {
    super(name, root);
  }

  /**
   * Construct with additional path prefix.
   */
  public PString(String name, R root, String prefix) {
    super(name, root, prefix);
  }

  /**
   * Case insensitive is equal to.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R ieq(String value) {
    expr().ieq(_name, value);
    return _root;
  }

  /**
   * Case insensitive is equal to.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R iequalTo(String value) {
    expr().ieq(_name, value);
    return _root;
  }

  /**
   * Like - include '%' and '_' placeholders as necessary.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R like(String value) {
    expr().like(_name, value);
    return _root;
  }

  /**
   * Starts with - uses a like with '%' wildcard added to the end.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R startsWith(String value) {
    expr().startsWith(_name, value);
    return _root;
  }

  /**
   * Ends with - uses a like with '%' wildcard added to the beginning.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R endsWith(String value) {
    expr().endsWith(_name, value);
    return _root;
  }

  /**
   * Contains - uses a like with '%' wildcard added to the beginning and end.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R contains(String value) {
    expr().contains(_name, value);
    return _root;
  }

  /**
   * Case insensitive like.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R ilike(String value) {
    expr().ilike(_name, value);
    return _root;
  }

  /**
   * Case insensitive starts with.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R istartsWith(String value) {
    expr().istartsWith(_name, value);
    return _root;
  }

  /**
   * Case insensitive ends with.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R iendsWith(String value) {
    expr().iendsWith(_name, value);
    return _root;
  }

  /**
   * Case insensitive contains.
   *
   * @param value the equal to bind value
   * @return the root query bean instance
   */
  public R icontains(String value) {
    expr().icontains(_name, value);
    return _root;
  }

}
