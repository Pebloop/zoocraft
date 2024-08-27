package pebloop.zoocraft.screenHandlers.enclosureController

import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos

class EnclosureBlockData(val pos: BlockPos, val block: Block) {

}

class EnclosureEntityData(val typeName: String) {

}
class EnclosureControllerData(val surface: List<EnclosureBlockData>, val entities: List<EnclosureEntityData>) {

}