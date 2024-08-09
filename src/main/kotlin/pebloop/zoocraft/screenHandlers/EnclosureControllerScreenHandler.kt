package pebloop.zoocraft.screenHandlers

import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class EnclosureControllerScreenHandler(sincId: Int, inventory: PlayerInventory?, data: EnclosureControllerData) : ScreenHandler(ZoocraftScreenHandler.ENCLOSURE_CONTROLLER_SCREEN_HANDLER, sincId) {
    val data = data
    companion object {

        val PACKET_CODEC: PacketCodec<ByteBuf, EnclosureControllerData> = PacketCodec.of(
                { value, buf ->
                    PacketCodecs.writeCollectionSize(buf, value.surface.size, 0x7FFFFFFF)
                    for (i in 0 until value.surface.size) {
                        val block = value.surface[i]
                        PacketByteBuf(buf).writeBlockPos(block.pos)
                        PacketByteBuf(buf).writeIdentifier(block.block.registryEntry.registryKey().value)
                    }
                },
                { buf ->
                    val size = PacketCodecs.readCollectionSize(buf, 0x7FFFFFFF)
                    val list = ArrayList<EnclosureBlockData>()
                    for (i in 0 until size) {
                        val pos = PacketByteBuf(buf).readBlockPos()
                        val id = PacketByteBuf(buf).readIdentifier()
                        val block = Registries.BLOCK.get(id) ?: Blocks.AIR
                        list.add(EnclosureBlockData(pos, block))
                    }
                    EnclosureControllerData(list)
                }

        )

    }

    override fun quickMove(player: PlayerEntity?, slot: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true;
    }
}