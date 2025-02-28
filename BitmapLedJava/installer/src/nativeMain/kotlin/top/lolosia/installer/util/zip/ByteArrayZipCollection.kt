package top.lolosia.installer.util.zip

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.pin

internal class ByteArrayZipCollection(data: ByteArray) : ZipCollection {
    private val dataPtr = data.pin()
    private val zipCollection = ZipCollection(dataPtr.addressOf(0), data.size)

    override fun iterator() = zipCollection.iterator()

    override fun close() {
        dataPtr.unpin()
        zipCollection.close()
    }
}