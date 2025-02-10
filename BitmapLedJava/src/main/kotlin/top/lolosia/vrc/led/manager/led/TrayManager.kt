package top.lolosia.vrc.led.manager.led

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseEvent.BUTTON1
import java.awt.event.MouseEvent.BUTTON3
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
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

    @Autowired
    lateinit var applicationContext: ApplicationContext

    private val windowManager by lazy {
        applicationContext.getBean<WindowManager>()
    }

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
                        windowManager.showMainWindow()
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

        // 获取屏幕缩放因子
        val config = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .defaultScreenDevice.defaultConfiguration
        val scale = config.defaultTransform.scaleX

        // 获取主显示器尺寸（考虑多显示器）
        val screenBounds = config.bounds
        val screenWidth = screenBounds.width
        val screenHeight = screenBounds.height - 60

        // 创建菜单窗口
        val jWindow = JWindow()
        menuWindow = jWindow
        jWindow.layout = BorderLayout()
        jWindow.background = Color(0, 0, 0, 0) // 透明背景

        // 创建菜单面板
        val menuPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = Color.WHITE
            // background = Color(0, 0, 0, 0)
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
            "主界面" to { windowManager.showMainWindow() },
            "设置" to { openSettings() },
            "退出" to { exitApp() }
        ).forEach { (text, action) ->
            val item = createMenuItem(text, action)
            menuPanel.add(item)
        }

        jWindow.add(menuPanel, BorderLayout.CENTER)
        jWindow.pack()
        jWindow.minimumSize = Dimension(100, 0)

        // 计算自适应位置
        val prefSize = jWindow.preferredSize
        val maxX = (e.xOnScreen / scale).toInt() + prefSize.width
        val maxY = (e.yOnScreen / scale).toInt() + prefSize.height

        // 智能定位算法
        val finalX = when {
            maxX > screenWidth -> (e.xOnScreen / scale - prefSize.width).toInt()
            else -> (e.xOnScreen / scale).toInt()
        }

        val finalY = when {
            maxY > screenHeight -> (e.yOnScreen / scale - prefSize.height).toInt()
            else -> (e.yOnScreen / scale).toInt()
        }

        // 最终边界检查
        jWindow.setLocation(
            finalX.coerceAtLeast(0),
            finalY.coerceAtLeast(0)
        )

        // 保证窗口完全可见
        val adjustedX = finalX.coerceIn(0, screenWidth - prefSize.width)
        val adjustedY = finalY.coerceIn(0, screenHeight - prefSize.height)

        jWindow.setLocation(adjustedX, adjustedY)
        jWindow.isVisible = true
        CoroutineScope(Dispatchers.Default).launch {
            delay(200)
            jWindow.requestFocus()
        }

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



    private fun openSettings() {
        // 打开设置逻辑
    }

    private fun exitApp() {
        // 退出应用逻辑
        exitProcess(0)
    }
}