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
 * JSON document type.
 * <p>
 * Type that is JSON content mapped to database types such as Postgres JSON/JSONB and otherwise Varchar,Clob and Blob.
 * <p>
 * The expressions on this type are valid of Postgres and Oracle.
 * <p>
 * The path can reference a nested property in the JSON document using dot notation -
 * for example "documentMeta.score" where "score" is an embedded attribute of "documentMeta"
 *
 * @param <R> the root query bean type
 */
public final class PJson<R> extends TQPropertyBase<R, String> {

  /**
   * Construct with a property name and root instance.
   *
   * @param name property name
   * @param root the root query bean instance
   */
  public PJson(String name, R root) {
    super(name, root);
  }

  /**
   * Construct with additional path prefix.
   */
  public PJson(String name, R root, String prefix) {
    super(name, root, prefix);
  }

  /**
   * Path exists - for the given path in a JSON document.
   *
   * <pre>{@code
   *
   *   new QSimpleDoc()
   *    .content.jsonExists("meta.title")
   *    .findList();
   *
   * }</pre>
   *
   * @param path the nested path in the JSON document in dot notation
   */
  public R jsonExists(String path) {
    expr().jsonExists(_name, path);
    return _root;
  }

  /**
   * Path does not exist - for the given path in a JSON document.
   *
   * <pre>{@code
   *
   *   new QSimpleDoc()
   *    .content.jsonNotExists("meta.title")
   *    .findList();
   *
   * }</pre>
   *
   * @param path the nested path in the JSON document in dot notation
   */
  public R jsonNotExists(String path) {
    expr().jsonNotExists(_name, path);
    return _root;
  }

  /**
   * Value at the given JSON path is equal to the given value.
   *
   *
   * <pre>{@code
   *
   *   new QSimpleDoc()
   *    .content.jsonEqualTo("title", "Rob JSON in the DB")
   *    .findList();
   *
   * }</pre>
   *
   * <pre>{@code
   *
   *   new QSimpleDoc()
   *    .content.jsonEqualTo("path.other", 34)
   *    .findList();
   *
   * }</pre>
   *
   * @param path  the dot notation path in the JSON document
   * @param value the equal to bind value
   */
  public R jsonEqualTo(String path, Object value) {
    expr().jsonEqualTo(_name, path, value);
    return _root;
  }

  /**
   * Not Equal to - for the given path in a JSON document.
   *
   * @param path  the nested path in the JSON document in dot notation
   * @param value the value used to test equality against the document path's value
   */
  public R jsonNotEqualTo(String path, Object value) {
    expr().jsonNotEqualTo(_name, path, value);
    return _root;
  }

  /**
   * Greater than - for the given path in a JSON document.
   *
   * @param path  the nested path in the JSON document in dot notation
   * @param value the value used to test against the document path's value
   */
  public R jsonGreaterThan(String path, Object value) {
    expr().jsonGreaterThan(_name, path, value);
    return _root;
  }

  /**
   * Greater than or equal to - for the given path in a JSON document.
   *
   * @param path  the nested path in the JSON document in dot notation
   * @param value the value used to test against the document path's value
   */
  public R jsonGreaterOrEqual(String path, Object value) {
    expr().jsonGreaterOrEqual(_name, path, value);
    return _root;
  }

  /**
   * Less than - for the given path in a JSON document.
   *
   * @param path  the nested path in the JSON document in dot notation
   * @param value the value used to test against the document path's value
   */
  public R jsonLessThan(String path, Object value) {
    expr().jsonLessThan(_name, path, value);
    return _root;
  }

  /**
   * Less than or equal to - for the given path in a JSON document.
   *
   * @param path  the nested path in the JSON document in dot notation
   * @param value the value used to test against the document path's value
   */
  public R jsonLessOrEqualTo(String path, Object value) {
    expr().jsonLessOrEqualTo(_name, path, value);
    return _root;
  }
}
