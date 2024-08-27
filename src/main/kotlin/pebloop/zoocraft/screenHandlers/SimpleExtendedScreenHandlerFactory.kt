package pebloop.zoocraft.screenHandlers

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class SimpleExtendedScreenHandlerFactory<T>(private val factory: (Int, PlayerInventory, T) -> ScreenHandler, val data: T) : ExtendedScreenHandlerFactory<T> {
    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return factory(syncId, inv, data)
    }

    override fun getDisplayName(): Text {
        return Text.of("name")
    }

    override fun getScreenOpeningData(player: ServerPlayerEntity?): T {
        return data
    }
}