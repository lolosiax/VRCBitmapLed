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

package top.lolosia.installer

import kotlinx.cinterop.*
import platform.windows.FindResourceW
import platform.windows.LoadResource
import platform.windows.LockResource
import platform.windows.SizeofResource
import top.lolosia.minizip.*

/**
 * jar
 * @author 洛洛希雅Lolosia
 * @since 2025-02-21 19:11
 */

interface JarCollection : Iterable<JarItem>, AutoCloseable
class JarItem(val fileName: String, data: () -> ByteArray) {
    val data by lazy(data)
    operator fun component1() = fileName
    operator fun component2() = data
}

fun getInstallerJarCollection() = getJarCollection0("IDR_JAR1")
fun getClassloaderJarCollection() = getJarCollection0("IDR_JAR2")

@OptIn(ExperimentalForeignApi::class)
private fun getJar0(name: String): Pair<COpaquePointer, Int> {
    memScoped {
        val hRes = FindResourceW(null, name, "JARFILE")
        hRes ?: throw RuntimeException("Internal jar '$name' not found!")
        val size = SizeofResource(null, hRes)
        val hData = LoadResource(null, hRes)
        val jarData = LockResource(hData)!!
        return jarData to size.toInt()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun getJarCollection0(name: String): JarCollection {
    val arena = Arena()
    var reader: COpaquePointerVar? = null
    var memStream: COpaquePointerVar? = null
    var item: CPointerVar<mz_zip_file>? = null
    var closed = false
    fun close0() {
        closed = true
        reader?.let {
            mz_zip_reader_close(it.value)
            mz_zip_reader_delete(it.ptr)
        }
        memStream?.let {
            mz_stream_mem_close(it.value)
            mz_stream_mem_delete(it.ptr)
        }
        arena.clear()
    }

    try {
        arena.apply {
            val memStream1 = alloc<COpaquePointerVar>();
            memStream1.value = mz_stream_mem_create()
            memStream = memStream1

            val reader1 = alloc<COpaquePointerVar>()
            reader1.value = mz_zip_reader_create()
            reader = reader1

            item = alloc<CPointerVar<mz_zip_file>>()
            val (data, size) = getJar0(name)
            mz_stream_mem_set_buffer(memStream1.value, data, size)
            mz_zip_reader_set_encoding(reader1.value, MZ_ENCODING_UTF8)
            var status = 0
            status = mz_zip_reader_open(reader1.value, memStream1.value)
            if (status != MZ_OK) throw RuntimeException("Cannot open internal jar file")
            status = mz_zip_reader_goto_first_entry(reader1.value)
            if (status != MZ_OK) throw RuntimeException("Cannot goto first entry.")
        }
    } catch (e: Throwable) {
        close0()
        throw e
    }

    return object : JarCollection {
        override fun iterator(): Iterator<JarItem> = object : Iterator<JarItem> {
            var index = -1
            override fun hasNext(): Boolean {
                val reader1 = reader!!
                // 忽略文件夹结尾的文件
                while (true) {
                    var status = mz_zip_reader_goto_next_entry(reader!!.value)
                    if (status != MZ_OK) return false

                    val item1 = item!!
                    status = mz_zip_reader_entry_get_info(reader1.value, item1.ptr)
                    if (status != MZ_OK) throw RuntimeException("Can not get entry info.")

                    val meta = item!!.pointed ?: throw NullPointerException("Can not get entry info.")
                    if (meta.filename!!.toKString().endsWith("/")) continue

                    index++
                    return true
                }
            }

            override fun next(): JarItem {
                val index0 = index
                val reader1 = reader!!
                val meta = item!!.pointed!!

                val fileName = meta.filename?.toKString() ?: throw NullPointerException("Can not get entry file name.")
                // println(fileName)

                return JarItem(fileName) {
                    if (closed) {
                        throw IllegalStateException("Can not get entry file data, because the jar file has been closed.")
                    }
                    if (index0 != index) {
                        throw IllegalStateException("Can not get entry file data, because iterator indices are not equal.")
                    }
                    try {
                        arena.run {
                            val depSize = meta.uncompressed_size.toInt()
                            val depData = ByteArray(depSize)
                            val depPtr = depData.refTo(0).getPointer(this)
                            val status = mz_zip_reader_entry_open(reader1.value)
                            if (status != MZ_OK) throw RuntimeException("Cannot open entry file")
                            mz_zip_reader_entry_read(reader1.value, depPtr, depSize)
                            depData
                        }
                    } finally {
                        mz_zip_reader_entry_close(reader1.value)
                    }
                }
            }
        }


        override fun close() = close0()
    }
}