package com.oraclerpg.easyshopgui

import com.oraclerpg.easyshopgui.GUI.CustomHolder
import com.oraclerpg.easyshopgui.GUI.ManageTradeGUI
import com.oraclerpg.easyshopgui.GUI.TradeGUI
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.lang.NullPointerException

class NpcEventListener(private val plugin: EasyShopGUI) : Listener {

    @EventHandler
    fun onClickEntity(e: PlayerInteractEntityEvent) {
        val entity = e.rightClicked
        if (!entity.hasMetadata("NPC")) return
        if (e.hand != EquipmentSlot.HAND) return
        val npc = CitizensAPI.getNPCRegistry().getNPC(entity) ?: return
        if (!e.player.isSneaking) {
            val gui = TradeGUI(npc.name)
            try {
                val selling = EasyShopGUI.instance.config.getList("${npc.uniqueId}selling") as ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>>
                val buying = EasyShopGUI.instance.config.getList("${npc.uniqueId}buying") as ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>>
            } catch (nullpo: NullPointerException) {
                e.player.sendMessage("&cShopが存在しません!".colored())
                return
            }
            e.player.openInventory(gui.getMenu(npc).inventory)
        } else if (e.player.isSneaking) {
            if (!e.player.hasPermission("easyshopgui.trade")) {
                e.player.sendMessage("&cYou don't have manage permission".colored())
                return
            }
            val gui = ManageTradeGUI(npc, plugin)
            plugin.config.set("Listener", "working")
            e.player.openInventory(gui.getAllManageMenu().inventory)
        }
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.view.topInventory.holder !is CustomHolder) return
        if (e.whoClicked !is Player) return
        val player = e.whoClicked as Player
        val item = e.currentItem
        if (item == null || item.type == Material.AIR) return
        val holder = e.view.topInventory.holder as CustomHolder
        if (!holder.getCanPutItem()) e.isCancelled = true
        val icon = holder.getIcon(e.rawSlot) ?: return
        for (action in icon.getClickActions()) {
            action.execute(e)
        }
    }
}