package pebloop.zoocraft.mixin;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pebloop.zoocraft.WorldExtended;
import pebloop.zoocraft.blockEntities.EnclosureControllerBlockEntity;
import pebloop.zoocraft.blocks.ZoocraftBlocks;

import java.util.ArrayList;
import java.util.List;

@Mixin(World.class)
public class ExampleMixin implements WorldExtended {

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

