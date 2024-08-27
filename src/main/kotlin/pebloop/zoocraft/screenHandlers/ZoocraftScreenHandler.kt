package pebloop.zoocraft.screenHandlers

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureControllerData
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureControllerScreenHandler
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexData
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexEntityScreenHandler
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexScreenHandler
import java.util.*

class ZoocraftScreenHandler {
    companion object {

        val ENCLOSURE_CONTROLLER_SCREEN_HANDLER = ExtendedScreenHandlerType<EnclosureControllerScreenHandler, EnclosureControllerData>(::EnclosureControllerScreenHandler, EnclosureControllerScreenHandler.PACKET_CODEC)
        val SET_NAME_SCREEN_HANDLER = ExtendedScreenHandlerType<SetNameScreenHandler, UUID>(::SetNameScreenHandler, SetNameScreenHandler.PACKET_CODEC)
        val ZOODEX_SCREEN_HANDLER = ExtendedScreenHandlerType<ZoodexScreenHandler, ZoodexData>(::ZoodexScreenHandler, ZoodexScreenHandler.PACKET_CODEC)
        val ZOODEX_ENTITY_SCREEN_HANDLER = ExtendedScreenHandlerType<ZoodexEntityScreenHandler, UUID>(::ZoodexEntityScreenHandler, ZoodexEntityScreenHandler.PACKET_CODEC)

        fun init() {
            Registry.register(Registries.SCREEN_HANDLER, "zoocraft:enclosure_controller", ENCLOSURE_CONTROLLER_SCREEN_HANDLER)
            Registry.register(Registries.SCREEN_HANDLER, "zoocraft:set_name", SET_NAME_SCREEN_HANDLER)
            Registry.register(Registries.SCREEN_HANDLER, "zoocraft:zoodex", ZOODEX_SCREEN_HANDLER)
            Registry.register(Registries.SCREEN_HANDLER, "zoocraft:zoodex_entity", ZOODEX_ENTITY_SCREEN_HANDLER)
        }
    }
}