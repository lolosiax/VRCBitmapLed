package top.lolosia.installer.ui.window

import kotlinx.coroutines.*
import libui.ktx.*
import top.lolosia.installer.downloadDependencies
import top.lolosia.installer.runOnUiThread
import top.lolosia.installer.ui.component.BaseContainer
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class MainWindow(
    val window: Window,
    val thread: CloseableCoroutineDispatcher,
    val components: MutableMap<String, Any>
) : BaseContainer() {

    companion object {
        suspend fun create(): MainWindow = createMainWindow()
    }

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
    val (window, map) = suspendCoroutine { continuation ->
        CoroutineScope(mainThread).launch {
            runMainWindow { window, map ->
                continuation.resume(window to map)
            }
        }
    }
    return MainWindow(window, mainThread, map)
}

/**
 * 启动主窗口，并运行消息循环
 * @param finish 创建窗口完成后的回调
 */
private fun runMainWindow(finish: (Window, MutableMap<String, Any>) -> Unit) = appWindow(
    title = "VRC Bitmap Led", width = 460, height = 240
) {
    val components: MutableList<Pair<Label, ProgressBar>> = mutableListOf()
    val componentsMap = mutableMapOf<String, Any>()
    vbox {
        val mainStatus = label("正在准备运行环境……")
        val mainProgress = progressbar()
        hbox {
            vbox {
                components += List(5) { i ->
                    label("任务${i + 1}") to progressbar()
                }
            }
            vbox {
                components += List(5) { i ->
                    label("任务${i + 6}") to progressbar()
                }
            }
            components += mainStatus to mainProgress
        }
    }

    fun updateDetail(i: Int, data: Pair<Double, String>) {
        val (label, progress) = components[i]
        runOnUiThread {
            var proc = (data.first * 100).roundToInt()
            proc = max(min(proc, 100), 0)
            var text = "任务${i + 1}：($proc%)${data.second}"
            if (text.length > 30) text = text.substring(0, 30) + "…"
            label.text = text
            progress.value = proc
        }
    }

    fun updateProgress(proc: Int, count: Int) {
        val (label, progress) = components[10]
        runOnUiThread {
            if (proc != count) {
                var proc1 = (100.0 * proc / count).roundToInt()
                proc1 = max(min(proc1, 100), 0)
                val text = "正在下载……($proc1%)"
                label.text = text
                progress.value = proc1
            } else {
                progress.value = 100
                label.text = "下载完成"
            }
        }
    }

    CoroutineScope(Dispatchers.Default).launch {
        val failure = downloadDependencies(::updateDetail, ::updateProgress)
        if (failure.isEmpty()) runOnUiThread {
            MsgBox("提示", "下载完成")
        }
        else runOnUiThread {
            val str = failure.joinToString("\n") { "${it.group}:${it.name}:${it.version}" }
            MsgBoxError("提示", "以下依赖库下载失败：\n$str")
        }
    }

    finish(this, componentsMap)
}
