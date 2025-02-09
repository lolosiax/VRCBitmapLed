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


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.io.path.Path
import kotlin.io.path.absolute

buildscript {
    dependencies {
        classpath("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    }
}

plugins {
    java
}

tasks.jar {
    dependsOn("buildResources")
    archiveFileName = "vrc-led-web-${rootProject.version}.jar"

    from(buildDir.resolve("tmp/webJar/resources/"))
    manifest {
        attributes["Implementation-Version"] = rootProject.version
        attributes["Implementation-Title"] = rootProject.name
    }
}

task("buildResources") {
    group = "build"

    doLast {
        val buildDir = project.buildDir
        delete(buildDir.resolve("tmp/webJar"))

        // 构建多个平台文件
        runBlocking {
            buildPlatform("bitmapLed", rootProject.projectDir.resolve("../BitmapLedJavaWeb").absolutePath)
        }
    }
}

suspend fun buildPlatform(name: String, dirPath: String) {
    val path = Path(dirPath).absolute()
    val vite = path.resolve("node_modules/.bin/vite.CMD")
    suspendCoroutine {
        val process = ProcessBuilder(vite.toString(), "build", "--mode", "build").apply {
            directory(path.toFile())
            val env = environment()
            env["VITE_APP_BASE_MODE"] = "local"
            env["VITE_BUILD_TIMESTAMP"] = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(Date())
            env["VITE_PROJECT_VERSION"] = rootProject.version.toString()
        }.start()

        CoroutineScope(Dispatchers.IO).launch {
            process.inputStream.transferTo(System.out)
        }
        CoroutineScope(Dispatchers.IO).launch {
            process.errorStream.transferTo(System.err)
        }

        process.onExit().thenAccept { p1 ->
            if (p1.exitValue() != 0) {
                it.resumeWithException(RuntimeException("Vite的退出值不为0"))
            } else it.resume(0)
        }
    }

    copy {
        into(buildDir.resolve("tmp/webJar/resources/static/$name"))
        from(path.resolve("dist").toString())
    }
}