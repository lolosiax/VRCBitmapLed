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
 * Integer property.
 *
 * @param <R> the root query bean type
 */
public final class PInteger<R> extends PBaseNumber<R,Integer> {

  /**
   * Construct with a property name and root instance.
   *
   * @param name property name
   * @param root the root query bean instance
   */
  public PInteger(String name, R root) {
    super(name, root);
  }

  /**
   * Construct with additional path prefix.
   */
  public PInteger(String name, R root, String prefix) {
    super(name, root, prefix);
  }

  /**
   * Add bitwise AND expression of the given bit flags to compare with the match/mask.
   * <p>
   * <pre>{@code
   *
   * // Flags Bulk + Size = Size
   * // ... meaning Bulk is not set and Size is set
   *
   * int selectedFlags = BwFlags.HAS_BULK + BwFlags.HAS_SIZE;
   * int mask = BwFlags.HAS_SIZE; // Only Size flag set
   *
   * bitwiseAnd(selectedFlags, mask)
   *
   * }</pre>
   *
   * @param flags        The flags we are looking for
   */
  public R bitwiseAnd(int flags, int mask) {
    expr().bitwiseAnd(_name, flags, mask);
    return _root;
  }

  /**
   * Add expression for ALL of the given bit flags to be set.
   * <pre>{@code
   *
   * bitwiseAll(BwFlags.HAS_BULK + BwFlags.HAS_COLOUR)
   *
   * }</pre>
   *
   * @param flags        The flags we are looking for
   */
  public R bitwiseAll(int flags) {
    expr().bitwiseAll(_name, flags);
    return _root;
  }

  /**
   * Add expression for ANY of the given bit flags to be set.
   * <pre>{@code
   *
   * bitwiseAny(BwFlags.HAS_BULK + BwFlags.HAS_COLOUR)
   *
   * }</pre>
   *
   * @param flags        The flags we are looking for
   */
  public R bitwiseAny(int flags) {
    expr().bitwiseAny(_name, flags);
    return _root;
  }

  /**
   * Add expression for the given bit flags to be NOT set.
   * <pre>{@code
   *
   * bitwiseNot(BwFlags.HAS_COLOUR)
   *
   * }</pre>
   *
   * @param flags        The flags we are looking for
   */
  public R bitwiseNot(int flags) {
    expr().bitwiseNot(_name, flags);
    return _root;
  }
}
