package pebloop.zoocraft.owo_screens

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.*
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Sizing
import net.minecraft.entity.EntityType
import net.minecraft.util.Identifier
import pebloop.zoocraft.Zoocraft
import pebloop.zoocraft.packets.SetNamePacket


class ZoodexEntityOwoScreen() : BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(Identifier.of("zoocraft", "zoodex_entity_screen"))) {

    override fun build(rootComponent: FlowLayout?) {

    }

}