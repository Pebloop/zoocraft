package pebloop.zoocraft.mixin.entities;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEntity.class)
interface MobEntityAccessor {
    @Accessor
    GoalSelector getGoalSelector();
}