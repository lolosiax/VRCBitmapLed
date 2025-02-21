package top.lolosia.installer.form

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import libui.ktx.*
import top.lolosia.installer.downloadDependencies
import top.lolosia.installer.runOnUiThread
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

fun libuiKtxMain() = appWindow(
    title = "Hello",
    width = 460,
    height = 240
) {
    val components: MutableList<Pair<Label, ProgressBar>> = mutableListOf()
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
}
