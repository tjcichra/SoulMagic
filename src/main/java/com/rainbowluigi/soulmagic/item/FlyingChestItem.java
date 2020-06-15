package com.rainbowluigi.soulmagic.item;

import java.util.List;

import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FlyingChestItem extends Item implements Accessory {

	private final ModelPart block = new ModelPart(64, 64, 0, 0);
	public static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/accessories/flying_chest.png");
	
	public FlyingChestItem(Settings item$Settings_1) {
		super(item$Settings_1);
		this.block.addCuboid(-15, -11, -4, 8, 8, 8);
	}

	@Override
	public void onWearTick(ItemStack stack, World world, PlayerEntity entity, int slotNum) {
		if(!world.isClient && !entity.isSneaking()) {
			List<Entity> entities = world.getEntities(entity, entity.getBoundingBox().expand(6));

			for(Entity e : entities) {
				if(e instanceof ItemEntity) {
					ItemEntity item = (ItemEntity) e;
					if(!item.cannotPickup()) {
						double x =  entity.getX() - item.getX();
						double y =  (entity.getY() + (entity.getBoundingBox().getYLength()) / 2) - item.getY();
						double z = entity.getZ() - item.getZ();
						
						double vbase = 0.7/(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));
						
						e.setVelocity(vbase * x, vbase * y, vbase * z);
						//ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, SoulMagicClient.ITEM_VACUUM, ItemVacuumMessage.makePacket(item, vbase * x, vbase * y, vbase * z));
					}
				}
				//SoulMagic.LOGGER.info(e);
			}
		}
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, ItemStack stack) {
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
		matrices.push();
		//float n = animationProgress * (float)(-(45 + m * 5));
		//matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(n));
		//float o = 0.75F * m;
		//matrices.scale(o, o, o);
		//matrices.translate(0.0D, (double)(-0.2F + 0.6F * (float)m), 0.0D);
		this.block.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}
}
