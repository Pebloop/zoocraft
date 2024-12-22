package pebloop.zoocraft.items

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.passive.RabbitEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.Registries
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World
import pebloop.zoocraft.blockEntities.EnclosureControllerBlockEntity
import pebloop.zoocraft.ducks.WorldExtended
import pebloop.zoocraft.screenHandlers.SetNameScreenHandler
import pebloop.zoocraft.screenHandlers.SimpleExtendedScreenHandlerFactory
import pebloop.zoocraft.screenHandlers.enclosureController.EnclosureEntityData
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexData
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexEntityScreenData
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexEntityScreenHandler
import pebloop.zoocraft.screenHandlers.zoodex.ZoodexScreenHandler
import java.util.*
import kotlin.collections.ArrayList

class ZoodexItem(settings: Settings) : Item(settings) {
    companion object {

    }

    override fun useOnBlock(context: ItemUsageContext?): ActionResult {
        val zoodexData = ZoodexData(ArrayList())
        context?.player?.openHandledScreen(SimpleExtendedScreenHandlerFactory<ZoodexData>({ syncId, inv, player -> ZoodexScreenHandler(syncId, inv,zoodexData) }, zoodexData))
        return super.useOnBlock(context)
    }

    override fun useOnEntity(stack: ItemStack?, user: PlayerEntity?, entity: LivingEntity?, hand: Hand?): ActionResult {
        if (entity != null) {
            val world = entity.world
            val worldExtended = entity.world as WorldExtended
            val controllersPos = worldExtended.`zoocraft$getEnclosureControllers`()
            val controllers = controllersPos.map { world.getBlockEntity(it) }.filterIsInstance<EnclosureControllerBlockEntity>()
            val controller = controllers.firstOrNull { it.entities.contains(entity.uuid) }

            user?.openHandledScreen(SimpleExtendedScreenHandlerFactory<ZoodexEntityScreenData>({ syncId, inv, player -> ZoodexEntityScreenHandler(syncId, inv, ZoodexEntityScreenData(entity.uuid, controller?.pos)) }, ZoodexEntityScreenData(entity.uuid, controller?.pos)))
        }
        return super.useOnEntity(stack, user, entity, hand)
    }

    override fun getUseAction(stack: ItemStack?): UseAction {
        return super.getUseAction(stack)
    }
}