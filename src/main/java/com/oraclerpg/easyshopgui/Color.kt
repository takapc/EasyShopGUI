package com.oraclerpg.easyshopgui

import org.bukkit.ChatColor

fun String.colored() : String = ChatColor.translateAlternateColorCodes('&', this)