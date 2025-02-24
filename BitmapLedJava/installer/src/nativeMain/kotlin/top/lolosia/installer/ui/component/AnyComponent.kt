package top.lolosia.installer.ui.component

import libui.ktx.Control

class AnyComponent<T : Control<*>>(override val container: T) : IComponent<T> {
    override var parent: IComponent<*>? = null
}