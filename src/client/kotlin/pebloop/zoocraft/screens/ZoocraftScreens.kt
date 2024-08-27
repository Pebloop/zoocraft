package pebloop.zoocraft.screens

import net.minecraft.client.gui.screen.ingame.HandledScreens
import pebloop.zoocraft.screenHandlers.ZoocraftScreenHandler

class ZoocraftScreens {
    companion object {

        fun init() {
            HandledScreens.register(ZoocraftScreenHandler.ENCLOSURE_CONTROLLER_SCREEN_HANDLER, ::EnclosureControllerScreen)
            HandledScreens.register(ZoocraftScreenHandler.SET_NAME_SCREEN_HANDLER, ::SetNameScreen)
            HandledScreens.register(ZoocraftScreenHandler.ZOODEX_SCREEN_HANDLER, ::ZoodexScreen)
            HandledScreens.register(ZoocraftScreenHandler.ZOODEX_ENTITY_SCREEN_HANDLER, ::ZoodexEntityScreen)
        }
    }
}