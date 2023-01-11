package com.oraclerpg.easyshopgui

import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.trait.Gravity
import net.citizensnpcs.trait.LookClose
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

fun SpawnNPC(loc: Location, npcName: String, skin: String) : NPC {
    val npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "dummy").apply {
        isProtected = true
        data().set(NPC.Metadata.PLAYER_SKIN_UUID, skin)
        data().set(NPC.Metadata.PLAYER_SKIN_USE_LATEST, false)
        addTrait(LookClose::class.java)
        getTrait(LookClose::class.java).lookClose(true)
        addTrait(Gravity::class.java)
        getTrait(Gravity::class.java).gravitate(false)
        name = npcName
    }
    npc.spawn(loc)
    return npc
}