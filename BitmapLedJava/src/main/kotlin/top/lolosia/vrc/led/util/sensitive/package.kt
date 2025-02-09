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

package top.lolosia.vrc.led.util.sensitive

import org.slf4j.LoggerFactory
import top.lolosia.vrc.led.BitmapLed
import java.util.zip.GZIPInputStream

private val logger by lazy {
    LoggerFactory.getLogger(object {}::class.java.packageName)
}

val sensitiveWords: Map<Char, Array<String>> by lazy {

    try {
        val sensitive0 = mutableMapOf<Char, MutableList<String>>()
        val stream = BitmapLed::class.java.getResourceAsStream("sensitive_words_lines.bin")!!
        val gzip = GZIPInputStream(stream)
        val reader = gzip.reader().buffered()
        while (true) {
            val line = reader.readLine() ?: break
            if (line.isEmpty()) continue
            val list = sensitive0.getOrPut(line[0]) { mutableListOf() }
            list.add(line)
        }
        stream.close()
        sensitive0.mapValues { it.value.toTypedArray() }
    } catch (e: Throwable) {
        logger.error("读取敏感词信息失败", e)
        emptyMap()
    }
}

/**
 * 替换敏感词
 */
fun String.sensitive(): String {
    var out = this
    var index = 0;
    val length = this.length
    while (index < length) {
        val char = out[index]
        val maybe = sensitiveWords[char]

        if (!maybe.isNullOrEmpty()) {
            for (word in maybe) {
                if (out.startsWith(word, index)) {
                    out = out.replaceRange(index, index + word.length, "*".repeat(word.length))
                    index += word.length - 1
                    continue
                }
            }
        }

        index++
    }

    return out
}


/**
 * 判断本句话是否存在敏感词
 */
fun String.sensitiveCheck(): List<String> {
    val out = mutableListOf<String>()
    var index = 0;
    val length = this.length
    while (index < length) {
        val char = this[index]
        val maybe = sensitiveWords[char]

        if (!maybe.isNullOrEmpty()) {
            for (word in maybe) {
                if (this.startsWith(word, index)) {
                    out.add(word)
                    index += word.length - 1
                    continue
                }
            }
        }

        index++
    }

    return out
}