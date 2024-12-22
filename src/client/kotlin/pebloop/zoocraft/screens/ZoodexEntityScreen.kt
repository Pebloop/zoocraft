package pebloop.zoocraft.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import pebloop.zoocraft.owo_screens.SetNameOwoScreen
import pebloop.zoocraft.owo_screens.ZoodexEntityOwoScreen
import pebloop.zoocraft.owo_screens.ZoodexOwoScreen
import pebloop.zoocraft.screenHandlers.SetNameScreenHandler
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexEntityScreenHandler
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexScreenHandler


class ZoodexEntityScreen(handler: ZoodexEntityScreenHandler?, inventory: PlayerInventory?, title: Text?) : HandledScreen<ZoodexEntityScreenHandler>(handler, inventory, title) {

    override fun init() {
        super.init()
        client?.setScreen(ZoodexEntityOwoScreen(handler))
    }

    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {

    }

}