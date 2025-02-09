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

package top.lolosia.vrc.led.business

/**
 * OSC字符，高8位表示色彩，低16位表示UTF-16 BE
 * @author 洛洛希雅Lolosia
 * @since 2025-02-09 15:47
 */
@JvmInline
value class OscChar(val value: Int) {
    constructor() : this(0)
    constructor(char: Char, color: UByte = 0x00U) : this(char.code, color)
    constructor(char: Int, color: UByte) : this((color.toInt() shl 16) or (char and 0xFFFF))
    constructor(
        color: UByte,
        high: UByte,
        low: UByte
    ) : this((color.toInt() shl 16) or (high.toInt() shl 8) or low.toInt())

    operator fun component1(): UByte = (value shr 16 and 0xFF).toUByte()
    operator fun component2(): UByte = (value shr 8 and 0xFF).toUByte()
    operator fun component3(): UByte = (value and 0xFF).toUByte()

    val char get() = (value and 0xFFFF).toChar()
    val color: Int get() = decodeColor(component1())
    val colorUByte: UByte get() = component1()
    val isBackgroundColor get() = isBackgroundColor(component1())

    infix fun color(color: Int): OscChar = OscChar(char, color.nearestForeground())
    infix fun background(color: Int): OscChar = OscChar(char, color.nearestBackground())
    infix fun char(char: Char): OscChar = OscChar(char, component1())
}