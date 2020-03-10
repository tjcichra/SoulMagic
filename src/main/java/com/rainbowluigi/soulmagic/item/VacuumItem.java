package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;

import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class VacuumItem extends Item implements Accessory {

	public VacuumItem(Settings item$Settings_1) {
		super(item$Settings_1);
	}

	/*@Override
	public void onWearTick(ItemStack stack, World world, PlayerEntity entity, int slotNum) {
		if(!world.isClient && !entity.isSneaking()) {
			List<Entity> entities = world.getEntities(entity, entity.getBoundingBox().expand(6.0D, 6D, 6.0D));
			for(Entity e : entities) {
				if(e instanceof ItemEntity) {
					ItemEntity item = (ItemEntity) e;
					if(!item.cannotPickup()) {
						double x =  entity.x - item.x;
						double y =  (entity.y + (entity.getBoundingBox().getYSize()) / 2) - item.y;
						double z = entity.z - item.z;
						
						double vbase = 0.7/(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));
						
						e.setVelocity(vbase * x, vbase * y, vbase * z);
						ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, SoulMagicClient.ITEM_VACUUM, ItemVacuumMessage.makePacket(item, vbase * x, vbase * y, vbase * z));
					}
				}
				//SoulMagic.LOGGER.info(e);
			}
		}
	}*/
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if(!world.isClient) {
			ContainerProviderImpl.INSTANCE.openContainer(ModContainerFactories.PERSONAL_CHEST, player, buf -> {
				buf.writeItemStack(stack);
			});
		}
		return new TypedActionResult<ItemStack>(ActionResult.PASS, stack);
	}
}
