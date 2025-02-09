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
 * Base scalar property.
 *
 * @param <R> The type of the owning root bean
 * @param <T> The property type
 */
public abstract class TQPropertyBase<R, T> extends TQProperty<R, T> {

  /**
   * Construct with a property name and root instance.
   *
   * @param name the name of the property
   * @param root the root query bean instance
   */
  public TQPropertyBase(String name, R root) {
    super(name, root);
  }

  /**
   * Construct with additional path prefix.
   */
  public TQPropertyBase(String name, R root, String prefix) {
    super(name, root, prefix);
  }

  /**
   * Order by ascending on this property.
   */
  public final R asc() {
    expr().orderBy().asc(_name);
    return _root;
  }

  /**
   * Order by descending on this property.
   */
  public final R desc() {
    expr().orderBy().desc(_name);
    return _root;
  }

}
