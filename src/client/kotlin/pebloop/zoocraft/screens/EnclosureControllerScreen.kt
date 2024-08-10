package pebloop.zoocraft.screens

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.block.Block
import net.minecraft.client.MinecraftClient
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
        context?.drawText(textRenderer, "Surface: " + handler.data.surface.size.toString(), x + 10, y + 10, 0xFFFFFF , false)
        val blocksk = mutableMapOf<Block, Int>()

        for (i in 0 until handler.data.surface.size) {
            val block = handler.data.surface[i]
            if (blocksk.containsKey(block.block)) {
                blocksk[block.block] = blocksk[block.block]!! + 1
            } else {
                blocksk[block.block] = 1
            }
        }
        var i = 0
        for ((key, value) in blocksk) {
            val text = key.name.append(": ").append(value.toString())
            context?.drawText(textRenderer, text, x + 10, y + 20 + i * 10, 0xFFFFFF, false)
            i++
        }
    }

    override fun init() {
        super.init()
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }

}