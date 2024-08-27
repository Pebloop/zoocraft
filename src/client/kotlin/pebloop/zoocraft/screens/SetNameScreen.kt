package pebloop.zoocraft.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import pebloop.zoocraft.owo_screens.SetNameOwoScreen
import pebloop.zoocraft.screenHandlers.SetNameScreenHandler


class SetNameScreen(handler: SetNameScreenHandler?, inventory: PlayerInventory?, title: Text?) : HandledScreen<SetNameScreenHandler>(handler, inventory, title) {

    override fun init() {
        super.init()
        client?.setScreen(SetNameOwoScreen())
    }

    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {

    }

    override fun shouldCloseOnEsc(): Boolean {
        return false
    }

}