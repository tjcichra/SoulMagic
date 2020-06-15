package com.rainbowluigi.soulmagic.item;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public interface Accessory {
	
	public static AccessoryType AMULET = new AccessoryType(1);
	public static AccessoryType EARRINGS = new AccessoryType(2);
	
	public default void onWearTick(ItemStack stack, World world, PlayerEntity entity, int slotNum) {
		
	}
	
	public default TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		//PlayerAccessories accessories = (PlayerAccessories) player;
		
		//accessories.
		//ItemStack itemStack_2 = player.getEquippedStack(equipmentSlot_1);
		//if (itemStack_2.isEmpty()) {
		//	player.equipStack(equipmentSlot_1, itemStack_1.copy());
		//	itemStack_1.setCount(0);
		//	return new TypedActionResult(ActionResult.SUCCESS, itemStack_1);
		//} else {
			return new TypedActionResult<ItemStack>(ActionResult.FAIL, stack);
		//}
	}
	
	public default AccessoryType getType() {
		return null;
	}

	public default void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, ItemStack stack) {

	}
	
	public static class AccessoryType {
		private int max;
		
		public AccessoryType(int max) {
			this.max = max;
		}
		
		public int getMax() {
			return this.max;
		}
	}
}
