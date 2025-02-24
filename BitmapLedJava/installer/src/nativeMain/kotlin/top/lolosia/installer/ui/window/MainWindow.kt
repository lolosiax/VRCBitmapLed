package top.lolosia.installer.ui.window

import kotlinx.coroutines.delay
import libui.ktx.VBox
import libui.ktx.Window
import libui.ktx.appWindow
import top.lolosia.installer.ui.component.BaseContainer
import top.lolosia.installer.ui.component.IComponent
import top.lolosia.installer.ui.layout.BaseLayout
import top.lolosia.installer.ui.layout.createLayouts
import top.lolosia.installer.ui.view.IRouterView
import top.lolosia.installer.util.threading.Thread
import top.lolosia.installer.util.threading.staThread
import top.lolosia.installer.ui
import top.lolosia.installer.withUI
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainWindow private constructor(
    val window: Window,
    val thread: Thread,
    private val mContainer: BaseContainer.VMode
) : IComponent<VBox> by mContainer {

    constructor(
        window: Window,
        thread: Thread
    ) : this(window, thread, BaseContainer.VMode())

    companion object {
        suspend fun create(): MainWindow = createMainWindow()
    }

    val components = mutableMapOf<String, Any>()

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

    init {
        window.add(mContainer.container)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponent(id: String): T = components[id] as T

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(id: String): T = components[id] as T

    fun setComponent(id: String, component: Any) = components.set(id, component)
    operator fun set(id: String, component: Any) = components.set(id, component)
}

private suspend fun createMainWindow(): MainWindow {
    var mainThread: Thread? = null
    val window = suspendCoroutine { continuation ->
        mainThread = staThread("main") {
            appWindow(title = "VRC Bitmap Led", width = 460, height = 240) {
                continuation.resume(this)
            }
            println("UI Thread exited!")
        }
    }

    delay(200)
    val mainWindow = withUI { MainWindow(window, mainThread!!) }
    return mainWindow
}