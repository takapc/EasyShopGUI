package com.oraclerpg.easyshopgui.GUI

import com.oraclerpg.easyshopgui.EasyShopGUI
import com.oraclerpg.easyshopgui.colored
import net.citizensnpcs.api.npc.NPC
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class TradeGUI(private val name: String) {

    fun getMenu(npc: NPC) : CustomHolder {
        val stack = CustomItem().name(" ").type(Material.GRAY_STAINED_GLASS_PANE).getItemStack()

        val broken = Icon(stack).apply {
            addClickAction( object : ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                }
            })
        }
        val holder = CustomHolder(54, name).apply {
            for (i in 0..8) setIcon(i, broken)
            for (i in 45..53) setIcon(i, broken)
            setCanPutItem(false)
            var viewList = ArrayList<ItemStack>()
            var sellingList = ArrayList<ArrayList<ItemStack>>()
            var buyingList = ArrayList<ArrayList<ItemStack>>()
            var selling: ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>> = EasyShopGUI.instance.config.getList("${npc.uniqueId}selling") as ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>>
            var buying: ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>> = EasyShopGUI.instance.config.getList("${npc.uniqueId}buying") as ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>>
            for (i: HashMap<String, ArrayList<ItemStack>> in selling) {
                for ((j, e) in i.values.withIndex()){
                    sellingList.add(e)
                    viewList.add(e[0])
                }
            }
            for (i: HashMap<String, ArrayList<ItemStack>> in buying) {
                for ((j, e) in i.values.withIndex()){
                    buyingList.add(e)
                }
            }
            for (i in 9..8 + viewList.size) {
                Bukkit.getLogger().info(sellingList[i-9].toString())
                Bukkit.getLogger().info(viewList[i-9].toString())
                var viewItem = ItemStack(viewList[i-9])
                var viewMeta = viewItem.itemMeta
                var lore = ArrayList<String>()
                lore.add("&b&l==&e&l販売アイテム&b&l==".colored())
                for ( t in sellingList[i-9]) {
                    var name = t.itemMeta.displayName
                    if (name.isEmpty()) name = t.type.name
                    lore.add("&f・$name &bx${t.amount}".colored())
                }
                lore.add("&c&l==&e&l必要アイテム&c&l==".colored())
                for ( t in buyingList[i-9]) {
                    var name = t.itemMeta.displayName
                    if (name.isEmpty()) name = t.type.name
                    lore.add("&f・$name &bx${t.amount}".colored())
                }
                viewMeta.lore = lore
                viewItem.setItemMeta(viewMeta)
                val icon = Icon(viewItem)
                Bukkit.getLogger().info("GUI起動時"+sellingList[i-9].toString())
                icon.addClickAction( object : ClickAction {
                    override fun execute(e: InventoryClickEvent) {
                        val player = e.whoClicked as Player
                        Bukkit.getLogger().info("購入判定前"+sellingList[i-9].toString())
                        for (buy in buyingList[i - 9]) {
                            if (!player.inventory.containsAtLeast(buy, buy.amount)) {
                                player.sendMessage("&c十分な素材を持っていません!".colored())
                                return
                            }
                        }
                        Bukkit.getLogger().info(sellingList[i-9].toString())
                        for (item in sellingList[i-9]) {
                            player.inventory.addItem(item)
                        }
                    }
                })
                setIcon(i, icon)
            }
        }
        return holder
    }
}