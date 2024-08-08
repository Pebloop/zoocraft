package pebloop.zoocraft

import net.fabricmc.api.ClientModInitializer
import pebloop.zoocraft.screens.ZoocraftScreens

object ZoocraftClient : ClientModInitializer {
	override fun onInitializeClient() {
		ZoocraftScreens.init()
	}
}