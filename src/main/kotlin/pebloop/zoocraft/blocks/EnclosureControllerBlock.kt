package pebloop.zoocraft.blocks

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView

class EnclosureControllerBlock(settings: Settings?) : Block(settings) {
    var enclosures: MutableList<BlockPos>? = null
    var enclosureInside: MutableList<BlockPos>? = null

    init {
        defaultState = stateManager.defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    companion object {
        public val CODEC: MapCodec<EnclosureControllerBlock> = Block.createCodec({settings -> EnclosureControllerBlock(settings)})
    }

    override fun getCodec(): MapCodec<out Block> {
        return CODEC
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>?) {
        builder?.add(Properties.HORIZONTAL_FACING)
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {
        val dir: Direction = state!!.get(Properties.HORIZONTAL_FACING)

        when (dir) {
            Direction.NORTH -> return VoxelShapes.cuboid(0.0, 0.45, 0.0, 1.0, 1.0, 0.1)
            Direction.SOUTH -> return VoxelShapes.cuboid(0.0, 0.45, 0.9, 1.0, 1.0, 1.0)
            Direction.EAST -> return VoxelShapes.cuboid(0.9, 0.45, 0.0, 1.0, 1.0, 1.0)
            Direction.WEST -> return VoxelShapes.cuboid(0.0, 0.45, 0.0, 0.1, 1.0, 1.0)
            else -> return VoxelShapes.fullCube()
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        return super.getPlacementState(ctx)?.with(Properties.HORIZONTAL_FACING, ctx!!.horizontalPlayerFacing)
    }

    /**
     * Enclosure Controller can only be placed on a fence block.
     */
    override fun canPlaceAt(state: BlockState?, world: WorldView?, pos: BlockPos?): Boolean {
        val behind: BlockPos = pos!!.offset(state!!.get(Properties.HORIZONTAL_FACING))
        val behindState: BlockState = world!!.getBlockState(behind)
        return isFence(behindState)
    }

    /**
     * Check if a block is a fence block.
     */
    fun isFence(state: BlockState): Boolean {
        return state.isOf(Blocks.OAK_FENCE) ||
                state.isOf(Blocks.BIRCH_FENCE) ||
                state.isOf(Blocks.SPRUCE_FENCE) ||
                state.isOf(Blocks.JUNGLE_FENCE) ||
                state.isOf(Blocks.ACACIA_FENCE) ||
                state.isOf(Blocks.DARK_OAK_FENCE) ||
                state.isOf(Blocks.CRIMSON_FENCE) ||
                state.isOf(Blocks.WARPED_FENCE)
    }

    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hit: BlockHitResult?
    ): ActionResult? {
        updateEnclosureController(world, pos)
        return ActionResult.SUCCESS
    }

    /**
     * Update the enclosure controller.
     */
    public fun updateEnclosureController(world: World?, pos: BlockPos?) {
        updateEnclosure(world, pos)
        updateEnclosureInside(world, pos)

        if (enclosureInside == null || enclosures == null) {
            enclosures = null
            enclosureInside = null
        }
    }

    /**
     * Update the enclosure of the enclosure controller.
     */
    private fun updateEnclosure(world: World?, pos: BlockPos?) {
        val enclosuresList: MutableList<BlockPos> = mutableListOf()
        val firstEnclosure: BlockPos = pos!!.offset(world!!.getBlockState(pos).get(Properties.HORIZONTAL_FACING))
        var currentEnclosure: BlockPos? = getNextEnclosure(world, firstEnclosure, enclosuresList, firstEnclosure)

        while (currentEnclosure != null && currentEnclosure != firstEnclosure) {
            enclosuresList.add(currentEnclosure)
            currentEnclosure = getNextEnclosure(world, currentEnclosure, enclosuresList, firstEnclosure)
        }

        if (currentEnclosure == null || enclosuresList.size <= 2) {
            enclosures = null
            return
        }

        enclosuresList.add(firstEnclosure)

        enclosures = enclosuresList
    }

    /**
     * Get the next enclosure block in the enclosure.
     */
    private fun getNextEnclosure(
        world: World?,
        pos: BlockPos?,
        enclosuresList: MutableList<BlockPos>,
        firstEnclosure: BlockPos
    ): BlockPos? {
        val north = pos!!.north()
        val south = pos.south()
        val east = pos.east()
        val west = pos.west()
        val northBlockState = world!!.getBlockState(north)
        val southBlockState = world.getBlockState(south)
        val eastBlockState = world.getBlockState(east)
        val westBlockState = world.getBlockState(west)

        // check if the block is a fence and not in the enclosure list
        if (isFence(northBlockState) && !enclosuresList.contains(north) && north != firstEnclosure) {
            return north
        } else if (isFence(southBlockState) && !enclosuresList.contains(south) && south != firstEnclosure) {
            return south
        } else if (isFence(eastBlockState) && !enclosuresList.contains(east) && east != firstEnclosure) {
            return east
        } else if (isFence(westBlockState) && !enclosuresList.contains(west) && west != firstEnclosure) {
            return west
        }

        // if none of them is a fence that is not in the enclosure list, check if they are the first enclosure
        if (north == firstEnclosure || south == firstEnclosure || east == firstEnclosure || west == firstEnclosure) {
            return firstEnclosure
        }

        // if none of them is a fence that is not in the enclosure list nor the first enclosure, return null
        return null
    }

    /**
     * Update the enclosure inside the enclosure controller.
     */
    private fun updateEnclosureInside(world: World?, pos: BlockPos?) {

        if (enclosures == null) {
            return
        }

        val firstInside: BlockPos = pos!!.offset(world!!.getBlockState(pos).get(Properties.HORIZONTAL_FACING))
            .offset(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING))
        val firstInsideLevel = getInsideHeightLevel(world, firstInside)
        enclosureInside = getEnclosureInsideRecursive(world, firstInsideLevel, mutableListOf())
    }

    /**
     * Get the enclosure inside the enclosure controller.
     */
    private fun getEnclosureInsideRecursive(
        world: World?,
        pos: BlockPos?,
        enclosureInsideList: MutableList<BlockPos>,
        recursMax: Int = 100
    ): MutableList<BlockPos>? {
        var list: MutableList<BlockPos>? = enclosureInsideList

        if (recursMax <= 0) {
            return null
        }

        val north = getInsideHeightLevel(world, pos!!.north())
        val south = getInsideHeightLevel(world, pos.south())
        val east = getInsideHeightLevel(world, pos.east())
        val west = getInsideHeightLevel(world, pos.west())

        // check if the block is a fence and not in the enclosure list
        if (!enclosures!!.contains(north) && !enclosureInsideList.contains(north)) {
            list?.add(north)
            if (list != null) {
                list = getEnclosureInsideRecursive(world, north, list, recursMax - 1)
            }
        }
        if (!enclosures!!.contains(south) && !enclosureInsideList.contains(south)) {
            list?.add(south)
            if (list != null) {
                list = getEnclosureInsideRecursive(world, south, list, recursMax - 1)
            }
        }
        if (!enclosures!!.contains(east) && !enclosureInsideList.contains(east)) {
            list?.add(east)
            if (list != null) {
                list = getEnclosureInsideRecursive(world, east, list, recursMax - 1)
            }
        }
        if (!enclosures!!.contains(west) && !enclosureInsideList.contains(west)) {
            list?.add(west)
            if (list != null) {
                list = getEnclosureInsideRecursive(world, west, list, recursMax - 1)
            }
        }

        return list
    }

    /**
     * Get the inside height level of the enclosure.
     */
    private fun getInsideHeightLevel(world: World?, pos: BlockPos?): BlockPos {
        var blockState = world!!.getBlockState(pos!!)
        var position: BlockPos = pos

        if (blockState.isAir) {
            while (blockState.isAir) {
                position = position.down()
                blockState = world.getBlockState(position)
            }
            return position
        } else {
            while (!blockState.isAir) {
                position = position.up()
                blockState = world.getBlockState(position)
            }
            return position.down()
        }
    }


}