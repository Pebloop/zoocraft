package pebloop.zoocraft.blocks

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties.FACING
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView
import pebloop.zoocraft.blockEntities.EnclosureControllerBlockEntity
import pebloop.zoocraft.blockEntities.RabbitTrapBlockEntity
import pebloop.zoocraft.enums.RabbitTrapStatus
import pebloop.zoocraft.items.ZoocraftItems

class RabbitTrapBlock(settings: Settings) : BlockWithEntity(settings)  {

    companion object {
        val STATUS: EnumProperty<RabbitTrapStatus> = EnumProperty.of("rabbit_trap_status", RabbitTrapStatus::class.java)

        val CODEC: MapCodec<RabbitTrapBlock> = createCodec(::RabbitTrapBlock)
    }

    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape {
        return createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    }

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(STATUS)
    }

    override fun canPlaceAt(state: BlockState?, world: WorldView?, pos: BlockPos?): Boolean {
        return super.canPlaceAt(state, world, pos)
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity? {
        if (pos == null || state == null) {
            return null
        }
        return RabbitTrapBlockEntity(pos, state)
    }

    override fun getCodec(): MapCodec<RabbitTrapBlock> {
        return CODEC
    }

    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hit: BlockHitResult?): ActionResult {
        return super.onUse(state, world, pos, player, hit)
    }

    override fun onUseWithItem(stack: ItemStack?, state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hit: BlockHitResult?): ItemActionResult {
        if (world == null || pos == null || state == null || player == null || stack == null) {
            return ItemActionResult.FAIL
        }
        if (world.isClient) {
            return ItemActionResult.SUCCESS
        }
        if (state.get(STATUS) == RabbitTrapStatus.EMPTY && stack.item == Items.CARROT) {
            world.setBlockState(pos, state.with(STATUS, RabbitTrapStatus.BAITED))
            return ItemActionResult.SUCCESS
        }
        return ItemActionResult.FAIL
    }

    override fun onEntityCollision(state: BlockState?, world: World?, pos: BlockPos?, entity: Entity?) {
        super.onEntityCollision(state, world, pos, entity)

        if (world == null || pos == null || state == null || entity == null) {
            return
        }

        if (state.get(STATUS) == RabbitTrapStatus.BAITED && entity is RabbitEntity) {
            world.setBlockState(pos, state.with(STATUS, RabbitTrapStatus.TRAPPED))
            entity.remove(Entity.RemovalReason.DISCARDED)
        }
    }

    override fun onBreak(world: World?, pos: BlockPos?, state: BlockState?, player: PlayerEntity?): BlockState {
        if (world == null || pos == null || state == null || player == null) {
            return super.onBreak(world, pos, state, player)
        }

        if (state.get(STATUS) == RabbitTrapStatus.TRAPPED) {
            world.spawnEntity(ItemEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(ZoocraftItems.TRAPPED_RABBIT)))
        } else if (state.get(STATUS) == RabbitTrapStatus.BAITED) {
            world.spawnEntity(ItemEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(Items.CARROT)))
            world.spawnEntity(ItemEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(ZoocraftBlocks.RABBIT_TRAP_BLOCK.asItem())))
        } else {
            world.spawnEntity(ItemEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(ZoocraftBlocks.RABBIT_TRAP_BLOCK.asItem())))
        }

            return super.onBreak(world, pos, state, player)
    }


}
