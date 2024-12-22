package pebloop.zoocraft.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pebloop.zoocraft.ducks.LivingEntityExtended;
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureControllerData;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityExtended {

    @Unique
    boolean caught = false;

    @Unique
    float hungerMax = 100;

    @Unique
    float hunger = 100;

    @Inject(at = @At("RETURN"), method = "<init>")
    public void init(EntityType entityType, World world, CallbackInfo ci) {

    }

    @Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (caught) {
            hunger -= 0.01f;
            if (hunger <= 0) {
                hunger = 0;
            }
        }
    }

    /*@Override
    public boolean zoocraft$isCatchable() {
        return catchable;
    }

    @Override
    public void zoocraft$setCatchable(boolean catchable) {
        LivingEntityMixin.catchable = catchable;
    }*/

    @Override
    public boolean zoocraft$isCaught() {
        return caught;
    }

    @Override
    public void zoocraft$setCaught(boolean caught) {
        this.caught = caught;
    }

    @Override
    public float zoocraft$getHunger() {
        return hunger;
    }

    @Override
    public void zoocraft$setHungerMax(float hunger) {
        this.hungerMax = hunger;
    }

    @Override
    public float zoocraft$getHungerMax() {
        return hungerMax;
    }

    @Override
    public void zoocraft$setHunger(float hunger) {
        this.hunger = hunger;
    }

    @Override
    public float zoocraft$getNeeds(@NotNull EnclosureControllerData enclosureData) {
        return 100;
    }
}
