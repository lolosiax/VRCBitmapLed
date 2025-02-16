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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import top.lolosia.minizip.*

/**
 * deps
 * @author 洛洛希雅Lolosia
 * @since 2025-02-16 17:09
 */
@Serializable
data class JarDependency(
    val group: String,
    val name: String,
    val version: String,
    val extension: String,
    val classifier: String,
    val sha256: String,
    val url: String
)

@Serializable
data class DependencyFile(
    val repositories: List<String>,
    val dependencies: List<JarDependency>
)

@OptIn(ExperimentalForeignApi::class)
fun getDeps(): DependencyFile {
    var depJson : String? = null
    memScoped {
        val memStream = alloc<COpaquePointerVar>();
        memStream.value = mz_stream_mem_create()
        val reader = alloc<COpaquePointerVar>()
        reader.value = mz_zip_reader_create()

        val data = getJar()
        val pData = data.refTo(0).getPointer(this)
        try {
            mz_stream_mem_set_buffer(memStream.value, pData, data.size)
            mz_zip_reader_set_encoding(reader.value, MZ_ENCODING_UTF8)
            var status = 0
            status = mz_zip_reader_open(reader.value, memStream.value)
            // status = mz_zip_reader_open_file(reader, zipPath)
            if (status != MZ_OK) throw RuntimeException("Cannot open internal jar file")
            status = mz_zip_reader_goto_first_entry(reader.value)
            if (status != MZ_OK) throw RuntimeException("Cannot goto first entry.")
            do {
                val zipFile = alloc<CPointerVar<mz_zip_file>>()
                status = mz_zip_reader_entry_get_info(reader.value, zipFile.ptr)
                if (status != MZ_OK) continue

                val meta = zipFile.pointed ?: continue
                val fileName = meta.filename?.toKString()
                if (fileName != "dependencies-urls.json") continue

                val depSize = meta.uncompressed_size.toInt()
                val depData = ByteArray(depSize)
                val depPtr = depData.refTo(0).getPointer(this)
                status = mz_zip_reader_entry_open(reader.value)
                if (status != MZ_OK) throw RuntimeException("Cannot open dependencies file")
                mz_zip_reader_entry_read(reader.value, depPtr, depSize)
                depJson = depData.decodeToString()
                mz_zip_reader_entry_close(reader.value)
            } while (mz_zip_reader_goto_next_entry(reader.value) == MZ_OK)
        }
        finally {
            mz_zip_reader_close(reader.value)
            mz_zip_reader_delete(reader.ptr)
            mz_stream_mem_close(memStream.value)
            mz_stream_mem_delete(memStream.ptr)
        }
    }
    depJson ?: throw RuntimeException("dependencies-urls.json not found!")

    return Json.decodeFromString<DependencyFile>(depJson!!)
}