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

import top.lolosia.vrc.led.boot.wrapper.IClassTransformer
import org.spongepowered.asm.service.ILegacyClassTransformer
import org.spongepowered.asm.service.MixinService

/**
 * Proxy transformer for the mixin transformer. These transformers are used
 * to allow the mixin transformer to be re-registered in the transformer
 * chain at a later stage in startup without having to fully re-initialise
 * the mixin transformer itself. Only the latest proxy to be instantiated
 * will actually provide callbacks to the underlying mixin transformer.
 */
class Proxy : IClassTransformer, ILegacyClassTransformer {
    /**
     * True if this is the active proxy, newer proxies disable their older
     * siblings
     */
    private var isActive = true

    init {
        for (proxy in proxies) {
            proxy.isActive = false
        }
        proxies.add(this)
        MixinService.getService().getLogger("mixin").debug("Adding new mixin transformer proxy #{}", proxies.size)
    }

    override fun transform(name: String, transformedName: String, basicClass: ByteArray): ByteArray {
        return if (isActive) {
            transformClassBytes.invoke(transformer, name, transformedName, basicClass) as ByteArray
            // transformer.transformClassBytes(
            //     name,
            //     transformedName,
            //     basicClass
            // )
        } else basicClass
    }

    override fun getName(): String {
        return this.javaClass.getName()
    }

    override fun isDelegationExcluded(): Boolean {
        return true
    }

    override fun transformClassBytes(name: String, transformedName: String, basicClass: ByteArray): ByteArray {
        return if (isActive) {
            transformClassBytes.invoke(transformer, name, transformedName, basicClass) as ByteArray
            // transformer.transformClassBytes(
            //     name,
            //     transformedName,
            //     basicClass
            // )
        } else basicClass
    }

    companion object {
        /**
         * All existing proxies
         */
        private val proxies: MutableList<Proxy> = ArrayList()

        private val transformerClass by lazy {
            Class.forName("org.spongepowered.asm.mixin.transformer.MixinTransformer")
        }

        /**
         * Actual mixin transformer instance
         */
        private val transformer : Any by lazy {
            val constructor = transformerClass.getDeclaredConstructor()
            constructor.isAccessible = true
            constructor.newInstance()
        }

        private val transformClassBytes by lazy {
            val method = transformerClass.getDeclaredMethod(
                "transformClassBytes",
                String::class.java,
                String::class.java,
                ByteArray::class.java
            )
            method.isAccessible = true
            method
        }
    }
}
