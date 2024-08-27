package pebloop.zoocraft.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pebloop.zoocraft.ducks.WorldExtended;

import java.util.ArrayList;

@Mixin(World.class)
public class WorldMixin implements WorldExtended {

	// create a variable in the World class
	@Unique
	private ArrayList<BlockPos> enclosureControllerBlockEntity;

	// inject into the constructor of the World class
	@Inject(at = @At("RETURN"), method = "<init>")
	private void init(CallbackInfo ci) {
		this.enclosureControllerBlockEntity = new ArrayList<>();
	}

	@NotNull
	@Override
	public ArrayList<BlockPos> zoocraft$getEnclosureControllers() {
		return this.enclosureControllerBlockEntity;
	}

	@Override
	public void zoocraft$setEnclosureControllers(@NotNull ArrayList<BlockPos> enclosureController) {
		this.enclosureControllerBlockEntity = enclosureController;
	}
}

