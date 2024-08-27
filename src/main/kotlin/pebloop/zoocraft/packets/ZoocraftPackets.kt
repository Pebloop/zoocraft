package pebloop.zoocraft.packets

import io.wispforest.owo.network.ServerAccess
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.text.Text
import net.minecraft.util.TypeFilter
import net.minecraft.util.math.Box
import net.minecraft.world.StructureSpawns.BoundingBox
import pebloop.zoocraft.Zoocraft
import pebloop.zoocraft.screenHandlers.SetNameScreenHandler

class ZoocraftPackets {

    companion object {


        fun init() {
            Zoocraft.channel.registerServerbound(SetNamePacket::class.java) { setNamePacket: SetNamePacket, serverAccess: ServerAccess ->
                val handler = serverAccess.player.currentScreenHandler
                if (handler is SetNameScreenHandler) {
                    val screenHandler = handler as SetNameScreenHandler
                    val entityUuid = screenHandler.getEntityId()
                    val playerPos = serverAccess.player.pos
                    val box = Box.from(playerPos).expand(10.0)
                    val entity = serverAccess.player.world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity::class.java), box) {
                        it.uuid == entityUuid
                    }.firstOrNull()
                    if (entity != null) {
                        entity.customName = Text.of(setNamePacket.name)
                    }
                    serverAccess.player.closeHandledScreen()
                }
            }

        }
    }
}