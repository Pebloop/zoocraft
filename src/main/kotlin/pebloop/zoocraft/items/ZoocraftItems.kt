package pebloop.zoocraft.items

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import pebloop.zoocraft.blocks.ZoocraftBlocks

class ZoocraftItems {

    companion object {
        fun register(instance: Item, path: String): Item {
            return Registry.register(Registries.ITEM, Identifier.of("zoocraft", path), instance)
        }

        val TRAPPED_RABBIT = register(TrappedRabbitItem(Item.Settings()), "trapped_rabbit")
        val ZOODEX = register(ZoodexItem(Item.Settings()), "zoodex")

        fun init() {
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register { group ->
                group.add(ZoocraftItems.TRAPPED_RABBIT)
                group.add(ZoocraftItems.ZOODEX)
            }
        }
    }
}