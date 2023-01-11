package com.oraclerpg.easyshopgui.GUI

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class CustomHolder(private val size: Int, private val title: String) : InventoryHolder {
    private val icons: MutableMap<Int, Icon> = HashMap()
    private var canPutItem = false
    fun setIcon(position: Int, icon: Icon): CustomHolder {
        icons[position] = icon
        return this
    }

    fun getIcon(position: Int): Icon? {
        return icons[position]
    }

    fun setCanPutItem(boolean: Boolean) : CustomHolder {
        canPutItem = boolean
        return this
    }

    fun getCanPutItem() : Boolean {
        return canPutItem
    }

    override fun getInventory(): Inventory {
        val inventory = Bukkit.createInventory(this, size, title)

        for ((key, value) in icons) {
            inventory.setItem(key, value.itemStack)
        }
        return inventory
    }
}