package pebloop.zoocraft

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory
import pebloop.zoocraft.blocks.ZoocraftBlocks

object Zoocraft : ModInitializer {
    private val logger = LoggerFactory.getLogger("zoocraft")

	override fun onInitialize() {
		ZoocraftBlocks.init()
	}
}