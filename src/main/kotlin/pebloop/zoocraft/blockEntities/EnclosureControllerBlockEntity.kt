package pebloop.zoocraft.blockEntities

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.AirBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import pebloop.zoocraft.WorldExtended
import pebloop.zoocraft.blockEntities.ZoocraftBlockEntities.Companion.ENCLOSURE_CONTROLLER_BLOCK_ENTITY
import pebloop.zoocraft.screenHandlers.EnclosureBlockData
import pebloop.zoocraft.screenHandlers.EnclosureControllerData
import pebloop.zoocraft.screenHandlers.EnclosureControllerScreenHandler

class EnclosureControllerBlockEntity(blockPos: BlockPos, blockState: BlockState) : BlockEntity(ENCLOSURE_CONTROLLER_BLOCK_ENTITY, blockPos, blockState), ExtendedScreenHandlerFactory<EnclosureControllerData> {
    var surface = mutableListOf<EnclosureBlockData>()
    var fences = mutableListOf<BlockPos>()

    override fun writeNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.writeNbt(nbt, registryLookup)
        val surfaceX = mutableListOf<Int>()
        val surfaceY = mutableListOf<Int>()
        val surfaceZ = mutableListOf<Int>()
        var surfaceIdentifier = mutableListOf<Identifier>()
        for (i in 0 until surface.size) {
            val block = surface[i]
            surfaceX.add(block.pos.x)
            surfaceY.add(block.pos.y)
            surfaceZ.add(block.pos.z)
            surfaceIdentifier.add(Registries.BLOCK.getId(block.block))
        }
        nbt?.putInt("surface_size", surface.size)
        nbt?.putIntArray("surface_x", surfaceX.toIntArray())
        nbt?.putIntArray("surface_y", surfaceY.toIntArray())
        nbt?.putIntArray("surface_z", surfaceZ.toIntArray())
        if (surfaceIdentifier.size > 0) {
            val bytes = ByteArray(surfaceIdentifier.size * 23)
            for (i in 0 until surfaceIdentifier.size) {
                val id = surfaceIdentifier[i]
                val idString = id.toString()
                for (j in 0 until idString.length) {
                    bytes[i * 23 + j] = idString[j].toByte()
                }
            }
            nbt?.putByteArray("surface_identifier", bytes)
        } else {
            nbt?.putByteArray("surface_identifier", byteArrayOf())
        }

        val fencesX = mutableListOf<Int>()
        val fencesY = mutableListOf<Int>()
        val fencesZ = mutableListOf<Int>()
        for (i in 0 until fences.size) {
            val pos = fences[i]
            fencesX.add(pos.x)
            fencesY.add(pos.y)
            fencesZ.add(pos.z)
        }
        nbt?.putInt("fences_size", fences.size)
        nbt?.putIntArray("fences_x", fencesX.toIntArray())
        nbt?.putIntArray("fences_y", fencesY.toIntArray())
        nbt?.putIntArray("fences_z", fencesZ.toIntArray())

    }

    override fun readNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registryLookup)

        if (nbt != null) {
            val surfaceSize = nbt.getInt("surface_size")
            val surfaceX = nbt.getIntArray("surface_x")
            val surfaceY = nbt.getIntArray("surface_y")
            val surfaceZ = nbt.getIntArray("surface_z")
            val surfaceIdentifier = nbt.getByteArray("surface_identifier")

            surface = mutableListOf()
            for (i in 0 until surfaceSize) {
                    val identifierStr = surfaceIdentifier.copyOfRange(i * 23, (i + 1) * 23)
                    val cleanedIdentifier = identifierStr.toString(Charsets.UTF_8).filter { it != '\u0000'}
                    val identifier = Identifier.of(cleanedIdentifier)
                    val block = Registries.BLOCK.get(identifier)
                surface.add(EnclosureBlockData(BlockPos(surfaceX[i], surfaceY[i], surfaceZ[i]), block))
            }

            fences = mutableListOf()
            val fencesSize = nbt.getInt("fences_size")
            val fencesX = nbt.getIntArray("fences_x")
            val fencesY = nbt.getIntArray("fences_y")
            val fencesZ = nbt.getIntArray("fences_z")
            for (i in 0 until fencesSize) {
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

    /// This function is called when a block is placed or removed to update the enclosure status in the controller
    fun updateEnclosure(pos: BlockPos) {
        if (world != null) {
            // if block is in the surface, remove it
            val block = world?.getBlockState(pos)?.block
            val index = surface.indexOfFirst { it.pos == pos }
            if (index != -1) {
                surface.removeAt(index)
                if (block != null) {
                    surface.add(EnclosureBlockData(pos, block))
                }
                markDirty()
            }
        }
    }
}