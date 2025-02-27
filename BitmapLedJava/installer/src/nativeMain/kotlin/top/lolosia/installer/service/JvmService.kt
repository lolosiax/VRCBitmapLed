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

package top.lolosia.installer.service

import kotlinx.cinterop.*
import kotlinx.io.files.SystemFileSystem
import top.lolosia.installer.Installer
import top.lolosia.installer.util.jvm.JavaVMBuilder
import top.lolosia.installer.util.jvm.JavaVMBuilderResult
import top.lolosia.jni.*

/**
 * jvm
 * @author 洛洛希雅Lolosia
 * @since 2025-02-21 18:41
 */

class JvmService {

    val jvmPath = ""

    // val dllPath = "D:\\Java\\jdk-21.0.4.7-hotspot\\bin\\server\\jvm.dll"
    val dllPath = "jre\\bin\\server\\jvm.dll"

    private lateinit var result: JavaVMBuilderResult

    val jvm: CPointer<JavaVMVar> get() = result.jvm
    val jEnv: CPointer<JNIEnvVar> get() = result.jEnv

    private fun createJavaVM() {
        val classPath = Installer.environmentService.dependencyFiles.map {
            SystemFileSystem.resolve(it).toString()
        }.joinToString(";")
        result = JavaVMBuilder(jvmPath).apply {
            vmArgs(
                "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005",
                "-cp=$classPath",
            )
            mainClass("top.lolosia.vrc.led.Launcher")
        }.build()
    }

    fun launch() {
        if (::result.isInitialized) return
        createJavaVM()
        result.launch()
    }
}

