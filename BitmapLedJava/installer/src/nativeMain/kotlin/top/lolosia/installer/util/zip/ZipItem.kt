package top.lolosia.installer.util.zip

/**
 * Zip条目
 * @param fileName 文件名
 * @param data 用于获取数据的回调
 */
class ZipItem(val fileName: String, data: () -> ByteArray) {
    /**
     * 文件数据，只能在迭代器进入下一个条目之前使用。
     * @throws IllegalStateException 压缩包已关闭或迭代器已经离开当前条目
     */
    val data by lazy(data)
    operator fun component1() = fileName
    operator fun component2() = data
}