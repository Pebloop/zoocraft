package pebloop.zoocraft.blocks

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier


class ZoocraftBlocks {
    companion object {
        fun register(block: Block, name: String, shouldRegisterItem: Boolean = true): Block {
            val id: Identifier = Identifier.of("zoocraft", name)
            if (shouldRegisterItem) {
                val blockItem = BlockItem(block, Item.Settings())
                Registry.register(Registries.ITEM, id, blockItem)
            }
            return Registry.register(Registries.BLOCK, id, block)
        }

        var ENCLOSURE_CONTROLLER = register(EnclosureControllerBlock(Settings.create()), "enclosure_controller")

        fun init() {
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register { group ->
                group.add(ENCLOSURE_CONTROLLER.asItem())
            }
        }
    }
}