package pebloop.zoocraft.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FenceBlock
import net.minecraft.block.ShapeContext
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties.FACING
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView

class EnclosureControllerBlock(settings: Settings?) : Block(settings) {

    companion object {
        val CONTROLLER_STATUS: BooleanProperty = BooleanProperty.of("controller_status")
    }

    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape {
        val facing = state?.get(FACING)
        if (facing == null) {
            return createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        } else {
            return when (facing) {
                // North
                Direction.NORTH -> createCuboidShape(0.0, 6.0, 0.0, 16.0, 16.0, 2.0)
                // East
                Direction.EAST -> createCuboidShape(14.0, 6.0, 0.0, 16.0, 16.0, 16.0)
                // South
                Direction.SOUTH -> createCuboidShape(0.0, 6.0, 14.0, 16.0, 16.0, 16.0)
                // West
                Direction.WEST -> createCuboidShape(0.0, 6.0, 0.0, 2.0, 16.0, 16.0)
                else -> createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
            }
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        return this.defaultState.with(FACING, ctx?.horizontalPlayerFacing)
    }

    override fun rotate(state: BlockState?, rotation: BlockRotation?): BlockState? {
        return state?.with(FACING, state.get(FACING)?.rotateYCounterclockwise())
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(FACING)
        builder?.add(CONTROLLER_STATUS)
    }

    // search for the enclosure
    class EnclosureNode(val pos: BlockPos, val parent: EnclosureNode?) {
        fun contains(pos: BlockPos): Boolean {
            if (this.pos == pos) {
                return true
            }
            if (parent == null) {
                return false
            }
            return parent.contains(pos)
        }
    }

    private fun searchForEnclosure(world: World, pos: BlockPos, nodes: EnclosureNode, target: BlockPos, start: Boolean): EnclosureNode? {
        if (pos == target) {
            return nodes
        }
        if (nodes.contains(pos)) {
            return null
        }
        if (world.getBlockState(pos).block !is FenceBlock) {
            return null
        }

        val northBlock = pos.offset(Direction.NORTH)
        val eastBlock = pos.offset(Direction.EAST)
        val southBlock = pos.offset(Direction.SOUTH)
        val westBlock = pos.offset(Direction.WEST)

        if (!start) {
            val northNode = searchForEnclosure(world, northBlock, EnclosureNode(pos, nodes), target, false)
            if (northNode != null) {
                return northNode
            }
        } else if (northBlock != target) {
            val northNode = searchForEnclosure(world, northBlock, EnclosureNode(pos, nodes), target, false)
            if (northNode != null) {
                return northNode
            }
        }
        if (!start) {
            val eastNode = searchForEnclosure(world, eastBlock, EnclosureNode(pos, nodes), target, false)
            if (eastNode != null) {
                return eastNode
            }
        } else if (eastBlock != target) {
            val eastNode = searchForEnclosure(world, eastBlock, EnclosureNode(pos, nodes), target, false)
            if (eastNode != null) {
                return eastNode
            }
        }

        if (!start) {
            val southNode = searchForEnclosure(world, southBlock, EnclosureNode(pos, nodes), target, false)
            if (southNode != null) {
                return southNode
            }
        } else if (southBlock != target) {
            val southNode = searchForEnclosure(world, southBlock, EnclosureNode(pos, nodes), target, false)
            if (southNode != null) {
                return southNode
            }
        }

        if (!start) {
            val westNode = searchForEnclosure(world, westBlock, EnclosureNode(pos, nodes), target, false)
            if (westNode != null) {
                return westNode
            }
        } else if (westBlock != target) {
            val westNode = searchForEnclosure(world, westBlock, EnclosureNode(pos, nodes), target, false)
            if (westNode != null) {
                return westNode
            }
        }


        return null
    }

    // this block can only be placed on a closed enclosure
    override fun canPlaceAt(state: BlockState?, world: WorldView?, pos: BlockPos?): Boolean {

        val facing = state?.get(FACING) ?: return false

        val supportBlock = pos?.offset(facing)
        val supportBlockType = world?.getBlockState(supportBlock)?.block
        if (supportBlockType !is FenceBlock) {
            return false
        }

        return super.canPlaceAt(state, world, pos)
    }

    private fun getEnclosure(world: World, pos: BlockPos): List<BlockPos>? {
        val northBlock = pos.offset(Direction.NORTH)
        val eastBlock = pos.offset(Direction.EAST)
        val southBlock = pos.offset(Direction.SOUTH)
        val westBlock = pos.offset(Direction.WEST)

        val northNode = searchForEnclosure(world, northBlock, EnclosureNode(pos, null), pos, true)
        val eastNode = searchForEnclosure(world, eastBlock, EnclosureNode(pos, null), pos, true)
        val southNode = searchForEnclosure(world, southBlock, EnclosureNode(pos, null), pos, true)
        val westNode = searchForEnclosure(world, westBlock, EnclosureNode(pos, null), pos, true)
        val enclosureNode = northNode ?: eastNode ?: southNode ?: westNode

        if (enclosureNode != null) {
            var currentNode = enclosureNode
            val enclosureList = mutableListOf<BlockPos>()
            while (currentNode?.parent != null) {
                currentNode = currentNode.parent!!
                enclosureList.add(currentNode.pos)
            }
            return enclosureList
        } else {
            return null
        }
    }

    override fun onPlaced(world: World?, pos: BlockPos?, state: BlockState?, placer: LivingEntity?, itemStack: ItemStack?) {
        super.onPlaced(world, pos, state, placer, itemStack)

        val facing = state?.get(FACING)
        val supportBlock = pos?.offset(facing)

        val enclosure = getEnclosure(world!!, supportBlock!!)
        if (enclosure != null) {
            world.setBlockState(pos, state?.with(CONTROLLER_STATUS, true), 3)
        } else {
            world.setBlockState(pos, state?.with(CONTROLLER_STATUS, false), 3)
        }
    }

}
