package pebloop.zoocraft.blocks

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import pebloop.zoocraft.ZoocraftItemGroups

class ZoocraftBlocks {

    companion object {

        val ENCLOSURE_CONTROLLER: Block = register(
            name = "enclosure_controller",
            blockFactory = { settings -> EnclosureControllerBlock(settings) },
            settings = AbstractBlock.Settings.create().sounds(BlockSoundGroup.AMETHYST_BLOCK),
            shouldRegisterItem = true
        )

        fun initialize() {
            ItemGroupEvents.modifyEntriesEvent(ZoocraftItemGroups.ZOOCRAFT_ITEM_GROUP_KEY).register { event ->
                event.add(ENCLOSURE_CONTROLLER.asItem())
            }
        }

        private fun register(name: String, blockFactory: (settings: AbstractBlock.Settings) -> Block, settings: AbstractBlock.Settings, shouldRegisterItem: Boolean = true): Block {
            val blockKey: RegistryKey<Block> = keyOfBlock(name)
            val block: Block = blockFactory(settings.registryKey(blockKey))

            if (shouldRegisterItem) {
                val itemKey: RegistryKey<Item> = keyOfItem(name)
                val item: BlockItem = BlockItem(block, Item.Settings().registryKey(itemKey))
                Registry.register(Registries.ITEM, itemKey, item)
            }

            return Registry.register(Registries.BLOCK, blockKey, block)
        }

        private fun keyOfBlock(name: String): RegistryKey<Block> {
            return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("zoocraft", name))
        }

        private fun keyOfItem(name: String): RegistryKey<Item> {
            return RegistryKey.of(RegistryKeys.ITEM, Identifier.of("zoocraft", name))
        }

    }
}