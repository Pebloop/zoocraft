package pebloop.zoocraft.blockEntities

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
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
import net.minecraft.util.TypeFilter
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import pebloop.zoocraft.blockEntities.ZoocraftBlockEntities.Companion.ENCLOSURE_CONTROLLER_BLOCK_ENTITY
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureBlockData
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureControllerData
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureControllerScreenHandler
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureEntityData
import java.util.UUID

class EnclosureControllerBlockEntity(blockPos: BlockPos, blockState: BlockState) : BlockEntity(ENCLOSURE_CONTROLLER_BLOCK_ENTITY, blockPos, blockState), ExtendedScreenHandlerFactory<EnclosureControllerData> {
    var surface = mutableListOf<EnclosureBlockData>()
    var fences = mutableListOf<BlockPos>()
    var entities = mutableListOf<UUID>()
    private var box: Box = Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    override fun writeNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.writeNbt(nbt, registryLookup)

        // register surface
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


        // register fences
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

        // register entities
        nbt?.putInt("entities_size", entities.size)
        for (i in 0 until entities.size) {
            nbt?.putUuid("entities_uuid_$i", entities[i])
        }

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

            // compute bounding box
            computeBoundingBox()

            // read entities
            val entitiesSize = nbt.getInt("entities_size")
            entities.clear()
            for (i in 0 until entitiesSize) {
                entities.add(nbt.getUuid("entities_uuid_$i"))
            }

        }
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
        var entitiesData: ArrayList<EnclosureEntityData> = ArrayList()

        if (world != null) {

            val ents = world!!.getEntitiesByType(TypeFilter.instanceOf(LivingEntity::class.java), box) {
                if (entities.contains<UUID>(it.uuid)) {
                    return@getEntitiesByType true
                }
                return@getEntitiesByType false
            }

            for (ent in ents) {

                if (ent != null) {
                    val entityData = EnclosureEntityData(ent.type.name.string)
                    entitiesData.add(entityData)
                }
            }
        }
        return EnclosureControllerScreenHandler(syncId, playerInventory, EnclosureControllerData(surface, entitiesData))
    }

    override fun getDisplayName(): Text {
        return Text.of("Enclosure Controller")
    }

    override fun getScreenOpeningData(player: ServerPlayerEntity?): EnclosureControllerData {

        val entitiesData: ArrayList<EnclosureEntityData> = ArrayList()
        if (world != null) {

            val ents = world!!.getEntitiesByType(TypeFilter.instanceOf(LivingEntity::class.java), box) {
                if (entities.contains<UUID>(it.uuid)) {
                    return@getEntitiesByType true
                }
                return@getEntitiesByType false
            }

            for (entity in ents) {
                val entityData = EnclosureEntityData(entity.type.name.string)
                entitiesData.add(entityData)
            }
        }

        return EnclosureControllerData(surface, entitiesData)
    }


    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup?): NbtCompound {
        return createNbt(registryLookup)
    }

    fun computeBoundingBox() {
        val min = BlockPos(fences.minOf { it.x }, fences.minOf { it.y }, fences.minOf { it.z })
        val max = BlockPos(fences.maxOf { it.x }, fences.maxOf { it.y }, fences.maxOf { it.z })
        box = Box(min.x.toDouble(), min.y.toDouble() - 10, min.z.toDouble(), max.x + 1.0, max.y + 11.0, max.z + 1.0)
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

    fun isInside(pos: BlockPos): Boolean {
        computeBoundingBox()
        return box.contains(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }

    fun addAnimal(entity: Entity) {
        entities.add(entity.uuid)
        markDirty()
    }

    fun removeAnimal(entity: Entity) {
        entities.remove(entity.uuid)
        markDirty()
    }
}