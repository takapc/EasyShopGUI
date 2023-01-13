package com.oraclerpg.easyshopgui.GUI

import com.oraclerpg.easyshopgui.EasyShopGUI
import com.oraclerpg.easyshopgui.colored
import net.citizensnpcs.api.npc.NPC
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class ManageTradeGUI(private val npc: NPC, private var plugin: EasyShopGUI)  {

    fun setPlugin(pl: EasyShopGUI) {
        this.plugin = pl
    }

    fun getAllManageMenu() : CustomHolder {
        val stack = CustomItem().name(" ").type(Material.GRAY_STAINED_GLASS_PANE).getItemStack()

        val broken = Icon(stack).apply {
            addClickAction( object : ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                }
            })
        }

        val plusStack = CustomItem().name("&a&lトレードを追加".colored()).type(Material.DIAMOND_SWORD).getItemStack()
        val plusIcon = Icon(plusStack).apply {
            addClickAction( object : ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                    e.whoClicked.openInventory(getAddSellingItemMenu().inventory)
                }
            })
        }
        val removeStack = CustomItem().name("&c&lトレードを削除".colored()).type(Material.BARRIER).getItemStack()
        val removeIcon = Icon(removeStack).apply {
            addClickAction( object : ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                    e.whoClicked.openInventory(getRemoveTradeMenu().inventory)
                }
            })
        }
        val creditStack = getPlayerHead(Bukkit.getOfflinePlayer("takap_c"))
        val creditMeta = creditStack.itemMeta
        creditMeta.setDisplayName("&b&lEasyShopGUI".colored())
        val lore = ArrayList<String>()
        lore.add("&eversion 0.1".colored())
        lore.add("&eMade by takap_c".colored())
        creditMeta.lore = lore
        creditStack.itemMeta = creditMeta
        val creditIcon = Icon(creditStack).apply {
            addClickAction(object :ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                }
            })
        }

        val holder = CustomHolder(27, npc.name).apply {
            for (i in 0..26) setIcon(i, broken)
            setIcon(11, plusIcon)
            setIcon(13, removeIcon)
            setIcon(15, creditIcon)
        }

        return holder
    }

    fun getAddSellingItemMenu() : CustomHolder {
        val stack = CustomItem().name("").type(Material.LIGHT_BLUE_STAINED_GLASS_PANE).getItemStack()

        val broken = Icon(stack).apply {
            addClickAction( object : ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                }
            })
        }

        val doneStack = CustomItem().name("&a&lDone".colored()).type(Material.EMERALD_BLOCK).getItemStack()
        val done = Icon(doneStack).apply {
            addClickAction( object : ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                    val player = e.whoClicked as Player
                    val items = ArrayList<ItemStack>()
                    val map = HashMap<String, ArrayList<ItemStack>>()
                    val list = ArrayList<HashMap<String, ArrayList<ItemStack>>>()
                    for (i in 9..44) {
                        val item = e.inventory.getItem(i)
                        if (item != null && item.type != Material.AIR) items.add(item)
                    }
                    for (i in items) {
                        player.sendMessage(i.displayName())
                    }
                    val uuid = UUID.randomUUID()
                    if (items.isEmpty()) return
                    map["sell$uuid"] = items
                    list.add(map)
                    val sellingList = EasyShopGUI.instance.config.getList("${npc.uniqueId}selling")
                    player.openInventory(getAddRequiredItemMenu(uuid, list, sellingList).inventory)
                }
            })
        }
        val holder = CustomHolder(54, "取引後のアイテムを選択").apply {
            for (i in 0..8) setIcon(i, broken)
            for (i in 45..53) setIcon(i, broken)
            setIcon(49, done)
            setCanPutItem(true)
        }
        return holder
    }

    fun getAddRequiredItemMenu(uuid: UUID, sellingConfig: ArrayList<HashMap<String, ArrayList<ItemStack>>>, defaultConfig: MutableList<*>?) : CustomHolder {
        val stack = CustomItem().name("").type(Material.RED_STAINED_GLASS_PANE).getItemStack()

        val broken = Icon(stack).apply {
            addClickAction( object : ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                }
            })
        }

        val doneStack = CustomItem().name("&a&lDone".colored()).type(Material.EMERALD_BLOCK).getItemStack()
        val done = Icon(doneStack).apply {
            addClickAction( object : ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                    val player = e.whoClicked as Player
                    val items = ArrayList<ItemStack>()
                    val map = HashMap<String, ArrayList<ItemStack>>()
                    val list = ArrayList<HashMap<String, ArrayList<ItemStack>>>()
                    for (i in 9..44) {
                        val item = e.inventory.getItem(i)
                        if (item != null && item.type != Material.AIR) items.add(item)
                    }
                    for (i in items) {
                        player.sendMessage(i.displayName())
                    }
                    if (items.isEmpty()) return
                    map["buy$uuid"] = items
                    list.add(map)
                    val buyingList = EasyShopGUI.instance.config.getList("${npc.uniqueId}buying")
                    if (buyingList is ArrayList) {
                        EasyShopGUI.instance.config.set("${npc.uniqueId}buying", list + buyingList)
                    } else {
                        EasyShopGUI.instance.config.set("${npc.uniqueId}buying", list)
                    }
                    if (defaultConfig is ArrayList) {
                        EasyShopGUI.instance.config.set("${npc.uniqueId}selling", sellingConfig + defaultConfig)
                    } else {
                        EasyShopGUI.instance.config.set("${npc.uniqueId}selling", sellingConfig)
                    }
                    EasyShopGUI.instance.saveConfig()
                    player.openInventory(getAllManageMenu().inventory)
                }
            })
        }
        val holder = CustomHolder(54, "取引前のアイテムを選択").apply {
            for (i in 0..8) setIcon(i, broken)
            for (i in 45..53) setIcon(i, broken)
            setIcon(49, done)
            setCanPutItem(true)
        }
        return holder
    }

    fun getRemoveTradeMenu() : CustomHolder {
        val stack = CustomItem().name(" ").type(Material.GRAY_STAINED_GLASS_PANE).getItemStack()

        val broken = Icon(stack).apply {
            addClickAction( object : ClickAction {
                override fun execute(e: InventoryClickEvent) {
                    e.isCancelled = true
                }
            })
        }
        val viewList = ArrayList<ItemStack>()
        val sellingList = ArrayList<ArrayList<ItemStack>>()
        val idList = ArrayList<String>()
        //String -> トレードID ArrayList<ItemStack> -> アイテム群
        val list: ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>> = EasyShopGUI.instance.config.getList("${npc.uniqueId}selling") as ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>>
        for (i: Map<String, ArrayList<ItemStack>> in list) { //トレードをforループ
            for (e in i.values) { //トレードのアイテム群をループ
                sellingList.add(e)
                viewList.add(e[0])
            }
            for (key: String in i.keys) {
                idList.add(key.substring(4))
            }
        }
        val icons = ArrayList<Icon>()
        for (i in 9..8 + viewList.size) {
            val viewSelling = ArrayList<ArrayList<ItemStack>>()
            val viewBuying = ArrayList<ArrayList<ItemStack>>()
            val sellings: ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>> = EasyShopGUI.instance.config.getList("${npc.uniqueId}selling") as ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>>
            val buyings: ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>> = EasyShopGUI.instance.config.getList("${npc.uniqueId}buying") as ArrayList<LinkedHashMap<String, ArrayList<ItemStack>>>
            for (t: HashMap<String, ArrayList<ItemStack>> in sellings) {
                for ((j, e) in t.values.withIndex()){
                    viewSelling.add(e)
                    viewList.add(e[0])
                }
            }
            for (t: HashMap<String, ArrayList<ItemStack>> in buyings) {
                for ((j, e) in t.values.withIndex()){
                    viewBuying.add(e)
                }
            }
            val viewItem = CustomItem().name("").stack(viewList[i-9]).addLore("&b&l==&e&l販売アイテム&b&l==")
            viewSelling[i-9].forEach {
                var name = it.itemMeta.displayName
                if (name.isEmpty()) name = it.type.name
                viewItem.addLore("&f・${name} &bx${it.amount}")
            }
            viewItem.addLore("&c&l==&e&l必要アイテム&c&l==")
            viewBuying[i-9].forEach {
                var name = it.itemMeta.displayName
                if (name.isEmpty()) name = it.type.name
                viewItem.addLore("&f・${name} &bx${it.amount}")
            }
            val icon = Icon(viewItem.getItemStack()).apply {
                addClickAction( object : ClickAction {
                    override fun execute(e: InventoryClickEvent) {
                        val player = e.whoClicked as Player
                        e.isCancelled = true
                        val tradeNumber = i - 9
                        val selling = EasyShopGUI.instance.config.getList("${npc.uniqueId}selling") as ArrayList<*>
                        val buying = EasyShopGUI.instance.config.getList("${npc.uniqueId}buying") as ArrayList<*>
                        var dealNum = 0
                        for ((num, trade) in selling.withIndex()) {
                            if (trade is HashMap<*, *>) {
                                if ((trade.keys.toList()[0] as String).substring(4) == idList[tradeNumber]) {
                                    dealNum = num
                                    break
                                }
                            }
                        }
                        selling.removeAt(dealNum)
                        buying.removeAt(dealNum)
                        EasyShopGUI.instance.config.set("${npc.uniqueId}selling", selling)
                        EasyShopGUI.instance.config.set("${npc.uniqueId}buying", buying)
                        EasyShopGUI.instance.saveConfig()
                        player.closeInventory()
                    }
                })
            }
            icons.add(icon)

        }
        val holder = CustomHolder(54, "削除したいトレードを選択").apply {
            for (i in 0..8) setIcon(i, broken)
            for (i in 45..53) setIcon(i, broken)
            setCanPutItem(false)
            for (i in 9..8 + icons.size) setIcon(i, icons[i - 9])
        }
        return holder
    }
    private fun getPlayerHead(player: OfflinePlayer): ItemStack {
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = head.itemMeta
        if(meta is SkullMeta){
            meta.owningPlayer = player
            head.itemMeta = meta
        }
        return head
    }
}