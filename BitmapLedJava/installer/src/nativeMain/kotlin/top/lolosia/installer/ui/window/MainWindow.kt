package top.lolosia.installer.ui.window

import kotlinx.coroutines.*
import libui.ktx.VBox
import libui.ktx.Window
import libui.ktx.appWindow
import top.lolosia.installer.ui.component.BaseContainer
import top.lolosia.installer.ui.component.IComponent
import top.lolosia.installer.ui.layout.BaseLayout
import top.lolosia.installer.ui.layout.createLayouts
import top.lolosia.installer.ui.view.IRouterView
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainWindow private constructor(
    val window: Window,
    val thread: CloseableCoroutineDispatcher,
    private val mContainer: BaseContainer.VMode
) : IComponent<VBox> by mContainer {

    constructor(
        window: Window,
        thread: CloseableCoroutineDispatcher
    ) : this(window, thread, BaseContainer.VMode())

    companion object {
        suspend fun create(): MainWindow = createMainWindow()
    }

    val components = mutableMapOf<String, Any>()

    init {
        window.add(mContainer.container)
    }

    var activePage: IRouterView<*> = IRouterView.Empty
        set(value) {
            field.parent = null
            mContainer.clear()
            field = value
            val la = layouts[value.layout] ?: layouts[BaseLayout::class]!!
            la.view = value
            mContainer.add(la as IComponent<*>)
            value.parent = this
        }

    private val layouts = createLayouts().associateBy { it::class }

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponent(id: String): T = components[id] as T

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(id: String): T = components[id] as T

    fun setComponent(id: String, component: Any) = components.set(id, component)
    operator fun set(id: String, component: Any) = components.set(id, component)
}

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
private suspend fun createMainWindow(): MainWindow {
    val mainThread = newSingleThreadContext("main")
    val window = suspendCoroutine { continuation ->
        CoroutineScope(mainThread).launch {
            appWindow(title = "VRC Bitmap Led", width = 460, height = 240) {
                continuation.resume(this)
            }
        }
    }
    return MainWindow(window, mainThread)
}