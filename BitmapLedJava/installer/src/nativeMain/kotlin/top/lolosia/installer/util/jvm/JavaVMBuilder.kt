package top.lolosia.installer.util.jvm

import kotlinx.cinterop.*
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import platform.windows.GetLastError
import platform.windows.GetProcAddress
import platform.windows.HMODULE
import platform.windows.LoadLibraryW
import top.lolosia.jni.*

class JavaVMBuilder(val javaHome: String = "") {
    val vmArgs = mutableListOf<String>()
    val classpath = mutableListOf<Path>()
    var mainClass: String? = null
    val launchArgs = mutableListOf<String>()

    val dllPath by lazy {
        SystemFileSystem.resolve(Path(javaHome, "jre\\bin\\server\\jvm.dll")).toString()
    }

    val hModule: HMODULE by lazy {
        val hModule = LoadLibraryW(dllPath)
        if (hModule == null) {
            val errorCode = GetLastError()
            throw RuntimeException("Failed to load $dllPath, error code: $errorCode")
        }
        hModule
    }

    private val mCreateJavaVM by lazy {
        val proc = GetProcAddress(hModule, "JNI_CreateJavaVM")
        proc ?: throw RuntimeException(
            "Could not locate function JNI_CreateJavaVM(JavaVM**, void**, void*) on $dllPath. " +
                    "Make sure you selected the correct \"jvm.dll\""
        )
        proc.reinterpret<JNI_CreateJavaVM_t>()
    }

    fun vmArgs(args: Collection<String>): JavaVMBuilder {
        vmArgs.addAll(args)
        return this
    }

    fun vmArgs(vararg args: String): JavaVMBuilder {
        vmArgs.addAll(args)
        return this
    }

    fun classpath(cp : Collection<Path>) : JavaVMBuilder {
        classpath.addAll(cp)
        return this
    }

    fun classpath(vararg cp: Path): JavaVMBuilder {
        classpath.addAll(cp)
        return this
    }

    fun mainClass(mainClass: String): JavaVMBuilder {
        this.mainClass = mainClass
        return this
    }

    fun build(): JavaVMBuilderResult = memScoped {
        //
        // 创建JVM
        val jvm = alloc<CPointerVar<JavaVMVar>>()
        val jEnv = alloc<CPointerVar<JNIEnvVar>>()

        val options = listOf(
            "-Djava.class.path=" + classpath.joinToString(";") { SystemFileSystem.resolve(it).toString() }
        ) + vmArgs

        val vmOptions = allocArray<JavaVMOption>(options.size)
        options.forEachIndexed { i, it ->
            vmOptions[i].optionString = it.cstr.ptr
        }

        val pVmArgs = alloc<JavaVMInitArgs>()
        pVmArgs.version = JNI_VERSION_21
        pVmArgs.options = vmOptions
        pVmArgs.nOptions = options.size
        // pVmArgs.ignoreUnrecognized = 1U

        val result = mCreateJavaVM(
            jvm.ptr,
            jEnv.reinterpret<COpaquePointerVar>().ptr,
            pVmArgs.reinterpret<COpaquePointerVar>().ptr
        )
        if (result != 0) {
            throw RuntimeException("Failed to create JVM. Error code: $result")
        }

        JavaVMBuilderResult(jvm.pointed!!.ptr, jEnv.pointed!!.ptr, mainClass, launchArgs)
    }
}