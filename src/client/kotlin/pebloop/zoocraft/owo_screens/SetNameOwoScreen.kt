package pebloop.zoocraft.owo_screens

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.component.ButtonComponent
import io.wispforest.owo.ui.component.TextBoxComponent
import io.wispforest.owo.ui.container.FlowLayout
import net.minecraft.util.Identifier
import pebloop.zoocraft.Zoocraft
import pebloop.zoocraft.packets.SetNamePacket


class SetNameOwoScreen() : BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(Identifier.of("zoocraft", "set_name_screen"))) {

    override fun build(rootComponent: FlowLayout?) {
        val button = rootComponent?.childById(ButtonComponent::class.java, "save_name")
        button?.onPress {
            val name = rootComponent.childById(TextBoxComponent::class.java, "name_input")?.text
            val packet = SetNamePacket(name.toString())
            Zoocraft.channel.clientHandle().send(packet)
        }
    }

}