package pebloop.zoocraft.entities.goals

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView
import pebloop.zoocraft.blocks.RabbitTrapBlock
import pebloop.zoocraft.enums.RabbitTrapStatus

class RabbitTrapGoal(entity: RabbitEntity): MoveToTargetPosGoal(entity as PathAwareEntity?, 0.7, 16) {
    override fun isTargetPos(world: WorldView?, pos: BlockPos?): Boolean {
        if (world == null || pos == null) {
            return false
        }

        val block = world.getBlockState(pos).block
        if (block is RabbitTrapBlock) {
            if (block.stateManager.getProperty(RabbitTrapBlock.STATUS.name)?.equals(RabbitTrapStatus.BAITED) == true) {
                return true
            }
        }

        return false
    }


}