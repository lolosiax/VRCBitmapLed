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

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.commons.collections4.BidiMap
import org.slf4j.LoggerFactory
import top.lolosia.vrc.led.BitmapLed
import top.lolosia.vrc.led.util.kotlin.toBidiMap

/**
 * special_font
 * @author 洛洛希雅Lolosia
 * @since 2025-02-09 16:31
 */
private val logger = LoggerFactory.getLogger("top.lolosia.vrc.led.business.SpecialFontKt")
private val jsonMapper = JsonMapper()

private val strMap: BidiMap<String, Char> by lazy {
    val bis = BitmapLed::class.java.getResourceAsStream("char_map.json")
    bis?.use {
        jsonMapper.readValue<Map<String, String>>(it)
            .map { (k, v) ->
                v to k.toInt(16).toChar()
            }.toMap().toBidiMap()
    } ?: run {
        logger.warn("no char_map.json file found!")
        emptyMap<String, Char>().toBidiMap()
    }
}

fun String.toOscString(): String {
    var text = this
    var target = ""
    while (text.isNotEmpty()) {
        if (text.length == 1) {
            target += text[0]
            break
        }
        val key = text.slice(0..1)
        if (key in strMap) {
            target += strMap[key]
            text = text.slice(2 until text.length)
            continue
        }
        target += text[0]
        text = text.slice(1 until text.length)
    }
    return target
}

fun String.toOsc() = toOscString().map { OscChar(it) }

fun String.decodeOscString() = map { if (it in strMap.values) strMap.getKey(it) else it }.joinToString("")
fun List<OscChar>.decodeOscString() = map { it.char }.joinToString("").decodeOscString()
infix fun List<OscChar>.color(color: Int): List<OscChar> = map { it color color }
infix fun List<OscChar>.background(color: Int): List<OscChar> = map { it background color }