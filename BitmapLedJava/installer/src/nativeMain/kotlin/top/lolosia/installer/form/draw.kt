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

@file:OptIn(ExperimentalForeignApi::class)

package top.lolosia.installer.form

import kotlinx.cinterop.*
import platform.gdiplus.*
import platform.windows.*

/**
 * draw
 * @author 洛洛希雅Lolosia
 * @since 2025-02-12 19:13
 */


@OptIn(ExperimentalForeignApi::class)
fun winProc(hwnd: HWND?, msg: UINT, wParam: WPARAM, lParam: LPARAM): LRESULT {
    // This switch block differentiates between the message type that could have been received. If you want to
    // handle a specific type of message in your application, just define it in this block.
    when (msg) {
        // This message type is used by the OS to close a window. Just closes the window using DestroyWindow(hwnd);
        WM_CLOSE.toUInt() -> DestroyWindow(hwnd)

        // This message type is part of the WM_CLOSE case. After the DestroyWindow(hwnd) function is called, a
        // WM_DESTROY message is sent to the window, which actually closes it.
        WM_DESTROY.toUInt() -> PostQuitMessage(0)

        // This message type is an important one for GUI programming. It symbolizes an event for a button for example.
        WM_COMMAND.toUInt() -> {
            // To differentiate between controls, compare the HWND of, for example, the button to the HWND that is passed
            // into the LPARAM parameter. This way you can establish control-specific actions.
            //if (lParam == button.objcPtr().toLong() && (wParam == BN_CLICKED.toULong()))
            //{
            //    // The button was clicked, this is your proof.
            //    MessageBoxA(hwnd, "Button is pressed!", "test", MB_ICONINFORMATION);
            //}
        }

        WM_PAINT.toUInt() -> drawWindow(hwnd, msg, wParam, lParam)

        // When no message type is handled in your application, return the default window procedure. In this case the message
        // will be handled elsewhere or not handled at all.
        else -> return (DefWindowProc!!)(hwnd, msg, wParam, lParam)
    }

    return 0
}

@OptIn(ExperimentalForeignApi::class)
private fun drawWindow(hwnd: HWND?, msg: UINT, wParam: WPARAM, lParam: LPARAM) {
    memScoped {
        val ps = alloc<PAINTSTRUCT>()

        val hdc = BeginPaint(hwnd, ps.ptr)!!
        FillRect(hdc, ps.rcPaint.ptr, (COLOR_WINDOW + 1).toLong().toCPointer())
        onDraw(hdc)
        EndPaint(hwnd, ps.ptr)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun onDraw(hdc: HDC) = memScoped {
    val graphics = alloc<COpaquePointerVar>();
    val cfh = GdipCreateFromHDC(hdc, graphics.ptr)


    println("GdipCreateFromHDC: $cfh")

    val brushVar = alloc<COpaquePointerVar>()
    val scf = GdipCreateSolidFill(0xFF00FF.toUInt(), brushVar.ptr)
    println("GdipCreateSolidFill: $scf")

    val fontFamily = alloc<COpaquePointerVar>()
    val cfn = GdipGetGenericFontFamilySansSerif(fontFamily.ptr)
    // val cfn = GdipCreateFontFamilyFromName("Times New Roman".wcstr.ptr, `null`, fontFamily.ptr)
    println("GdipCreateFontFamilyFromName: $cfn")

    val font = alloc<COpaquePointerVar>()
    // val cf = GdipCreateFontFromDC(hdc, font.ptr)
    val cf = GdipCreateFont(fontFamily.ptr, 24F, FontStyleRegular.toInt(), UnitPoint , font.ptr)
    println("GdipCreateFont: $cf")
    val rect = alloc<RectF>()
    rect.X = 10F
    rect.Y = 20F
    rect.Width = 200F
    rect.Height = 30F

    val format = alloc<StringFormatFlagsVar>()
    val sft = GdipCreateStringFormat(StringFormatFlagsNoClip.toInt(), 0U, format.ptr.reinterpret())
    println("GdipCreateStringFormat: $sft")

    val text = "Hello World!"

    val gds = GdipDrawString(graphics.ptr, text.wcstr.ptr, -1, font.ptr, rect.ptr, format.ptr, brushVar.ptr)
    println("GdipDrawString: $gds")


    println("on draw!")
}