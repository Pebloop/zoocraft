package pebloop.zoocraft.screenHandlers

import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.RabbitEntity
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
import java.util.UUID

class SetNameScreenHandler(sincId: Int, inventory: PlayerInventory?, val entity: UUID) : ScreenHandler(ZoocraftScreenHandler.SET_NAME_SCREEN_HANDLER, sincId) {

    companion object {
        val PACKET_CODEC: PacketCodec<ByteBuf, UUID> = PacketCodec.of(
                { value, buf ->
                    PacketByteBuf(buf).writeUuid(value)
                },
                { buf ->
                    PacketByteBuf(buf).readUuid()
                }
        )
    }
    override fun quickMove(player: PlayerEntity?, slot: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true
    }

    override fun onClosed(player: PlayerEntity?) {
        super.onClosed(player)
    }

    override fun onButtonClick(player: PlayerEntity?, id: Int): Boolean {
        println("Button clicked")
        return super.onButtonClick(player, id)
    }

    fun getEntityId(): UUID {
        return entity
    }

}