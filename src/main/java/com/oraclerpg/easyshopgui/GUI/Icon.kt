package com.oraclerpg.easyshopgui.GUI

import org.bukkit.inventory.ItemStack


class Icon(val itemStack: ItemStack) {
    private val clickActions: MutableList<ClickAction> = ArrayList()
    fun addClickAction(clickAction: ClickAction): Icon {
        clickActions.add(clickAction)
        return this
    }

    fun getClickActions(): List<ClickAction> {
        return clickActions
    }
}