package com.rainbowluigi.soulmagic.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	
	/*@Redirect(method = "renderGuiItemOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamaged()Z"))
	private boolean isDamaged(ItemStack stack) {
		if(stack.getItem() instanceof CustomDamageDisplayer) {
			return ((CustomDamageDisplayer) stack.getItem()).showBar(stack);
		}
		return stack.isDamaged();
	}
	
	@Redirect(method = "renderGuiItemOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getDamage()I"))
	private int getDamage(ItemStack stack) {
		if(stack.getItem() instanceof CustomDamageDisplayer) {
			return (int) (((CustomDamageDisplayer) stack.getItem()).currentBar(stack) * 100);
		}
		return stack.getDamage();
	}
	
	@Redirect(method = "renderGuiItemOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxDamage()I"))
	private int getMaxDamage(ItemStack stack) {
		if(stack.getItem() instanceof CustomDamageDisplayer) {
			return 100;
		}
		return stack.getMaxDamage();
	}*/
	
	@Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("RETURN"))
	public void renderGuiItemOverlay(TextRenderer textRenderer, ItemStack stack, int x, int y, String s, CallbackInfo cbi) {
		if (stack.getItem() instanceof SoulEssenceStaff) {
			MinecraftClient client = MinecraftClient.getInstance();
			SoulEssenceStaff staff = (SoulEssenceStaff) stack.getItem();
			
            int total = 0;
            for (SoulType type : ModSoulTypes.SOUL_TYPE) {
				if (staff.getSoul(stack, client.world, type) > 0) {
					total += staff.getMaxSoul(stack, client.world, type);
				}
			}
            
            if (total > 0) {
            	RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableAlphaTest();
                RenderSystem.disableBlend();
                
                Tessellator tessellator_1 = Tessellator.getInstance();
                BufferBuilder bufferBuilder_1 = tessellator_1.getBuffer();
                
            	this.renderGuiQuad(bufferBuilder_1, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
            	
            	double start = 0;
				for (SoulType type : ModSoulTypes.SOUL_TYPE) {
					if (staff.getSoul(stack, client.world, type) > 0) {
						int color = type.getColor();
						
						double j = (double) staff.getSoul(stack, client.world, type) / total * 13;
						this.renderGuiQuad(bufferBuilder_1, x + 2 + start, y + 13, j, 1, color >> 16 & 255, color >> 8 & 255, color & 255, 255);
						
						start += j;
					}
				}
				
				RenderSystem.enableBlend();
				RenderSystem.enableAlphaTest();
	            RenderSystem.enableTexture();
	            RenderSystem.enableDepthTest();
			}
         }
	}
	
	private void renderGuiQuad(BufferBuilder bufferBuilder_1, double int_1, double int_2, double int_3, double int_4, int int_5, int int_6, int int_7, int int_8) {
		bufferBuilder_1.begin(7, VertexFormats.POSITION_COLOR);
		bufferBuilder_1.vertex(int_1, int_2, 0.0D).color(int_5, int_6, int_7, int_8).next();
		bufferBuilder_1.vertex(int_1, int_2 + int_4, 0.0D).color(int_5, int_6, int_7, int_8).next();
		bufferBuilder_1.vertex(int_1 + int_3, int_2 + int_4, 0.0D).color(int_5, int_6, int_7, int_8).next();
		bufferBuilder_1.vertex(int_1 + int_3, int_2, 0.0D).color(int_5, int_6, int_7, int_8).next();
		Tessellator.getInstance().draw();
	}
}
