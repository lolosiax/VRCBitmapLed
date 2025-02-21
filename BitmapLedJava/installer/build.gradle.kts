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
    // maven("https://maven.aliyun.com/nexus/content/groups/public/")
    mavenCentral()
}

kotlin {
    mingwX64("native") {
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
                create("jni") {
                    defFile("src/nativeInterop/cinterop/jni.def")
                    includeDirs(
                        "src/nativeInterop/cinterop/jni",
                        "src/nativeInterop/cinterop/jni/win32",
                        "src/nativeInterop/cinterop/jni/win32/bridge"
                    )
                }
                create("minizip") {
                    defFile("src/nativeInterop/cinterop/minizip.def")
                    includeDirs("src/nativeInterop/cinterop/minizip")
                }
            }

            kotlinOptions.freeCompilerArgs = listOf(
                "-include-binary", "$projectDir/src/nativeInterop/cinterop/minizip/libminizip.a",
                "-include-binary", "$projectDir/src/nativeInterop/cinterop/minizip/libbzip2.a",
                "-include-binary", "$projectDir/src/nativeInterop/cinterop/minizip/liblzma.a",
                "-include-binary", "$projectDir/src/nativeInterop/cinterop/minizip/libzstd.a",
                "-include-binary", "$projectDir/src/nativeInterop/cinterop/minizip/libz-ng.a",
            )
        }
    }

    jvm("java") {
        withJava()
    }

    sourceSets {
        nativeMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
            implementation("com.github.msink:libui:0.1.9")
            implementation("io.ktor:ktor-client-core:3.1.0")
            implementation("io.ktor:ktor-client-winhttp:3.1.0")
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

        dependsOn(
            compilation.compileTaskProvider.get(),
            ":installer:copyInstallerJar",
            ":installer:copyClassloaderJar"
        )
    }

    linkTaskProvider.get().dependsOn(windresTask)
    linkerOpts(outFile.toString())
}

tasks.register<Copy>("copyInstallerJar") {
    val jar = project(":").tasks["installerJar"] as Jar
    // dependsOn(jar)
    val file = jar.outputs.files.singleFile
    from(file)
    into("$projectDir/resources")
    rename("(.*).jar", "installer.jar")
}

tasks.register<Copy>("copyClassloaderJar") {
    val jar = tasks["javaJar"] as Jar
    dependsOn(jar)
    val file = jar.outputs.files.singleFile
    from(file)
    into("$projectDir/resources")
    rename("(.*).jar", "classloader.jar")
}