@file:OptIn(ExperimentalForeignApi::class)

package top.lolosia.installer

import kotlinx.cinterop.*
import top.lolosia.jni.*
import platform.windows.*
import kotlin.system.exitProcess

typealias JNI_CreateJavaVM_t = CFunction<(
    pvm: CValuesRef<CPointerVar<JavaVMVar>>?, // JavaVM**
    penv: CValuesRef<COpaquePointerVar>?, // JNIEnv**
    args: CValuesRef<*>? // void*
) -> Int> // jint

@OptIn(ExperimentalForeignApi::class)
fun main() {
    winForm()
    if (1 == 1) return

    val dllPath = "jre\\bin\\server\\jvm.dll"

    val hModule = LoadLibraryW(dllPath)
    if (hModule == null) {
        val errorCode = GetLastError()
        println("Failed to load $dllPath, error: $errorCode")
        exitProcess(-1)
    } else {
        println("Successfully loaded jvm.dll at $dllPath")
    }

    val proc = GetProcAddress(hModule, "JNI_CreateJavaVM")!!

    val createJavaVM = proc.reinterpret<JNI_CreateJavaVM_t>()

    memScoped {
        val p_vm = alloc<CPointerVar<JavaVMVar>>()
        val p_env = alloc<CPointerVar<JNIEnvVar>>()
        val args: COpaquePointer? = `null`

        val result = createJavaVM(p_vm.ptr, p_env.reinterpret<COpaquePointerVar>().ptr, args)
        if (result != 0) {
            println("Failed to create JVM. Error code: $result")
        } else {
            println("JVM created successfully")
        }
    }

    return
}