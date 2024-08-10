package pebloop.zoocraft

import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.util.function.Supplier


class StateSaverAndLoader: PersistentState() {
    var enclosureControllers: ArrayList<BlockPos> = ArrayList()

    companion object {
        private val type = Type<StateSaverAndLoader>(
                Supplier<StateSaverAndLoader> { StateSaverAndLoader() },
                StateSaverAndLoader::createFromNbt,
                null
        )

        fun createFromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): StateSaverAndLoader {
            val state = StateSaverAndLoader()
            val x = nbt.getIntArray("enclosure_controllers_x")
            val y = nbt.getIntArray("enclosure_controllers_y")
            val z = nbt.getIntArray("enclosure_controllers_z")
            if (x != null && y != null && z != null) {
                for (i in x.indices) {
                    state.enclosureControllers.add(BlockPos(x[i], y[i], z[i]))
                }
            }
            return state
        }

        fun getServerState(server: MinecraftServer): StateSaverAndLoader {
            val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager
            val state = persistentStateManager.getOrCreate<StateSaverAndLoader>(type, "zoocraft")
            state.markDirty()

            return state
        }
    }
    override fun writeNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?): NbtCompound {
        val x = mutableListOf<Int>()
        val y = mutableListOf<Int>()
        val z = mutableListOf<Int>()
        for (i in 0 until enclosureControllers.size) {
            val pos = enclosureControllers[i]
            x.add(pos.x)
            y.add(pos.y)
            z.add(pos.z)
        }
        nbt?.putIntArray("enclosure_controllers_x", x.toIntArray())
        nbt?.putIntArray("enclosure_controllers_y", y.toIntArray())
        nbt?.putIntArray("enclosure_controllers_z", z.toIntArray())
        return nbt!!
    }

}