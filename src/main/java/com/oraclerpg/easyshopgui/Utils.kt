package com.oraclerpg.easyshopgui

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class Utils {
    fun getPlugin() : Plugin? {
        return Bukkit.getServer().pluginManager.getPlugin("EasyShopGUI")
    }
}