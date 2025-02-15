plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    // alias(libs.plugins.kotlinMultiplatform)
    // alias(libs.plugins.kotlinxSerialization)
}

group = "top.lolosia"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        name = "localRepo"
        val dir = projectDir.resolve("repo").absoluteFile
        if (!dir.exists()) dir.mkdirs()
        setUrl(layout.buildDirectory.dir("file://${dir}"))
    }
    mavenCentral()
}

kotlin {
    val nativeTarget = mingwX64("native")

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "top.lolosia.installer.main"

                runTask?.args("")
                linkerOpts("-Wl,--subsystem,windows")

                windowsResources("$projectDir/resources/installer.rc")
            }
        }
        compilations.getByName("main") {
            cinterops {
                create("jni"){
                    defFile("src/nativeInterop/cinterop/jni.def")
                    includeDirs(
                        "src/nativeInterop/cinterop/jni",
                        "src/nativeInterop/cinterop/jni/win32",
                        "src/nativeInterop/cinterop/jni/win32/bridge"
                    )
                }
            }
        }
    }

    sourceSets {
        nativeMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
            implementation("com.github.msink:libui:0.1.9")
        }
    }
}

fun org.jetbrains.kotlin.gradle.plugin.mpp.Executable.windowsResources(rcFileName: String) {
    val taskName = linkTaskName.replaceFirst("link", "windres")
    val inFile = File(rcFileName)
    val outFile = buildDir.resolve("processedResources/$taskName.res")

    val windresTask = tasks.create<Exec>(taskName) {
        val konanDataDir = System.getenv("KONAN_DATA_DIR") ?: "${System.getProperty("user.home")}/.konan"
        val toolchainBinDir = when (target.konanTarget.architecture.bitness) {
            32 -> "$konanDataDir/dependencies/msys2-mingw-w64-i686-2/bin"
            64 -> "$konanDataDir/dependencies/msys2-mingw-w64-x86_64-2/bin"
            else -> error("Unsupported architecture")
        }

        inputs.file(inFile)
        outputs.file(outFile)
        commandLine("$toolchainBinDir/windres", inFile, "-D_${buildType.name}", "-O", "coff", "-o", outFile)
        environment("PATH", "$toolchainBinDir;${System.getenv("PATH")}")

        dependsOn(compilation.compileKotlinTask)
    }

    linkTask.dependsOn(windresTask)
    linkerOpts(outFile.toString())
}