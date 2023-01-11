package com.oraclerpg.easyshopgui

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CreateNpcCommand(private val plugin: EasyShopGUI) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return true
        val npc = SpawnNPC(sender.location, "Test", sender.name)
        return true
    }
}