package top.lolosia.installer.util.threading

import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.synchronized
import kotlinx.cinterop.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.windows.*

open class Thread() {
    constructor(block: () -> Unit) : this() {
        mBlock = block
    }

    constructor(name: String?, block: () -> Unit) : this(block) {
        mName = name
    }

    private lateinit var mBlock: () -> Unit
    private val mLock = ReentrantLock()
    private var mName: String? = null
    open val name: String get() = mName ?: mThreadId?.let { "Thread-$it" } ?: "Thread-<unknown>"

    private var mThreadId: UInt? = null
    val threadId: UInt get() = mThreadId ?: throw IllegalStateException("Thread not started!")

    private var mState = State.Ready

    private var mHandle: HANDLE? = null
    val handle: HANDLE get() = mHandle!!

    private var mStableRef: StableRef<Thread>? = null

    private fun createThread() = memScoped {
        val lpThreadID = alloc<DWORDVar>()
        mStableRef = StableRef.create(this@Thread)

        mHandle = CreateThread(null, 0u, staticCFunction { it ->
            val ref0 = it!!.asStableRef<Thread>()
            val thread = ref0.get()
            ref0.dispose()
            thread.mStableRef = null
            try {
                thread.onStart()
                return@staticCFunction 0U
            } catch (e: Throwable) {
                e.printStackTrace()
                return@staticCFunction 0xFFFFU
            } finally {
                thread.onClose()
            }
        }, mStableRef!!.asCPointer(), 0u, lpThreadID.ptr)
        mThreadId = lpThreadID.value
    }

    protected open fun onStart() {
        if (!::mBlock.isInitialized) {
            throw NotImplementedError("Thread.onStart() is not implemented!")
        }
        mBlock()
    }

    private fun onClose() {
        synchronized(mLock) {
            mState = State.Exited
        }
        CoroutineScope(Dispatchers.Default).launch {
            WaitForSingleObject(mHandle, INFINITE)
            CloseHandle(mHandle)
            mHandle = null
        }
    }

    fun start() {
        synchronized(mLock) {
            if (mState != State.Ready) {
                throw IllegalStateException(
                    "Can not start the Thread, because thread is ${mState.name.lowercase()}!"
                )
            }
            mState = State.Running
            createThread()
        }
    }

    fun interrupt() {
        synchronized(mLock) {
            if (mState != State.Running) return
            mState = State.Exited
            TerminateThread(mHandle, 0xFFFFU)
            CloseHandle(mHandle)
            mHandle = null
        }
    }

    override fun toString(): String {
        return "${this::class.simpleName}(name=$name, state=$mState, id=$mThreadId)"
    }

    enum class State {
        Ready,
        Running,
        Exited
    }
}