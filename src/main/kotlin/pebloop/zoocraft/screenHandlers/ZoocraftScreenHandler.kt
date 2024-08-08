package pebloop.zoocraft.screenHandlers

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType

class ZoocraftScreenHandler {
    companion object {

        val ENCLOSURE_CONTROLLER_SCREEN_HANDLER = ScreenHandlerType<EnclosureControllerScreenHandler>(::EnclosureControllerScreenHandler, FeatureFlags.VANILLA_FEATURES)

        fun init() {
            Registry.register(Registries.SCREEN_HANDLER, "zoocraft:enclosure_controller", ENCLOSURE_CONTROLLER_SCREEN_HANDLER)
        }
    }
}