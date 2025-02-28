package top.lolosia.installer.util.dependency

import kotlinx.serialization.Serializable


@Serializable
data class DependencyFile(
    val repositories: List<String>,
    val dependencies: List<JarDependency>
)