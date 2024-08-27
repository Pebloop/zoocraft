package pebloop.zoocraft.owo_screens

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.component.LabelComponent
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Color
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureControllerData


class EnclosureControllerOwoScreen(enclosureControllerData: EnclosureControllerData) : BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(Identifier.of("zoocraft", "enclosure_controller_screen"))) {
    val enclosureData = enclosureControllerData
    override fun build(rootComponent: FlowLayout?) {
        // display animals
        val entitiesContainer = rootComponent?.childById(FlowLayout::class.java, "entities")
        val entitiesList = ArrayList<LabelComponent>()
        for (entity in enclosureData.entities) {
            entitiesList.add(Components.label(Text.of(entity.typeName)).color(Color(0f, 0f, 0f, 255f)))
        }
        entitiesContainer?.children(entitiesList)

        // display surface
        val surfaceContainer = rootComponent?.childById(FlowLayout::class.java, "surface")
        val surfaceList = ArrayList<LabelComponent>()
        val surfaceMap: MutableMap<String, Int> = HashMap()
        for (surface in enclosureData.surface) {
            val block = surface.block
            if (surfaceMap.containsKey(block.name.string)) {
                surfaceMap[block.name.string] = surfaceMap[block.name.string]!! + 1
            } else {
                surfaceMap[block.name.string] = 1
            }
        }
        for ((key, value) in surfaceMap) {
            val percentage = value / enclosureData.surface.size.toFloat() * 100
            surfaceList.add(Components.label(Text.of("$key: $percentage%")).color(Color(0f, 0f, 0f, 255f)))
        }
        surfaceContainer?.children(surfaceList)
    }

}