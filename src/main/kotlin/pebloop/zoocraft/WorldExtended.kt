package pebloop.zoocraft

import net.minecraft.util.math.BlockPos

interface WorldExtended {
    fun `zoocraft$getEnclosureControllers`(): ArrayList<BlockPos>
fun `zoocraft$setEnclosureControllers`(enclosureController: java.util.ArrayList<BlockPos>)


}