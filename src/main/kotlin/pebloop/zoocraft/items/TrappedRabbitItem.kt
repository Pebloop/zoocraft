package pebloop.zoocraft.items

import io.wispforest.owo.ui.core.OwoUIAdapter
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.Registries
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerFactory
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import pebloop.zoocraft.blockEntities.EnclosureControllerBlockEntity
import pebloop.zoocraft.screenHandlers.SetNameScreenHandler
import pebloop.zoocraft.screenHandlers.SimpleExtendedScreenHandlerFactory
import java.util.*

class TrappedRabbitItem(settings: Settings) : Item(settings) {
    companion object {

    }


    override fun useOnBlock(context: ItemUsageContext?): ActionResult {
        if (context?.world?.isClient == true) {
            return ActionResult.SUCCESS
        }

        val world = context?.world as? ServerWorld ?: return ActionResult.FAIL
        val pos = context.blockPos
        val rabbit = EntityType.RABBIT.spawn(world, pos.up(), SpawnReason.EVENT)

        val extendedWorld = world as? pebloop.zoocraft.ducks.WorldExtended ?: return ActionResult.CONSUME
        val controllers = extendedWorld.`zoocraft$getEnclosureControllers`()

        for (controllerPos in controllers) {
            val entity = world.getBlockEntity(controllerPos)
            val controller = entity as? pebloop.zoocraft.blockEntities.EnclosureControllerBlockEntity ?: continue
            println(controller.isInside(pos))
            if (controller.isInside(pos)) {
                val blockEntity = world.getBlockEntity(controller.pos) as? pebloop.zoocraft.blockEntities.EnclosureControllerBlockEntity ?: return ActionResult.CONSUME
                blockEntity.addAnimal(rabbit!!)


                context.player?.openHandledScreen(SimpleExtendedScreenHandlerFactory<UUID>({ syncId, inv, player -> SetNameScreenHandler(syncId, inv, rabbit.uuid) }, rabbit.uuid))
                val playerExtended = context.player as? pebloop.zoocraft.ducks.PlayerEntityExtended ?: return ActionResult.CONSUME
                val playerData = playerExtended.getZoodexData()
                val entityData = playerData.firstOrNull { it.type == rabbit.type }
                if (entityData != null) {
                    entityData.zoodexStatus = pebloop.zoocraft.ducks.data.PlayerZooEntityData.ZoodexStatus.CAUGHT
                } else {
                    val rabbitEntity = pebloop.zoocraft.ducks.data.PlayerZooEntityData(rabbit.type)
                    rabbitEntity.zoodexStatus = pebloop.zoocraft.ducks.data.PlayerZooEntityData.ZoodexStatus.CAUGHT
                    playerData.add(rabbitEntity)
                }
                break
            }
        }

        return ActionResult.CONSUME
    }
}