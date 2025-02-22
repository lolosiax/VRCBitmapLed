package top.lolosia.installer

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val name = getCurrentProcessFileName()
    println(name)
    Installer.main()
}