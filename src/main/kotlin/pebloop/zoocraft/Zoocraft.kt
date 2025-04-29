package pebloop.zoocraft

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory
import pebloop.zoocraft.blocks.ZoocraftBlocks

object Zoocraft : ModInitializer {
    public val LOGGER = LoggerFactory.getLogger("zoocraft")
	public val MOD_ID = "zoocraft"

	override fun onInitialize() {
		LOGGER.info("Zoocraft is initializing!")
		ZoocraftItemGroups.initialize()
		LOGGER.info("Zoocraft item groups have been initialized!")
		ZoocraftBlocks.initialize()
		LOGGER.info("Zoocraft blocks have been initialized!")
		LOGGER.info("Zoocraft has been initialized!")
	}
}