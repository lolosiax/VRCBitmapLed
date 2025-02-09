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

package top.lolosia.vrc.led.boot.mixin

import top.lolosia.vrc.led.boot.wrapper.ITweaker
import top.lolosia.vrc.led.boot.wrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import java.io.File

/**
 * Tweaker used to notify the environment when we transition from preinit to
 * default
 */
class EnvironmentStateTweaker : ITweaker {
    override fun acceptOptions(args: List<String>, gameDir: File, assetsDir: File, profile: String) {}
    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        MixinBootstrap.getPlatform().inject()
    }

    override fun getLaunchTarget(): String {
        return ""
    }

    override fun getLaunchArguments(): Array<String> {
        val start = MixinEnvironment::class.java.getDeclaredMethod("gotoPhase", MixinEnvironment.Phase::class.java)
        start.isAccessible = true
        start.invoke(null, MixinEnvironment.Phase.DEFAULT)
        return emptyArray()
    }
}
