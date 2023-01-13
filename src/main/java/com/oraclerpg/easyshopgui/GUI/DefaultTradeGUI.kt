package com.oraclerpg.easyshopgui.GUI

import com.oraclerpg.easyshopgui.EasyShopGUI
import com.oraclerpg.easyshopgui.colored
import net.citizensnpcs.api.npc.NPC
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.lang.NullPointerException

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
            val selling = EasyShopGUI.instance.config.getList("${npc.uniqueId}selling") as ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>>
            val buying = EasyShopGUI.instance.config.getList("${npc.uniqueId}buying") as ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>>
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
                var viewItem = ItemStack(viewList[i-9])
                var viewMeta = viewItem.itemMeta
                viewMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                viewMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
                viewMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                var lore = ArrayList<String>()
                lore.add("&b&l==&e&lSales Item&b&l==".colored())
                for ( t in sellingList[i-9]) {
                    var name = t.itemMeta.displayName
                    if (name.isEmpty()) name = t.type.name
                    lore.add("&f・$name &bx${t.amount}".colored())
                }
                lore.add("&c&l==&e&lRequire Item&c&l==".colored())
                for ( t in buyingList[i-9]) {
                    var name = t.itemMeta.displayName
                    if (name.isEmpty()) name = t.type.name
                    lore.add("&f・$name &bx${t.amount}".colored())
                }
                viewMeta.lore = lore
                viewItem.setItemMeta(viewMeta)
                viewMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                viewMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
                viewMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                val icon = Icon(viewItem)
                icon.addClickAction( object : ClickAction {
                    override fun execute(e: InventoryClickEvent) {
                        val player = e.whoClicked as Player
                        val isFinishedBuying = ArrayList<Boolean>()
                        for (buy in buyingList[i - 9]) {
                            if (!player.inventory.containsAtLeast(buy, buy.amount)) {
                                player.sendMessage("&cYou don't have enough material!".colored())
                                return
                            }
                            isFinishedBuying.add(false)
                        }
                        val playerItems = HashMap<Int, ItemStack>()
                        val buyingListWithIsFinished = HashMap<ItemStack, Boolean>()
                        val nokori = ArrayList<Int>()
                        for ((index,item) in player.inventory.withIndex()) {
                            if (item != null) playerItems[index] = item
                            player.inventory.setItem(index, null)
                        }
                        for (buy in buyingList[i-9]) {
                            buyingListWithIsFinished[buy] = false
                            nokori.add(buy.amount)
                        }
                        var num = 0
                        for (buy in buyingListWithIsFinished) { //要求量を計算して余った分をプレイヤーに返す
                            for (item in playerItems) {
                                if (item.value.isSimilar(buy.key) && !buy.value) {
                                    if (item.value.amount <= nokori[num]) {
                                        nokori[num] -= item.value.amount
                                        item.value.amount = 0
                                    } else {
                                        if (nokori[num] > 0) {
                                            item.value.amount -= nokori[num]
                                            nokori[num] = 0
                                        }
                                    }
                                    player.inventory.setItem(item.key, item.value)
                                    if (nokori[num] <= 0) buyingListWithIsFinished[buy.key] = true
                                } else {
                                    player.inventory.setItem(item.key, item.value)
                                }
                            }
                            num++
                        }
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