package pebloop.zoocraft.screenHandlers

import net.minecraft.block.Block
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class EnclosureBlockData(val pos: BlockPos, val block: Block) {

}

class EnclosureControllerData(val surface: List<EnclosureBlockData>) {

}