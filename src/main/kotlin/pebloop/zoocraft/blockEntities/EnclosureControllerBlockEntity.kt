package pebloop.zoocraft.blockEntities

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import pebloop.zoocraft.blockEntities.ZoocraftBlockEntities.Companion.ENCLOSURE_CONTROLLER_BLOCK_ENTITY
import pebloop.zoocraft.screenHandlers.EnclosureBlockData
import pebloop.zoocraft.screenHandlers.EnclosureControllerData
import pebloop.zoocraft.screenHandlers.EnclosureControllerScreenHandler

class EnclosureControllerBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity(ENCLOSURE_CONTROLLER_BLOCK_ENTITY, blockPos, blockState), ExtendedScreenHandlerFactory<EnclosureControllerData> {
    val surface = mutableListOf<EnclosureBlockData>()
    val fences = mutableListOf<BlockPos>()
    override fun writeNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        for (i in 0 until surface.size) {
            val block = surface[i]
            nbt?.putIntArray("surface_x", intArrayOf(block.pos.x))
            nbt?.putIntArray("surface_y", intArrayOf(block.pos.y))
            nbt?.putIntArray("surface_z", intArrayOf(block.pos.z))
        }
        for (i in 0 until fences.size) {
            val pos = fences[i]
            nbt?.putIntArray("fences_x", intArrayOf(pos.x))
            nbt?.putIntArray("fences_y", intArrayOf(pos.y))
            nbt?.putIntArray("fences_z", intArrayOf(pos.z))
        }

        super.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registryLookup)

        if (nbt != null) {
            val surfaceX = nbt.getIntArray("surface_x")
            val surfaceY = nbt.getIntArray("surface_y")
            val surfaceZ = nbt.getIntArray("surface_z")

            for (i in 0 until surfaceX.size) {
                val block = world?.getBlockState(BlockPos(surfaceX[i], surfaceY[i], surfaceZ[i]))?.block
                if (block != null)
                    surface.add(EnclosureBlockData(BlockPos(surfaceX[i], surfaceY[i], surfaceZ[i]), block))
            }

            val fencesX = nbt.getIntArray("fences_x")
            val fencesY = nbt.getIntArray("fences_y")
            val fencesZ = nbt.getIntArray("fences_z")
            for (i in 0 until fencesX.size) {
                fences.add(BlockPos(fencesX[i], fencesY[i], fencesZ[i]))
            }

        }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
        return EnclosureControllerScreenHandler(syncId, playerInventory, EnclosureControllerData(surface))
    }

    override fun getDisplayName(): Text {
        return Text.of("Enclosure Controller")
    }

    override fun getScreenOpeningData(player: ServerPlayerEntity?): EnclosureControllerData {

        return EnclosureControllerData(surface)
    }


    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup?): NbtCompound {
        return createNbt(registryLookup)
    }
}