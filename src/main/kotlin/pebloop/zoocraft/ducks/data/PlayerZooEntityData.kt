package pebloop.zoocraft.ducks.data

import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement

class PlayerZooEntityData(val type : EntityType<*>) {

    var totalCaught: Int = 0
    var zoodexStatus: ZoodexStatus = ZoodexStatus.UNKNOWN

    
    enum class ZoodexStatus {
        UNKNOWN,
        SEEN,
        CAUGHT
    }

    fun toNbt(): NbtElement {
        val nbt = NbtCompound()
        nbt.putString("type", type.toString())
        nbt.putInt("totalCaught", totalCaught)
        nbt.putString("zoodexStatus", zoodexStatus.name)
        return nbt
    }
}