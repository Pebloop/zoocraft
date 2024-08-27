package pebloop.zoocraft.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pebloop.zoocraft.ducks.WorldExtended;
import pebloop.zoocraft.blockEntities.EnclosureControllerBlockEntity;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(at = @At("RETURN"), method = "onPlaced")
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        WorldExtended extendedWorld = (WorldExtended) world;
        extendedWorld.zoocraft$getEnclosureControllers().forEach(enclosureControllerBlockEntityPos -> {
            final EnclosureControllerBlockEntity enclosureController = (EnclosureControllerBlockEntity) world.getBlockEntity(enclosureControllerBlockEntityPos);
            if (enclosureController != null)
                enclosureController.updateEnclosure(pos);
        });
    }

    @Inject(at = @At("RETURN"), method = "onBroken")
    public void onBreak(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
        WorldExtended extendedWorld = (WorldExtended) world;
        extendedWorld.zoocraft$getEnclosureControllers().forEach(enclosureControllerBlockEntityPos -> {
            final EnclosureControllerBlockEntity enclosureController = (EnclosureControllerBlockEntity) world.getBlockEntity(enclosureControllerBlockEntityPos);
            if (enclosureController != null)
                enclosureController.updateEnclosure(pos);
        });
    }
}
