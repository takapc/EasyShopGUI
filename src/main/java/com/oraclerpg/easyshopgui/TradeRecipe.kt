package com.oraclerpg.easyshopgui

import org.bukkit.inventory.ItemStack

class TradeRecipe {
    private val sellings: MutableList<ItemStack> = ArrayList()
    private val requires: MutableList<ItemStack> = ArrayList()

    fun getSellings(): MutableList<ItemStack> {
        return sellings
    }

    fun addSellings(op: ItemStack): TradeRecipe {
        sellings.add(op)
        return this
    }

    fun getRequires(): MutableList<ItemStack> {
        return requires
    }

    fun addRequires(op: ItemStack): TradeRecipe {
        requires.add(op)
        return this
    }
}