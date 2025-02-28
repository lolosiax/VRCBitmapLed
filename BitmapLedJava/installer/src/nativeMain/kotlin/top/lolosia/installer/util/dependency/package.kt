package top.lolosia.installer.util.dependency

import kotlinx.serialization.json.Json
import top.lolosia.installer.util.jar.getInstallerJarCollection

fun getDependencies(): DependencyFile {
    val json = getInstallerJarCollection().use { iter ->
        iter.find { it.fileName == "dependencies-urls.json" }?.data?.decodeToString()
    }
    json ?: throw RuntimeException("dependencies-urls.json not found!")
    return Json.decodeFromString<DependencyFile>(json)
}