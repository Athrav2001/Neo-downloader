package ir.neo.util.compose.action

import ir.neo.util.compose.IconSource
import ir.neo.util.compose.StringSource

abstract class AnAction(
    title: StringSource,
    icon: IconSource? = null,
) : MenuItem.SingleItem(
    title = title,
    icon = icon,
) {
    override fun onClick() = actionPerformed()

    abstract fun actionPerformed()
}


