package pebloop.zoocraft.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import pebloop.zoocraft.owo_screens.EnclosureControllerOwoScreen
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureControllerScreenHandler


class EnclosureControllerScreen(handler: EnclosureControllerScreenHandler?, inventory: PlayerInventory?, title: Text?) : HandledScreen<EnclosureControllerScreenHandler>(handler, inventory, title) {

    override fun init() {
        super.init()
        client?.setScreen(EnclosureControllerOwoScreen(handler.data))
    }

    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {

    }

}