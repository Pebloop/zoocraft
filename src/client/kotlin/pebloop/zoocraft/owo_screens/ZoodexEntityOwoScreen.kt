package pebloop.zoocraft.owo_screens

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.*
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Color
import io.wispforest.owo.ui.core.Sizing
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import pebloop.zoocraft.Zoocraft
import pebloop.zoocraft.ducks.LivingEntityExtended
import pebloop.zoocraft.packets.SetNamePacket
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexEntityScreenHandler


class ZoodexEntityOwoScreen(val zoodexEntityScreenHandler: ZoodexEntityScreenHandler) : BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(Identifier.of("zoocraft", "zoodex_entity_screen"))) {

    override fun build(rootComponent: FlowLayout?) {
        val entityId = zoodexEntityScreenHandler.zoodexEntityScreenData.entity;
        val entity = client?.world?.entities?.first { it.uuid == entityId } as? LivingEntity
        val entityExtended = entity as LivingEntityExtended

        val entityName = rootComponent?.childById(LabelComponent::class.java, "name")
        entityName?.text(Text.of(entity.name.string))

        val entityContainer = rootComponent?.childById(FlowLayout::class.java, "entity_container")
        val entityComponent = Components.entity(Sizing.fixed(50), entity.type, null).scaleToFit(true)
        entityContainer?.children(listOf(entityComponent))

        // Health
        val entityHealth = rootComponent?.childById(BoxComponent::class.java, "health")
        val healthRatio = entity.health / entity.maxHealth * 50
        entityHealth?.fill(true)
        entityHealth?.sizing(Sizing.fixed(healthRatio.toInt()), Sizing.fixed(10))

        // Hunger
        val entityHunger = rootComponent?.childById(BoxComponent::class.java, "hunger")
        val hungerRatio = entityExtended.`zoocraft$getHunger`() / entityExtended.`zoocraft$getHungerMax`() * 50;
        entityHunger?.fill(true)
        entityHunger?.sizing(Sizing.fixed(hungerRatio.toInt()), Sizing.fixed(10))

        // Environment
        val entityNeeds = rootComponent?.childById(BoxComponent::class.java, "needs")
        entityNeeds?.fill(true)
        if (zoodexEntityScreenHandler.zoodexEntityScreenData.enclosurePos != null) {
            val enclosure = client?.world?.getBlockEntity(zoodexEntityScreenHandler.zoodexEntityScreenData.enclosurePos) as pebloop.zoocraft.blockEntities.EnclosureControllerBlockEntity
            val needsRatio = entityExtended.`zoocraft$getNeeds`(enclosure.getScreenOpeningData(null))
            entityNeeds?.sizing(Sizing.fixed(needsRatio.toInt() / 2), Sizing.fixed(10))
        }
    }

}