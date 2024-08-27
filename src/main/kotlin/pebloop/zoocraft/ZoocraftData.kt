package pebloop.zoocraft

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.util.TypeFilter
import pebloop.zoocraft.Zoocraft

class ZoocraftData {
    val zooEntities: ArrayList<EntityType<*>> = ArrayList()

    init {
        if (Zoocraft.CONFIG.isRabbitCatchable)
            zooEntities.add(EntityType.RABBIT)
        if (Zoocraft.CONFIG.isPigCatchable)
            zooEntities.add(EntityType.PIG)
        if (Zoocraft.CONFIG.isCowCatchable)
            zooEntities.add(EntityType.COW)
        if (Zoocraft.CONFIG.isChickenCatchable)
            zooEntities.add(EntityType.CHICKEN)
        if (Zoocraft.CONFIG.isSheepCatchable)
            zooEntities.add(EntityType.SHEEP)
    }

    fun addEntity(entity: EntityType<*>) {
        zooEntities.add(entity)
    }

    fun removeEntity(entity: EntityType<*>) {
        zooEntities.remove(entity)
    }

    fun getEntities(): ArrayList<EntityType<*>> {
        return zooEntities
    }

    fun isCatchable(entity: EntityType<*>): Boolean {
        return zooEntities.contains(entity)
    }
}