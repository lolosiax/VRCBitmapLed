package top.lolosia.installer.util.zip

/**
 * ZipCollection
 *
 * 此对象在使用结束后必须被关闭
 * @author 洛洛希雅Lolosia
 * @since 2025-02-21 19:11
 */
interface ZipCollection : Iterable<ZipItem>, AutoCloseable