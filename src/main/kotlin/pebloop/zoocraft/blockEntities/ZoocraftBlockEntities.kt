package pebloop.zoocraft.blockEntities

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import pebloop.zoocraft.blocks.EnclosureControllerBlock
import pebloop.zoocraft.blocks.ZoocraftBlocks

class ZoocraftBlockEntities {

    companion object {
        fun register(name: String, entity: BlockEntityFactory<*>, block: Block): BlockEntityType<*> {
            val id: Identifier = Identifier.of("zoocraft", name)
            return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, BlockEntityType.Builder.create(entity, block).build())
        }

        var ENCLOSURE_CONTROLLER_BLOCK_ENTITY = register("enclosure_controller_entity", ::EnclosureControllerBlockEntity, ZoocraftBlocks.ENCLOSURE_CONTROLLER)

        fun init() {

        }
    }

}