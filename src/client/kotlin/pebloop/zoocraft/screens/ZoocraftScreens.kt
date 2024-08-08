package pebloop.zoocraft.screens

import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.gui.screen.ingame.HandledScreens.Provider
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import pebloop.zoocraft.screenHandlers.ZoocraftScreenHandler

class ZoocraftScreens {
    companion object {

        fun init() {
            HandledScreens.register(ZoocraftScreenHandler.ENCLOSURE_CONTROLLER_SCREEN_HANDLER, ::EnclosureControllerScreen)
        }
    }
}