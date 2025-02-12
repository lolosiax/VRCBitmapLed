/*
 * Copyright (c) 2025 Lolosia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package top.lolosia.installer.form

import kotlinx.cinterop.*
import platform.gdiplus.GdiplusShutdown
import platform.gdiplus.GdiplusStartup
import platform.gdiplus.GdiplusStartupInput
import platform.gdiplus.GdiplusStartupOutput
import platform.windows.*
import kotlin.system.exitProcess

@OptIn(ExperimentalForeignApi::class)
class MainWindow {
    lateinit var mHWND: HWND

    fun create(): Unit = memScoped {
        val hInstance = (GetModuleHandle!!)(null)
        val lpszClassName = "BitmapLed"

        val handle = alloc<ULONG_PTRVar>()
        val inp = alloc<GdiplusStartupInput>()
        inp.GdiplusVersion = 1U
        val oup = alloc<GdiplusStartupOutput>()
        // Initialize GDI+.
        val gs = GdiplusStartup(handle.ptr, inp.ptr, oup.ptr)
        println("GdiplusStartup: $gs")


        // 为了能够创建窗口，你需要有一个可用的window类。通过注册一个窗口类，可以为应用程序创建一个窗口类。
        // 下面的结构体声明和填充提供了新window类的详细信息。
        val wc = alloc<WNDCLASSEX>()

        wc.cbSize = sizeOf<WNDCLASSEX>().toUInt()
        wc.style = 0u
        wc.lpfnWndProc = staticCFunction(::winProc)
        wc.cbClsExtra = 0
        wc.cbWndExtra = 0
        wc.hInstance = hInstance
        wc.hIcon = null
        wc.hCursor = (LoadCursor!!)(hInstance, IDC_ARROW)
        wc.hbrBackground = CreateSolidBrush(0xFFFFFFU)
        wc.lpszMenuName = null
        wc.lpszClassName = lpszClassName.wcstr.ptr
        wc.hIconSm = null

        // 这个函数实际上注册了Window类。如果在`wc`结构体中指定的信息是正确的，则应该创建Window类并且没有错误返回。
        if ((RegisterClassEx!!)(wc.ptr) == 0u.toUShort()) {
            println("Failed to register!")
            return
        }

        // 这个函数创建第一个窗口。
        // 它使用在第一部分中注册的window类，并接受标题、样式和位置/大小参数。
        // 有关特定于样式的定义的更多信息，请参阅MSDN，其中提供了扩展文档。
        val hwnd = CreateWindowExA(
            WS_EX_CLIENTEDGE.toUInt(), lpszClassName, "Win32 C Window application by evolution536",
            (WS_OVERLAPPED or WS_CAPTION or WS_SYSMENU or WS_MINIMIZEBOX).toUInt(),
            CW_USEDEFAULT, CW_USEDEFAULT, 800, 350, null, null, hInstance, NULL
        )

        mHWND = hwnd!!

        // 一切正常，显示窗口包括所有控件。
        ShowWindow(hwnd, 1)
        UpdateWindow(hwnd)

        // 这部分是“消息循环”。这个循环确保应用程序持续运行，并使窗口能够在WndProc函数中接收消息。
        // 如果想让它正常运行，就必须在GUI应用程序中包含这段代码。
        val msg = alloc<MSG>()
        while ((GetMessage!!)(msg.ptr, null, 0u, 0u) > 0) {
            TranslateMessage(msg.ptr)
            (DispatchMessage!!)(msg.ptr)
        }

        GdiplusShutdown(handle.value)
        exitProcess(msg.wParam.toInt())
    }
}