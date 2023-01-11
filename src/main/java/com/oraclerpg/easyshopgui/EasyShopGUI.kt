package com.oraclerpg.easyshopgui

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.Plugin

class EasyShopGUI : JavaPlugin() {

    companion object {
        lateinit var instance: EasyShopGUI
    }
    init {
        instance = this
    }

    override fun onEnable() {
        logger.info("Plugin is enabled!")
        getCommand("create")?.setExecutor(CreateNpcCommand(this))
        server.pluginManager.registerEvents(NpcEventListener(this), this)
        saveConfig()
    }
    override fun onDisable() {
    }
}