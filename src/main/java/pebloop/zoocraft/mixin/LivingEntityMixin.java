package pebloop.zoocraft.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pebloop.zoocraft.ducks.LivingEntityExtended;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityExtended {

    @Unique
    boolean catchable = true;

    @Unique
    boolean caught = false;




    @Inject(at = @At("RETURN"), method = "<init>")
    public void init(EntityType entityType, World world, CallbackInfo ci) {

    }

    @Override
    public boolean zoocraft$isCatchable() {
        return catchable;
    }

    @Override
    public void zoocraft$setCatchable(boolean catchable) {
        this.catchable = catchable;
    }

    @Override
    public boolean zoocraft$isCaught() {
        return caught;
    }

    @Override
    public void zoocraft$setCaught(boolean caught) {
        this.caught = caught;
    }
}
