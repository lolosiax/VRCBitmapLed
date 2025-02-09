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

import kotlin.math.pow

/**
 * nearest_color
 * @author 洛洛希雅Lolosia
 * @since 2025-02-09 17:14
 */

private data class ColorEntry(val r: Int, val g: Int, val b: Int, val col: Int, val row: Int)

private val foregroundColors by lazy { generateForegroundColors() }
private val backgroundColors by lazy { generateBackgroundColors() }

fun Int.nearestForeground() = findNearestForeground(this shr 16 and 0xff, this shr 8 and 0xff, this and 0xff)
fun findNearestForeground(r: Int, g: Int, b: Int): UByte {
    var minDist = Double.MAX_VALUE
    var bestCol = 0
    var bestRow = 0
    foregroundColors.forEach { (cr, cg, cb, cx, cy) ->
        val dist = colorDistance(r, g, b, cr, cg, cb)
        if (dist < minDist) {
            minDist = dist
            bestCol = cx
            bestRow = cy
        }
    }
    return (bestCol * 16 + bestRow).toUByte()
}

fun Int.nearestBackground() = findNearestBackground(this shr 16 and 0xff, this shr 8 and 0xff, this and 0xff)
fun findNearestBackground(r: Int, g: Int, b: Int): UByte {
    var minDist = Double.MAX_VALUE
    var bestCol = 0
    var bestRow = 0
    backgroundColors.forEach { (cr, cg, cb, cx, cy) ->
        val dist = colorDistance(r, g, b, cr, cg, cb)
        if (dist < minDist) {
            minDist = dist
            bestCol = cx
            bestRow = cy
        }
    }
    return (bestCol * 16 + (15 - bestRow)).toUByte()
}

fun decodeColor(color: UByte): Int {
    val row = color.toInt() / 16
    val col = color.toInt() % 16
    val (r, g, b) = foregroundColors.first { it.row == row && it.col == col }
    return r shl 16 or g shl 8 or b
}

fun isBackgroundColor(color: UByte) = !isForegroundColor(color)
fun isForegroundColor(color: UByte): Boolean{
    return color.toInt() / 16 < 13
}

private fun generateForegroundColors(): List<ColorEntry> {
    val colors = mutableListOf<ColorEntry>()
    for (x in 0..12) {
        for (imgY in 0..15) {
            val y = imgY
            val (r, g, b) = if (x == 0) {
                val grayValue = 1 - y / 15.0
                val value = (grayValue * 255).toInt()
                Triple(value, value, value)
            } else {
                val hue = (x - 1) * 30.0 / 360.0
                val s = imgY / 4
                val v = imgY % 4
                val sat = 0.25 + s * 0.25
                val value = 0.25 + v * 0.25
                hsvToRgb(hue, sat, value)
            }
            colors.add(ColorEntry(r, g, b, x, imgY))
        }
    }
    return colors
}

private fun generateBackgroundColors(): List<ColorEntry> {
    val colors = mutableListOf<ColorEntry>()
    // 处理彩色背景
    listOf(13, 14, 15).forEach { x ->
        for (yImg in 0..11) {
            val hue = yImg * 30.0 / 360.0
            val (sat, value) = when (x) {
                13 -> 1.0 to 0.25
                14 -> 1.0 to 1.0
                else -> 0.5 to 1.0
            }
            val (r, g, b) = hsvToRgb(hue, sat, value)
            colors.add(ColorEntry(r, g, b, x, yImg))
        }
    }
    // 处理灰阶背景
    for (xBg in 0..2) {
        for (yBg in 0..3) {
            val grayValue = (xBg * 3 + yBg) / 9.0
            val colorValue = (grayValue * 255).toInt()
            val column = (2 - xBg) + 13
            val row = yBg + 12
            colors.add(ColorEntry(colorValue, colorValue, colorValue, column, row))
        }
    }
    return colors
}

private fun colorDistance(r1: Int, g1: Int, b1: Int, r2: Int, g2: Int, b2: Int): Double {
    return (r1 - r2).toDouble().pow(2) +
            (g1 - g2).toDouble().pow(2) +
            (b1 - b2).toDouble().pow(2)
}

private fun hsvToRgb(h: Double, s: Double, v: Double): Triple<Int, Int, Int> {
    val c = v * s
    val x = c * (1 - kotlin.math.abs((h * 6) % 2 - 1))
    val m = v - c

    val (r1, g1, b1) = when ((h * 6).toInt()) {
        0 -> Triple(c, x, 0.0)
        1 -> Triple(x, c, 0.0)
        2 -> Triple(0.0, c, x)
        3 -> Triple(0.0, x, c)
        4 -> Triple(x, 0.0, c)
        5 -> Triple(c, 0.0, x)
        else -> Triple(0.0, 0.0, 0.0)
    }

    return Triple(
        ((r1 + m) * 255).toInt().coerceIn(0..255),
        ((g1 + m) * 255).toInt().coerceIn(0..255),
        ((b1 + m) * 255).toInt().coerceIn(0..255)
    )
}
