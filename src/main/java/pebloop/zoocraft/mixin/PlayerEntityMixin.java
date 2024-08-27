package pebloop.zoocraft.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pebloop.zoocraft.Zoocraft;
import pebloop.zoocraft.ducks.PlayerEntityExtended;
import pebloop.zoocraft.ducks.data.PlayerZooEntityData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityExtended {

    @Unique private ArrayList<PlayerZooEntityData> zooEntities = new ArrayList<>();

    @Inject(at = @At("TAIL"), method = "<init>")
    void init(CallbackInfo ci) {
        zooEntities = new ArrayList<>();

        for (int i = 0; i < Zoocraft.INSTANCE.getDATA().getZooEntities().size(); i++) {
            zooEntities.add(new PlayerZooEntityData(Zoocraft.INSTANCE.getDATA().getZooEntities().get(i)));
        }
    }


    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("zooEntitiesSize", zooEntities.size());
        for (int i = 0; i < zooEntities.size(); i++) {
            PlayerZooEntityData zooEntity = zooEntities.get(i);
            String typeName = zooEntity.getType().getName().getString();
            nbt.put("zooEntity_" + i, zooEntity.toNbt());
        }
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        zooEntities = new ArrayList<>();
        int zooEntitiesSize = nbt.getInt("zooEntitiesSize");
        for (int i = 0; i < zooEntitiesSize; i++) {
            NbtCompound zooEntityNbt = nbt.getCompound("zooEntity_" + i);
            Optional<EntityType<?>> entityType = EntityType.get(zooEntityNbt.getString("type"));
            if (entityType.isPresent()) {
                PlayerZooEntityData zooEntity = new PlayerZooEntityData(entityType.get());
                zooEntity.setTotalCaught(zooEntityNbt.getInt("totalCaught"));
                PlayerZooEntityData.ZoodexStatus zoodexStatus = PlayerZooEntityData.ZoodexStatus.valueOf(zooEntityNbt.getString("zoodexStatus"));
                zooEntity.setZoodexStatus(zoodexStatus);
                if (zooEntities.stream().noneMatch(e -> e.getType().equals(entityType.get())))
                    zooEntities.add(zooEntity);
                else {
                    PlayerZooEntityData existingZooEntity = zooEntities.stream().filter(e -> e.getType().equals(entityType.get())).findFirst().get();
                    existingZooEntity.setTotalCaught(zooEntity.getTotalCaught());
                    existingZooEntity.setZoodexStatus(zooEntity.getZoodexStatus());
                }
            }

        }
    }

    @NotNull
    @Override
    public ArrayList<PlayerZooEntityData> getZoodexData() {
        return zooEntities;
    }

    @Override
    public void setZoodexData(@NotNull ArrayList<PlayerZooEntityData> data) {
        zooEntities = data;
    }
}
