package pebloop.zoocraft.mixin.entities;

import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pebloop.zoocraft.entities.goals.RabbitTrapGoal;

import java.util.List;
import java.util.Set;

@Mixin(RabbitEntity.class)
public abstract class RabbitMixin {

    @Inject(at = @At("TAIL"), method = "initGoals")
    public void initGoals(CallbackInfo ci) {
        GoalSelector goalSelector = ((MobEntityAccessor) this).getGoalSelector();
        MobEntity mobEntity = (MobEntity) (Object) this;
        Set<PrioritizedGoal> goals = goalSelector.getGoals();
        goals.removeIf(prioritizedGoal -> prioritizedGoal.getGoal() instanceof TemptGoal);

        goalSelector.add(3, new RabbitTrapGoal((RabbitEntity) mobEntity));
    }


}
