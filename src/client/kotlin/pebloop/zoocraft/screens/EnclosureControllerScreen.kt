package pebloop.zoocraft.screens

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import pebloop.zoocraft.screenHandlers.EnclosureBlockData
import pebloop.zoocraft.screenHandlers.EnclosureControllerScreenHandler


class EnclosureControllerScreen(handler: EnclosureControllerScreenHandler?, inventory: PlayerInventory?, title: Text?) : HandledScreen<EnclosureControllerScreenHandler>(handler, inventory, title) {
    var surface: List<EnclosureBlockData> = listOf()

    init {
        if (handler != null) {
            surface = handler.data.surface
        }
    }

    companion object {
        private val bgWidth = 300
        private val bgHeight = 166
        private val TEXTURE = Identifier.of("zoocraft", "textures/gui/enclosure_controller_interface.png")
    }
    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        val x = (width - bgWidth) / 2
        val y = (height - bgHeight) / 2
        context?.drawTexture(TEXTURE, x, y, 0, 0f, 0f, bgWidth, bgHeight, bgWidth, bgHeight)
    }

    override fun drawForeground(context: DrawContext?, mouseX: Int, mouseY: Int) {
        val x = (width - bgWidth) / 2
        val y = (height - bgHeight) / 2
        context?.drawText(textRenderer, "Surface: " + surface.size.toString(), x + 10, y + 10, 0xFFFFFF , false)

        for (i in 0 until surface.size) {
            val block = surface[i]
            context?.drawText(textRenderer, block.block.toString(), x + 10, y + 20 + i * 10, 0xFFFFFF , false)
        }
    }

    override fun init() {
        super.init()
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }

}