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

package top.lolosia.installer

import kotlinx.cinterop.*
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import platform.windows.GetLastError
import platform.windows.GetProcAddress
import platform.windows.LoadLibraryW
import top.lolosia.jni.*
import kotlin.system.exitProcess

/**
 * jvm
 * @author 洛洛希雅Lolosia
 * @since 2025-02-21 18:41
 */

typealias JNI_CreateJavaVM_t = CFunction<(
    pvm: CValuesRef<CPointerVar<JavaVMVar>>?, // JavaVM**
    penv: CValuesRef<COpaquePointerVar>?, // JNIEnv**
    args: CValuesRef<*>? // void*
) -> Int> // jint

typealias NewObject_t = CFunction<(CPointer<JNIEnvVar>?, jclass?, jmethodID?) -> jobject?>

fun runJvm() {

    val dllPath = "D:\\Java\\jdk-21.0.4.7-hotspot\\bin\\server\\jvm.dll"
    // val dllPath = "jre\\bin\\server\\jvm.dll"

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
        val vmArgs = alloc<JavaVMInitArgs>()
        val vmOptions = allocArrayOfPointersTo<JavaVMOption>()
        // val vmOptions = cValuesOf<JavaVMOption>()

        vmArgs.version = JNI_VERSION_21
        vmArgs.options = vmOptions.pointed.value
        vmArgs.nOptions = 0

        val result = createJavaVM(
            jvm.ptr,
            jEnv.reinterpret<COpaquePointerVar>().ptr,
            vmArgs.reinterpret<COpaquePointerVar>().ptr
        )
        if (result != 0) {
            throw RuntimeException("Failed to create JVM. Error code: $result")
        }
        println("JVM created successfully")

        val env = jEnv.pointed!!.value!!.pointed

        // val inte = alloc<JNINativeInterface_>()
        var classLoaderClazz: jclass? = null
        getClassloaderJarCollection().use { iter ->
            iter.forEach { (name, data) ->
                if (!name.endsWith(".class")) return@forEach
                val name1 = name.removeSuffix(".class")
                // println(name1)
                val clazz = data.usePinned { pinned ->
                    env.DefineClass!!(jEnv.value, name1.cstr.ptr, null, pinned.addressOf(0), data.size)
                }

                if (clazz == null) jEnv.checkException()

                if (name1 == "top/lolosia/installer/WinResourceClassLoader") {
                    classLoaderClazz = clazz
                }
            }
        }
        if (classLoaderClazz == null) {
            throw IllegalStateException("Class \"top.lolosia.installer.WinResourceClassLoader\" doesn't exist!")
        }

        val methods = allocArray<JNINativeMethod>(2)
        // 方法 1: loadFromResource
        methods[0].fnPtr = staticCFunction(::loadFromResource)
        methods[0].name = "loadFromResource".cstr.ptr
        methods[0].signature = "(Ljava/lang/String;)[B".cstr.ptr

        // 方法 2: getResourcesNames
        methods[1].fnPtr = staticCFunction(::getResourcesNames)
        methods[1].name = "getResourcesNames".cstr.ptr
        methods[1].signature = "()[Ljava/lang/String;".cstr.ptr

        // 方法 3: getLibraries
        methods[2].fnPtr = staticCFunction(::getLibraries)
        methods[2].name = "getLibraries".cstr.ptr
        methods[2].signature = "()[Ljava/lang/String;".cstr.ptr

        val status = env.RegisterNatives!!(jEnv.value, classLoaderClazz, methods, 3)
        if (status != JNI_OK) {
            jEnv.checkException()
            throw IllegalStateException("RegisterNatives failed")
        }

        val constructorMethodId = env.GetMethodID!!(jEnv.value, classLoaderClazz, "<init>".cstr.ptr, "()V".cstr.ptr)
        constructorMethodId ?: jEnv.checkException()
        val NewObject = env.NewObject!!.reinterpret<NewObject_t>()
        val classLoader = NewObject(jEnv.value, classLoaderClazz, constructorMethodId)
        classLoader ?: jEnv.checkException()

        println("Create ClassLoader successfully!")
    }

}

private fun loadFromResource(env: CPointer<JNIEnvVar>, clazz: jclass, resourcePath: jstring): jbyteArray? {
    memScoped {
        val util = env.getUtil()
        val len = util.GetStringUTFLength!!(env, resourcePath)
        val chars = util.GetStringUTFChars!!(env, resourcePath, null)
        val bytes = chars?.readBytes(len) ?: byteArrayOf()
        val resourcePathStr = bytes.decodeToString()

        val data = getInstallerJarCollection().use { iter ->
            iter.find { it.fileName == resourcePathStr }?.data
        }
        if (data == null) {
            val cls = util.FindClass!!(env, "java/io/FileNotFoundException".cstr.ptr)
            cls ?: env.checkException()
            util.ThrowNew!!(env, cls, "Can not found the resources: $resourcePathStr".cstr.ptr)
            return null
        }
        val jBytearray = util.NewByteArray!!(env, data.size)
        data.usePinned { pinned ->
            util.SetByteArrayRegion!!(env, jBytearray, 0, data.size, pinned.addressOf(0))
        }
        return jBytearray
    }
}

private fun getResourcesNames(env: CPointer<JNIEnvVar>, clazz: jclass): jobjectArray? {
    memScoped {
        // 返回资源名称
        val files = getInstallerJarCollection().use { iter -> iter.map { it.fileName } }
        val util = env.getUtil()
        val clazz0 = util.FindClass!!(env, "java/lang/String".cstr.ptr);
        val objArray = util.NewObjectArray!!(env, files.size, clazz0, null)
        files.forEachIndexed { i, it ->
            val jStr = util.NewStringUTF!!(env, it.cstr.ptr)
            util.SetObjectArrayElement!!(env, objArray, i, jStr)
        }
        return objArray
    }
}


private fun getLibraries(env: CPointer<JNIEnvVar>, clazz: jclass): jobjectArray? {
    var baseDir = Path("library")
    if (!SystemFileSystem.exists(baseDir)) SystemFileSystem.createDirectories(baseDir)
    baseDir = SystemFileSystem.resolve(baseDir)

    memScoped {
        // 返回资源名称
        val dependencies = getDependencies()
        val files = dependencies.dependencies.map {
            val fileName = it.url.split('/').last()
            Path(baseDir, it.group, it.name, it.version, fileName).toString().replace("\\", "/")
        }
        val util = env.getUtil()
        val clazz0 = util.FindClass!!(env, "java/lang/String".cstr.ptr);
        val objArray = util.NewObjectArray!!(env, files.size, clazz0, null)
        files.forEachIndexed { i, it ->
            val jStr = util.NewStringUTF!!(env, it.cstr.ptr)
            util.SetObjectArrayElement!!(env, objArray, i, jStr)
        }
        return objArray
    }
}

fun CPointerVar<JNIEnvVar>.checkException(clear: Boolean = true) = value!!.checkException(clear)

fun CPointer<JNIEnvVar>.checkException(clear: Boolean = true) {
    val env = getUtil()
    val exception = env.ExceptionOccurred!!(this)
    if (exception != null) {
        env.ExceptionDescribe!!(this)
        env.ExceptionClear!!(this)
        throw RuntimeException("Jvm thrown an exception, see log for more details.")
    }
}


fun CPointer<JNIEnvVar>.getUtil() = pointed.value!!.pointed

fun CPointerVar<JNIEnvVar>.getUtil() = pointed!!.value!!.pointed