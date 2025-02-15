package top.lolosia.installer.form

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import libui.ktx.*

fun libuiKtxMain() = appWindow(
    title = "Hello",
    width = 320,
    height = 240
) {
    vbox {
        lateinit var scroll: TextArea

        button("libui говорит: click me!") {
            action {
                scroll.append(
                    """
                    |Hello, World!  Ciao, mondo!
                    |Привет, мир!  你好，世界！
                    |
                    |""".trimMargin()
                )
            }

            CoroutineScope(Dispatchers.Default).launch {
                for (i in 0 until 10){
                    delay(2000)
                    scroll.append("这是调度器。")
                }
            }
        }
        progressbar {
            value = 50
        }
        scroll = textarea {
            readonly = true
            stretchy = true
        }
    }
}
