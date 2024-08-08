package pebloop.zoocraft.screenHandlers

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler

class EnclosureControllerScreenHandler(sincId: Int, inventory: PlayerInventory?) : ScreenHandler(ZoocraftScreenHandler.ENCLOSURE_CONTROLLER_SCREEN_HANDLER, sincId) {
    override fun quickMove(player: PlayerEntity?, slot: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true;
    }
}