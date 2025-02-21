@file:OptIn(ExperimentalForeignApi::class)

package top.lolosia.installer

import kotlinx.cinterop.*
import platform.windows.GetLastError
import platform.windows.GetProcAddress
import platform.windows.LoadLibraryW
import top.lolosia.installer.form.libuiKtxMain
import top.lolosia.jni.*
import kotlin.system.exitProcess

typealias JNI_CreateJavaVM_t = CFunction<(
    pvm: CValuesRef<CPointerVar<JavaVMVar>>?, // JavaVM**
    penv: CValuesRef<COpaquePointerVar>?, // JNIEnv**
    args: CValuesRef<*>? // void*
) -> Int> // jint

@OptIn(ExperimentalForeignApi::class)
fun main() {
    libuiKtxMain()
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
        val jvm = alloc<CPointerVar<JavaVMVar>>()
        val jEnv = alloc<CPointerVar<JNIEnvVar>>()
        val args: COpaquePointer? = `null`

        val result = createJavaVM(jvm.ptr, jEnv.reinterpret<COpaquePointerVar>().ptr, args)
        if (result != 0) {
            println("Failed to create JVM. Error code: $result")
        } else {
            println("JVM created successfully")
        }

        val inte = alloc<JNINativeInterface_>()
        val bytes = byteArrayOf().refTo(0).getPointer(this)
        inte.DefineClass!!.invoke(jEnv.value, "Name".cstr.ptr, null, bytes, 0)
    }

    return
}