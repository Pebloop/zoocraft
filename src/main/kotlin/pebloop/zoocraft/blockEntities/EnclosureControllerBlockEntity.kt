package pebloop.zoocraft.blockEntities

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import pebloop.zoocraft.blockEntities.ZoocraftBlockEntities.Companion.ENCLOSURE_CONTROLLER_BLOCK_ENTITY
import pebloop.zoocraft.screenHandlers.EnclosureControllerScreenHandler

class EnclosureControllerBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity(ENCLOSURE_CONTROLLER_BLOCK_ENTITY, blockPos, blockState), NamedScreenHandlerFactory {

    val surface = mutableListOf<BlockPos>()
    val fences = mutableListOf<BlockPos>()
    override fun writeNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        for (i in 0 until surface.size) {
            val pos = surface[i]
            nbt?.putIntArray("surface_x", intArrayOf(pos.x))
            nbt?.putIntArray("surface_y", intArrayOf(pos.y))
            nbt?.putIntArray("surface_z", intArrayOf(pos.z))
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
                surface.add(BlockPos(surfaceX[i], surfaceY[i], surfaceZ[i]))
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
        return EnclosureControllerScreenHandler(syncId, playerInventory)
    }

    override fun getDisplayName(): Text {
        return Text.of("Enclosure Controller")
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup?): NbtCompound {
        return createNbt(registryLookup)
    }
}