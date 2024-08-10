package pebloop.zoocraft.blocks

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties.FACING
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockRotation
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView
import pebloop.zoocraft.WorldExtended
import pebloop.zoocraft.blockEntities.EnclosureControllerBlockEntity
import pebloop.zoocraft.screenHandlers.EnclosureBlockData

class EnclosureControllerBlock(settings: Settings?) : BlockWithEntity(settings)  {

    val MAX_SURFACE: Int = 10000

    companion object {
        val CONTROLLER_STATUS: BooleanProperty = BooleanProperty.of("controller_status")

        val CODEC: MapCodec<EnclosureControllerBlock> = createCodec(::EnclosureControllerBlock)
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

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
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

    // this block can only be placed on a closed enclosure
    override fun canPlaceAt(state: BlockState?, world: WorldView?, pos: BlockPos?): Boolean {

        val facing = state?.get(FACING) ?: return false

        val supportBlock = pos?.offset(facing)
        val supportBlockType = world?.getBlockState(supportBlock)?.block
        if (supportBlockType !is FenceBlock) {
            return false
        }

        val otherBlock = supportBlock?.offset(facing)
        val otherBlockType = world.getBlockState(otherBlock)?.block
        if (otherBlockType !is AirBlock) {
            return false
        }

        return super.canPlaceAt(state, world, pos)
    }

    /// This function compute the enclosure and fences of the enclosure
    private fun getEnclosure(world: World, pos: BlockPos): Pair<List<BlockPos>, List<BlockPos>>? {
        val enclosure = mutableListOf<BlockPos>()
        val fences = mutableListOf<BlockPos>()
        val visited = mutableSetOf<BlockPos>()
        val stack = mutableListOf<BlockPos>()

        stack.add(pos)
        while (stack.isNotEmpty()) {
            if (enclosure.size > MAX_SURFACE) {
                return null
            }

            val current = stack.removeAt(0)
            if (visited.contains(current)) {
                continue
            }
            visited.add(current)
            enclosure.add(current)

            val neighbors = mutableListOf<BlockPos>()
            neighbors.add(current.offset(Direction.NORTH))
            neighbors.add(current.offset(Direction.EAST))
            neighbors.add(current.offset(Direction.SOUTH))
            neighbors.add(current.offset(Direction.WEST))

            for (neighbor in neighbors) {
                if (visited.contains(neighbor)) {
                    continue
                }
                val block = world.getBlockState(neighbor).block
                if (block !is FenceBlock) {
                    stack.add(neighbor)
                } else {
                    if (!fences.contains(neighbor)) {
                        fences.add(neighbor)
                    }
                }
            }

        }

        if (enclosure.size > MAX_SURFACE) {
            return null
        }

        return Pair(enclosure, fences)
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? {
        if (pos == null || state == null) {
            return null
        }
        return EnclosureControllerBlockEntity(pos, state)
    }

    override fun getCodec(): MapCodec<EnclosureControllerBlock> {
        return CODEC
    }

    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hit: BlockHitResult?): ActionResult {
        if (world != null) {
            if (world.isClient())
                return ActionResult.SUCCESS

            val screenHandlerFactory: NamedScreenHandlerFactory? = state?.createScreenHandlerFactory(world, pos)
            if (screenHandlerFactory != null) {
                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS)
                val entity = world.getBlockEntity(pos) as EnclosureControllerBlockEntity
                println(entity.surface.size)
                player?.openHandledScreen(screenHandlerFactory)
                return ActionResult.CONSUME
            }
        }
        return ActionResult.PASS
    }

    override fun onStateReplaced(state: BlockState?, world: World?, pos: BlockPos?, newState: BlockState?, moved: Boolean) {
        if (state == null || world == null || pos == null || newState == null) {
            super.onStateReplaced(state, world, pos, newState, moved)
            return
        }
        if (newState.block != ZoocraftBlocks.ENCLOSURE_CONTROLLER) {
            val entity = world.getBlockEntity(pos)
            if (entity is EnclosureControllerBlockEntity) {
                val worldExtended = world as WorldExtended
                val controllers = worldExtended.`zoocraft$getEnclosureControllers`()
                val controllerToRemove = controllers.indexOf(entity.pos)
                if (controllerToRemove != -1) {
                    controllers.removeAt(controllerToRemove)
                }
                worldExtended.`zoocraft$setEnclosureControllers`(controllers)
            }
            entity?.markDirty()
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun onBlockAdded(state: BlockState?, world: World?, pos: BlockPos?, oldState: BlockState?, notify: Boolean) {
        super.onBlockAdded(state, world, pos, oldState, notify)

        val entity = world?.getBlockEntity(pos)

        val facing = state?.get(FACING)
        val startBlock = pos?.offset(facing)?.offset(facing)

        val enclosureData = getEnclosure(world!!, startBlock!!)
        val enclosure = enclosureData?.first
        val fences = enclosureData?.second

        if (enclosure != null) {
            world.setBlockState(pos, state?.with(CONTROLLER_STATUS, true), 3)

            if (entity is EnclosureControllerBlockEntity) {
                val enclosureControllerEntity = entity as EnclosureControllerBlockEntity
                enclosureControllerEntity.surface.clear()
                enclosureControllerEntity.fences.clear()

                for (block in enclosure) {
                    var pos = block
                    while (pos.y > 0) {
                        pos = pos.down()
                        val type = world.getBlockState(pos).block
                        if (type !is AirBlock) {
                            break
                        }
                    }

                    val type = world.getBlockState(pos).block

                    enclosureControllerEntity.surface.add(EnclosureBlockData(pos, type))
                    entity.markDirty()
                }
                if (fences != null) {
                    enclosureControllerEntity.fences.addAll(fences)
                }
            }

        } else {
            world.setBlockState(pos, state?.with(CONTROLLER_STATUS, false), 3)
        }

        val worldExtended = world as WorldExtended
        val controllers = worldExtended.`zoocraft$getEnclosureControllers`()
        if (entity != null) {
            if (controllers.contains(entity.pos)) {
                return
            }
        }
        if (entity != null) {
            controllers.add(entity.pos)
        }
        worldExtended.`zoocraft$setEnclosureControllers`(controllers)
    }

}
