plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    // alias(libs.plugins.kotlinMultiplatform)
    // alias(libs.plugins.kotlinxSerialization)
}

group = "top.lolosia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val nativeTarget = mingwX64("native")

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "top.lolosia.installer.main"
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
        }
    }
}
