package pebloop.zoocraft.screenHandlers

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.math.BlockPos

class ZoocraftScreenHandler {
    companion object {

        val ENCLOSURE_CONTROLLER_SCREEN_HANDLER = ExtendedScreenHandlerType<EnclosureControllerScreenHandler, EnclosureControllerData>(::EnclosureControllerScreenHandler, EnclosureControllerScreenHandler.PACKET_CODEC)

        fun init() {
            Registry.register(Registries.SCREEN_HANDLER, "zoocraft:enclosure_controller", ENCLOSURE_CONTROLLER_SCREEN_HANDLER)
        }
    }
}