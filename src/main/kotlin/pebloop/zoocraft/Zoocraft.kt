package pebloop.zoocraft

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer
import org.slf4j.LoggerFactory
import pebloop.zoocraft.blockEntities.ZoocraftBlockEntities
import pebloop.zoocraft.blocks.ZoocraftBlocks
import pebloop.zoocraft.screenHandlers.ZoocraftScreenHandler

object Zoocraft : ModInitializer {
    private val logger = LoggerFactory.getLogger("zoocraft")

	override fun onInitialize() {
		ZoocraftScreenHandler.init()
		ZoocraftBlocks.init()
		ZoocraftBlockEntities.init()

		ServerLifecycleEvents.SERVER_STARTED.register(ServerLifecycleEvents.ServerStarted { server: MinecraftServer ->
			val worldExtended = server.overworld as WorldExtended
			val stateSaverAndLoader = StateSaverAndLoader.getServerState(server)
			worldExtended.`zoocraft$setEnclosureControllers`(stateSaverAndLoader.enclosureControllers)
			println("Loading enclosure controllers")

		})

		ServerLifecycleEvents.BEFORE_SAVE.register(ServerLifecycleEvents.BeforeSave { server: MinecraftServer, flush: Boolean, b1: Boolean ->
			val worldExtended = server.overworld as WorldExtended
			val stateSaverAndLoader = StateSaverAndLoader.getServerState(server)
			stateSaverAndLoader.enclosureControllers = worldExtended.`zoocraft$getEnclosureControllers`()
			println("Saving enclosure controllers")
			stateSaverAndLoader.markDirty()

		})
	}
}