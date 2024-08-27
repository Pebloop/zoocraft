package pebloop.zoocraft.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import pebloop.zoocraft.owo_screens.SetNameOwoScreen
import pebloop.zoocraft.owo_screens.ZoodexOwoScreen
import pebloop.zoocraft.screenHandlers.SetNameScreenHandler
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexScreenHandler


class ZoodexScreen(handler: ZoodexScreenHandler?,val inventory: PlayerInventory?, title: Text?) : HandledScreen<ZoodexScreenHandler>(handler, inventory, title) {

    override fun init() {
        super.init()
        client?.setScreen(inventory?.player?.let { ZoodexOwoScreen(it) })
    }

    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {

    }

    override fun drawForeground(context: DrawContext?, mouseX: Int, mouseY: Int) {

    }

}