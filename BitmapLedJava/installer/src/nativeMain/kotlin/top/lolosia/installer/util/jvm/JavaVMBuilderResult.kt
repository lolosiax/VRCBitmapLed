package top.lolosia.installer.util.jvm

import kotlinx.cinterop.*
import top.lolosia.jni.*

data class JavaVMBuilderResult(
    val jvm: CPointer<JavaVMVar>,
    val jEnv: CPointer<JNIEnvVar>,
    private val mainClass: String?,
    private val args: List<String>
) {
    fun launch() {
        mainClass ?: throw RuntimeException("No main class specified!")
        val util = jEnv.getUtil()

        @Suppress("LocalVariableName")
        val CallMainMethod = util.CallStaticVoidMethod!!.reinterpret<CallMainMethod_t>()

        var classMain : jclass?
        var mainMethodId: jmethodID?
        var objArray: jobjectArray?

        memScoped {
            val mainClass1 = mainClass.replace('.', '/')

            classMain = util.FindClass!!(jEnv, mainClass1.cstr.ptr)
            classMain ?: jEnv.checkException()

            mainMethodId = util.GetStaticMethodID!!(
                jEnv, classMain, "main".cstr.ptr, "([Ljava/lang/String;)V".cstr.ptr
            )
            mainMethodId ?: jEnv.checkException()

            val clazzString = util.FindClass!!(jEnv, "java/lang/String".cstr.ptr)
            objArray = util.NewObjectArray!!(jEnv, args.size, clazzString, null)
            objArray ?: jEnv.checkException()

            args.forEachIndexed { i, it ->
                val jStr = util.NewStringUTF!!(jEnv, it.cstr.ptr)
                util.SetObjectArrayElement!!(jEnv, objArray, i, jStr)
            }
        }

        CallMainMethod(jEnv, classMain, mainMethodId, objArray)
        jEnv.checkException()
    }
}