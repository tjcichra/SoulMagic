package com.rainbowluigi.soulmagic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.block.entity.SoulInfuserBlockEntity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(BuiltinModelItemRenderer.class)
public class ItemDynamicRendererMixin {
	
	private final SoulInfuserBlockEntity renderSoulInfuser = new SoulInfuserBlockEntity();
	
	@Inject(at = @At(value = "INVOKE"), method = "render", cancellable = true)
	public void renderSoulMagic(ItemStack stack, MatrixStack matrixStack_1, VertexConsumerProvider vertexConsumerProvider_1, int int_1, int int_2, CallbackInfo cb) {
		if (stack.getItem() == Item.fromBlock(ModBlocks.SOUL_INFUSER)) {
			BlockEntityRenderDispatcher.INSTANCE.renderEntity(this.renderSoulInfuser, matrixStack_1, vertexConsumerProvider_1, int_1, int_2);
			cb.cancel();
		}
	}
}
