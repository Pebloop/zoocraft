package pebloop.zoocraft.owo_screens

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.*
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Sizing
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import pebloop.zoocraft.Zoocraft
import pebloop.zoocraft.ducks.PlayerEntityExtended
import pebloop.zoocraft.ducks.data.PlayerZooEntityData
import pebloop.zoocraft.packets.SetNamePacket


class ZoodexOwoScreen(val player: PlayerEntity) : BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(Identifier.of("zoocraft", "zoodex_screen"))) {

    var index = 0

    fun displayEntity(container: FlowLayout, entityName: LabelComponent?) {
        val playerExtended = player as PlayerEntityExtended
        val entities = Zoocraft.DATA.getEntities()
        val entityType = entities[index]
        val entityComponent = Components.entity(Sizing.fixed(50), entityType, null).scaleToFit(true)
        val playerData = playerExtended.getZoodexData()
        val entityData = playerData.firstOrNull { it.type == entityType }

        if (entityData != null) {
            // CAUGHT
            if (entityData.zoodexStatus == PlayerZooEntityData.ZoodexStatus.CAUGHT) {
                entityName?.text(Text.of(entityType.getName().string))
                container.children(listOf(entityComponent))

            // SEEN
            } else if (entityData.zoodexStatus == PlayerZooEntityData.ZoodexStatus.SEEN) {
                entityName?.text(Text.of("????"))
                container.children(listOf(entityComponent))

            // UNKNOWN
            } else {
                entityName?.text(Text.of("????"))
            }
        // Not found
        } else {
            entityName?.text(Text.of("????"))
        }
    }
    override fun build(rootComponent: FlowLayout?) {
        val buttonLeft = rootComponent?.childById(ButtonComponent::class.java, "left")
        val buttonRight = rootComponent?.childById(ButtonComponent::class.java, "right")
        val entityContainer = rootComponent?.childById(FlowLayout::class.java, "entity_container")
        val entityName = rootComponent?.childById(LabelComponent::class.java, "name")

        if (entityContainer != null) {
            displayEntity(entityContainer, entityName)
        }

        buttonLeft?.onPress {
            if (index > 0)
                index--
            else
                index = Zoocraft.DATA.getEntities().size - 1
            if (entityContainer != null) {
                displayEntity(entityContainer, entityName)
            }
        }
        buttonRight?.onPress {
            if (index < Zoocraft.DATA.getEntities().size - 1)
                index++
            else
                index = 0
            if (entityContainer != null) {
                displayEntity(entityContainer, entityName)
            }
        }
    }

}