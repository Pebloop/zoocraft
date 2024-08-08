package pebloop.zoocraft.screens

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import pebloop.zoocraft.screenHandlers.EnclosureControllerScreenHandler


class EnclosureControllerScreen(handler: EnclosureControllerScreenHandler?, inventory: PlayerInventory?, title: Text?) : HandledScreen<EnclosureControllerScreenHandler>(handler, inventory, title) {

    companion object {
        private val TEXTURE = Identifier.of("zoocraft", "textures/gui/enclosure_controller_interface.png")
    }
    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexProgram() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        context?.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context, mouseX, mouseY, delta)
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    override fun init() {
        super.init()
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }

}