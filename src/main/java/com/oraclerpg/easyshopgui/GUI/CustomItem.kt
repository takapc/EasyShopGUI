package com.oraclerpg.easyshopgui.GUI

import com.oraclerpg.easyshopgui.colored
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class CustomItem {

    private var name = ""
    private var stack = ItemStack(Material.AIR)
    private var lore = ArrayList<String>()
    fun getItemStack(): ItemStack {
        var item = stack
        var meta = item.itemMeta
        meta.setDisplayName(name)
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        meta.lore = lore
        item.setItemMeta(meta)
        return item
    }

    fun name(op : String) : CustomItem { return apply { name = op } }
    fun stack(op: ItemStack) : CustomItem { return apply { stack = op }}
    fun type(op: Material) : CustomItem { return apply { stack = ItemStack(op) }}
    fun lore(op : ArrayList<String>) : CustomItem { return  apply { lore = op }}
    fun addLore(op: String) : CustomItem { return apply { lore += op.colored() }}
    fun getName() : String = name
    fun getStack() : ItemStack = stack
    fun getLore() : ArrayList<String> = lore
}