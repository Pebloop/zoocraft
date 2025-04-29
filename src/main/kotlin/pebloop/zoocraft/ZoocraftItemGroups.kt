package pebloop.zoocraft

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import pebloop.zoocraft.blocks.ZoocraftBlocks

class ZoocraftItemGroups {

    companion object {

        public val ZOOCRAFT_ITEM_GROUP_KEY: RegistryKey<ItemGroup> = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(),
            Identifier.of(Zoocraft.MOD_ID, "zoocraft_item_group")
        )
        public val ZOOCRAFT_ITEM_GROUP: ItemGroup = FabricItemGroup.builder()
            .icon { ItemStack(ZoocraftBlocks.ENCLOSURE_CONTROLLER.asItem()) }
            .displayName(Text.translatable("itemGroup.zoocraft"))
            .build()

        fun initialize() {
            Registry.register(Registries.ITEM_GROUP, ZOOCRAFT_ITEM_GROUP_KEY, ZOOCRAFT_ITEM_GROUP)
        }
    }
}