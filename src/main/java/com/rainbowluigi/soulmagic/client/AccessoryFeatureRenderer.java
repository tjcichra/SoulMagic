package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.inventory.AccessoriesInventory;
import com.rainbowluigi.soulmagic.item.crafting.PlayerAccessories;
import com.rainbowluigi.soulmagic.item.Accessory;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class AccessoryFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	
	public AccessoryFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int idkwhatthisis, AbstractClientPlayerEntity player, float f, float g, float h, float j, float k, float l) {
		AccessoriesInventory accessories = ((PlayerAccessories) (Object) player).getAccessories();
		for(int i = 0; i < accessories.size(); i++) {
			ItemStack stack = accessories.getStack(i);
			((Accessory) stack.getItem()).render(stack, player);
		}
	}
}