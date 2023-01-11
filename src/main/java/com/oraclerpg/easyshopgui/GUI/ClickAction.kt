package com.oraclerpg.easyshopgui.GUI

import com.oraclerpg.easyshopgui.EasyShopGUI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.event.inventory.InventoryClickEvent

interface ClickAction {

    fun execute(e: InventoryClickEvent) {

    }
}