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

package top.lolosia.vrc.led

import com.sun.jna.Native
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.Psapi
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.RECT
import com.sun.jna.platform.win32.WinNT.PROCESS_QUERY_INFORMATION
import com.sun.jna.platform.win32.WinNT.PROCESS_VM_READ
import com.sun.jna.ptr.IntByReference
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory


/**
 * WindowTest
 * @author 洛洛希雅Lolosia
 * @since 2025-02-10 19:27
 */
class WindowTest {

    val user32 = User32.INSTANCE
    val kernel32 = Kernel32.INSTANCE
    val psapi = Psapi.INSTANCE
    val logger = LoggerFactory.getLogger(WindowTest::class.java)!!

    @Test
    fun findWindowHWND() {
        val windowText = CharArray(512)
        val processText = CharArray(512)
        var count = 0
        user32.EnumWindows({ hwnd, ptr ->
            user32.GetWindowText(hwnd, windowText, 512)
            val wText: String = Native.toString(windowText)
            val rectangle = RECT()
            val dwId = IntByReference()
            user32.GetWindowRect(hwnd, rectangle)
            user32.GetWindowThreadProcessId(hwnd, dwId)

            var pText = "null"
            val hProcess = kernel32.OpenProcess(PROCESS_QUERY_INFORMATION or PROCESS_VM_READ, false, dwId.value);
            if (hProcess != null) {
                val size = IntByReference(processText.size)
                kernel32.QueryFullProcessImageName(hProcess, 0, processText, size)
                // psapi.GetModuleFileNameExW(hProcess, null, processText, processText.size)
                pText = Native.toString(processText)
                kernel32.CloseHandle(hProcess)
            }

            val visible = User32.INSTANCE.IsWindowVisible(hwnd)

            if (wText.isEmpty() || !(!visible && rectangle.left > -32000)) {
                return@EnumWindows true
            }
            // if ("msedge.exe" in pText){
            println("${rectangle}, Hwnd: ${hwnd.pointer}, Text: $wText\n$pText\n")
            // }

            true
        }, null)
    }
}