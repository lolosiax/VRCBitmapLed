package top.lolosia.vrc.led.manager.led

import com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import top.lolosia.vrc.led.config.SConfig
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseEvent.BUTTON1
import java.awt.event.MouseEvent.BUTTON3
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import java.net.URI
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JWindow
import javax.swing.border.EmptyBorder
import kotlin.system.exitProcess

@Service
class TrayManager {

    val tray by lazy { createTray() }
    private var notificationCallback = {}
    private var menuWindow: JWindow? = null

    private val logger = LoggerFactory.getLogger(TrayManager::class.java)

    @PostConstruct
    fun init() {
        try {
            SystemTray.getSystemTray().add(tray)
        } catch (e: Exception) {
            logger.warn("System tray is not supported!", e)
        }
    }

    private fun createTray(): TrayIcon {
        val icon = TrayIcon(createTrayIconImage(), "BitmapLed-VRC点阵屏幕")
        icon.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                when (e.button) {
                    BUTTON1 -> {
                        showMainWindow()
                    }

                    BUTTON3 -> showMenuWindow(e)
                }
            }
        })
        icon.addActionListener {
            notificationCallback()
            notificationCallback = {}
        }
        return icon
    }

    fun notification(
        title: String,
        message: String,
        type: TrayIcon.MessageType = TrayIcon.MessageType.INFO,
        callback: () -> Unit = {}
    ) {
        notificationCallback = callback
        tray.displayMessage(title, message, type)
    }

    private fun createTrayIconImage(): Image {
        // 创建16x16的托盘图标（替换为实际图标）
        val image = BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB)
        val g2d = image.createGraphics()
        g2d.color = Color.WHITE
        g2d.fillOval(0, 0, 16, 16)
        g2d.dispose()
        return image
    }

    private fun showMenuWindow(e: MouseEvent) {
        menuWindow?.dispose()

        // 创建菜单窗口
        val jWindow = JWindow()
        menuWindow = jWindow
        jWindow.layout = BorderLayout()
        jWindow.background = Color(0, 0, 0, 0) // 透明背景

        // 创建菜单面板
        val menuPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = Color.WHITE
            border = EmptyBorder(5, 10, 5, 10)
            addMouseListener(object : MouseAdapter() {
                override fun mouseExited(e: MouseEvent) {
                    if (!contains(e.point)) {
                        disposeWindow()
                    }
                }
            })
        }

        // 添加菜单项
        arrayOf(
            "主界面" to { showMainWindow() },
            "设置" to { openSettings() },
            "退出" to { exitApp() }
        ).forEach { (text, action) ->
            val item = createMenuItem(text, action)
            menuPanel.add(item)
        }

        jWindow.add(menuPanel, BorderLayout.CENTER)
        jWindow.pack()
        jWindow.setLocation(
            e.xOnScreen - 20,
            e.yOnScreen - jWindow.height - 5
        )
        jWindow.isVisible = true

        // 添加窗口焦点监听
        jWindow.addWindowFocusListener(object : WindowAdapter() {
            override fun windowLostFocus(e: WindowEvent?) {
                disposeWindow()
            }
        })
    }

    private fun createMenuItem(text: String, action: () -> Unit): JLabel {
        return JLabel(text).apply {
            font = Font("Microsoft YaHei", Font.PLAIN, 12) // 设置中文字体
            border = EmptyBorder(5, 5, 5, 5)
            foreground = Color(0x333333)
            isOpaque = true

            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    action()
                    disposeWindow()
                }

                override fun mouseEntered(e: MouseEvent?) {
                    background = Color(0xEEEEEE)
                }

                override fun mouseExited(e: MouseEvent?) {
                    background = Color.WHITE
                }
            })
        }
    }

    private fun disposeWindow() {
        menuWindow?.dispose()
        menuWindow = null
    }

    private fun showMainWindow() {
        val url = "http://127.0.0.1:${SConfig.server.port}/"
        // Desktop.getDesktop().browse(URI.create())
        Runtime.getRuntime().exec(arrayOf("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe", "--app=\"${url}\""))
    }

    private fun openSettings() {
        // 打开设置逻辑
    }

    private fun exitApp() {
        // 退出应用逻辑
        exitProcess(0)
    }
}