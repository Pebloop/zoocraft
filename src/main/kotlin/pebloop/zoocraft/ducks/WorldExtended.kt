package pebloop.zoocraft.ducks

import net.minecraft.util.math.BlockPos

interface WorldExtended {
    fun `zoocraft$getEnclosureControllers`(): ArrayList<BlockPos>
fun `zoocraft$setEnclosureControllers`(enclosureController: java.util.ArrayList<BlockPos>)


}