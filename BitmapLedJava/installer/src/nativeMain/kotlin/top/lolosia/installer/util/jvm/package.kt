package top.lolosia.installer.util.jvm

import kotlinx.cinterop.*
import top.lolosia.jni.*

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

typealias JNI_CreateJavaVM_t = CFunction<(
    pVm: CValuesRef<CPointerVar<JavaVMVar>>?, // JavaVM**
    pEnv: CValuesRef<COpaquePointerVar>?, // JNIEnv**
    args: CValuesRef<*>? // void*
) -> Int> // jint

typealias CallMainMethod_t = CFunction<(CPointer<JNIEnvVar>?, jclass?, jmethodID?, jobjectArray?) -> Unit>