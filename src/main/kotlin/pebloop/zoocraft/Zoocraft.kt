package pebloop.zoocraft

import io.wispforest.owo.network.OwoNetChannel
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import pebloop.zoocraft.blockEntities.ZoocraftBlockEntities
import pebloop.zoocraft.blocks.ZoocraftBlocks
import pebloop.zoocraft.ducks.WorldExtended
import pebloop.zoocraft.screenHandlers.ZoocraftScreenHandler
import pebloop.zoocraft.ZoocraftConfig
import pebloop.zoocraft.items.ZoocraftItems
import pebloop.zoocraft.packets.ZoocraftPackets

object Zoocraft : ModInitializer {

	final val channel = OwoNetChannel.create(Identifier.of("zoocraft", "main"))
    private val logger = LoggerFactory.getLogger("zoocraft")
	const val MOD_ID = "zoocraft"
	val CONFIG: ZoocraftConfig = ZoocraftConfig.createAndLoad()
	val DATA: ZoocraftData = ZoocraftData()

	override fun onInitialize() {
		ZoocraftScreenHandler.init()
		ZoocraftBlocks.init()
		ZoocraftBlockEntities.init()
		ZoocraftItems.init()
		ZoocraftPackets.init()

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