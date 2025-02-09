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

import top.lolosia.vrc.led.boot.LedClassLoader
import top.lolosia.vrc.led.boot.wrapper.ITweaker
import top.lolosia.vrc.led.boot.wrapper.LaunchClassLoader
import org.slf4j.LoggerFactory
import org.spongepowered.asm.launch.GlobalProperties
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.launch.platform.CommandLineOptions
import org.spongepowered.asm.mixin.Mixins
import java.io.File
import java.util.jar.JarFile

/**
 * TweakClass for running mixins in production. Being a tweaker ensures that we
 * get injected into the AppClassLoader but does mean that we will need to
 * inject the FML coremod by hand if running under FML.
 */
class MixinTweaker : ITweaker {

    val logger = LoggerFactory.getLogger(MixinTweaker::class.java)
    val plt = LedMixinPlatformManager()
    /**
     * Hello world
     */
    init {
        GlobalProperties.put(GlobalProperties.Keys.PLATFORM_MANAGER, plt)

        val start = MixinBootstrap::class.java.getDeclaredMethod("start")
        start.isAccessible = true
        start.invoke(null)
    }

    override fun acceptOptions(args: List<String>, gameDir: File, assetsDir: File, profile: String) {
        plt.init()
        val doInit = MixinBootstrap::class.java.getDeclaredMethod("doInit", CommandLineOptions::class.java)
        doInit.isAccessible = true
        doInit.invoke(null, CommandLineOptions.ofArgs(args))
    }

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        val inject = MixinBootstrap::class.java.getDeclaredMethod("inject")
        inject.isAccessible = true
        inject.invoke(null)
        classLoader as LedClassLoader
        val configs = mutableListOf<String>();
        classLoader.pluginRegister.values.forEach { url ->
            JarFile(File(url.toURI())).use { jar ->
                configs += jar.entries()
                    .asSequence()
                    .filter { it.name.endsWith(".mixin.json") }
                    .map { it.name }
            }
        }
        logger.info("找到Mixin配置${configs}")
        Mixins.addConfigurations(configs.toTypedArray(), null)
    }

    override fun getLaunchTarget(): String {
        return MixinBootstrap.getPlatform().launchTarget
    }

    override fun getLaunchArguments(): Array<String> {
        return arrayOf()
    }
}
