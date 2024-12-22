package pebloop.zoocraft.screenHandlers.zoodex

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import pebloop.zoocraft.screenHandlers.ZoocraftScreenHandler
import java.util.UUID

class ZoodexEntityScreenHandler(sincId: Int, inventory: PlayerInventory?, val zoodexEntityScreenData: ZoodexEntityScreenData) : ScreenHandler(ZoocraftScreenHandler.ZOODEX_ENTITY_SCREEN_HANDLER, sincId) {

    companion object {
        val PACKET_CODEC: PacketCodec<ByteBuf, ZoodexEntityScreenData> = PacketCodec.of(
                { value, buf ->
                    PacketByteBuf(buf).writeUuid(value.entity)
                    if (value.enclosurePos != null) {
                        PacketByteBuf(buf).writeBoolean(true)
                        PacketByteBuf(buf).writeBlockPos(value.enclosurePos)
                    } else {
                        PacketByteBuf(buf).writeBoolean(false)
                    }
                },
                { buf ->
                    val entity = PacketByteBuf(buf).readUuid()
                    val hasEnclosurePos = PacketByteBuf(buf).readBoolean()
                    if (!hasEnclosurePos) {
                        ZoodexEntityScreenData(entity, null)
                    } else {
                        val enclosurePos = PacketByteBuf(buf).readBlockPos()
                        ZoodexEntityScreenData(entity, enclosurePos)
                    }
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

}