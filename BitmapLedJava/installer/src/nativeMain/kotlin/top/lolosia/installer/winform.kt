package top.lolosia.installer

import kotlinx.cinterop.*
import platform.windows.*

@OptIn(ExperimentalForeignApi::class)
fun WndProc(hwnd: HWND?, msg: UINT, wParam: WPARAM, lParam: LPARAM) : LRESULT
{
    // This switch block differentiates between the message type that could have been received. If you want to
    // handle a specific type of message in your application, just define it in this block.
    when(msg)
    {
        // This message type is used by the OS to close a window. Just closes the window using DestroyWindow(hwnd);
        WM_CLOSE.toUInt() -> DestroyWindow(hwnd)

        // This message type is part of the WM_CLOSE case. After the DestroyWindow(hwnd) function is called, a
        // WM_DESTROY message is sent to the window, which actually closes it.
        WM_DESTROY.toUInt() -> PostQuitMessage(0)

        // This message type is an important one for GUI programming. It symbolizes an event for a button for example.
        WM_COMMAND.toUInt() ->
        {
            // To differentiate between controls, compare the HWND of, for example, the button to the HWND that is passed
            // into the LPARAM parameter. This way you can establish control-specific actions.
            //if (lParam == button.objcPtr().toLong() && (wParam == BN_CLICKED.toULong()))
            //{
            //    // The button was clicked, this is your proof.
            //    MessageBoxA(hwnd, "Button is pressed!", "test", MB_ICONINFORMATION);
            //}
        }

        // When no message type is handled in your application, return the default window procedure. In this case the message
        // will be handled elsewhere or not handled at all.
        else -> return (DefWindowProc!!)(hwnd, msg, wParam, lParam)
    }

    return 0
}

@OptIn(ExperimentalForeignApi::class)
fun winForm() {

    println("Hello, Kotlin/Native!")


    memScoped {

        val hInstance = (GetModuleHandle!!)(null)
        val lpszClassName = "GijSoft"

        // 为了能够创建窗口，你需要有一个可用的window类。通过注册一个窗口类，可以为应用程序创建一个窗口类。
        // 下面的结构体声明和填充提供了新window类的详细信息。
        val wc = alloc<WNDCLASSEX>()

        wc.cbSize        = sizeOf<WNDCLASSEX>().toUInt()
        wc.style         = 0u
        wc.lpfnWndProc   = staticCFunction(::WndProc)
        wc.cbClsExtra    = 0
        wc.cbWndExtra    = 0
        wc.hInstance     = hInstance
        wc.hIcon         = null
        wc.hCursor       = (LoadCursor!!)(hInstance, IDC_ARROW)
        wc.lpszMenuName  = null
        wc.lpszClassName = lpszClassName.wcstr.ptr
        wc.hIconSm       = null
        wc.hbrBackground = CreateSolidBrush(0xFFFFFFU)

        // 这个函数实际上注册了Window类。如果在`wc`结构体中指定的信息是正确的，则应该创建Window类并且没有错误返回。
        if((RegisterClassEx!!)(wc.ptr) == 0u.toUShort())
        {
            println("Failed to register!")
            return
        }

        // 这个函数创建第一个窗口。
        // 它使用在第一部分中注册的window类，并接受标题、样式和位置/大小参数。
        // 有关特定于样式的定义的更多信息，请参阅MSDN，其中提供了扩展文档。
        val hwnd = CreateWindowExA(WS_EX_CLIENTEDGE.toUInt(), lpszClassName, "Win32 C Window application by evolution536",
            (WS_OVERLAPPED or WS_CAPTION or WS_SYSMENU or WS_MINIMIZEBOX).toUInt(),
            CW_USEDEFAULT, CW_USEDEFAULT, 320, 125, null, null, hInstance, NULL
        )

        // 一切正常，显示窗口包括所有控件。
        ShowWindow(hwnd, 1)
        UpdateWindow(hwnd)


        // 这部分是“消息循环”。这个循环确保应用程序持续运行，并使窗口能够在WndProc函数中接收消息。
        // 如果想让它正常运行，就必须在GUI应用程序中包含这段代码。
        val Msg = alloc<MSG>()
        while((GetMessage!!)(Msg.ptr, null, 0u, 0u) > 0)
        {
            TranslateMessage(Msg.ptr)
            (DispatchMessage!!)(Msg.ptr)
        }
    }

}