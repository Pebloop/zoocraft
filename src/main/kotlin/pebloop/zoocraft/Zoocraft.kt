package pebloop.zoocraft

import net.fabricmc.api.ModInitializer
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
	}
}