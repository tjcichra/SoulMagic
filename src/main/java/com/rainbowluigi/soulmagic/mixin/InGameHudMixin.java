package com.rainbowluigi.soulmagic.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaffDisplayer;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.util.ItemHelper;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {

	@Shadow @Final private MinecraftClient client;
	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;
	
	private static int timer = 0;
	
	private static Identifier SOUL_STAFF_TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/soul_essence_staff.png");
	
	@Inject(method = "render", at = @At(value = "INVOKE", target = "renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V"), cancellable = true)
	public void render(MatrixStack matrix, float f, CallbackInfo info) {
		//if(timer < 30) {
		if(this.getHoldingSoulItem() != null) {
			if(timer < 30) {
				timer++;
			}
		} else if(timer > 0){
			timer--;
		}
		
		
		if(timer > 0) {
			
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.client.getTextureManager().bindTexture(SOUL_STAFF_TEXTURE);
			int x = (int) (0.025*timer*timer-1.75*timer+this.scaledWidth+5);
			this.drawTexture(matrix, x, this.scaledHeight / 2 - 40, 0, 0, 22, 81);
			
			if(this.getHoldingSoulItem() != null) {
				ItemStack stack = this.getHoldingSoulItem();
				SoulEssenceStaffDisplayer sesd = (SoulEssenceStaffDisplayer) stack.getItem();
				
				if(sesd.getSoulTypesToShow(stack, this.client.player) != null) {
					SoulType soulTypes[] = sesd.getSoulTypesToShow(stack, this.client.player);
					
					
					
					ItemStack ish = ItemHelper.findItem(this.client.player, ModItems.SOUL_ESSENCE_STAFF);
					
					if(ish != null) {
						SoulEssenceStaff staff = (SoulEssenceStaff) ish.getItem();
						
						int total = 0;
						for (SoulType type : soulTypes) {
							if (staff.getSoul(ish, client.world, type) > 0) {
								total += staff.getMaxSoul(ish, client.world, type);
							}
						}
						
						int start = 0;
						if (total > 0) {
							for (SoulType type : soulTypes) {
								if (staff.getSoul(ish, client.world, type) > 0) {
									
									int color = type.getColor();
									RenderSystem.color4f((color >>> 16) / 255f, (color >>> 8 & 255) / 255f, (color & 255) / 255f, 1);
									
									int j = (int) (((double) staff.getSoul(ish, client.world, type) / total) * 71 + 0.5);
									//this.blit(this.scaledWidth - 18, this.scaledHeight - 5 - j - start, 22, 71 - j - start, 14, j);
									this.drawTexture(matrix, x + 4, this.scaledHeight / 2 + 36 - j - start, 22, 71 - j - start, 14, j);
									
									if(timer >= 30 && this.client.player.isSneaking()) {
										this.drawCenteredText(matrix, this.client.textRenderer, new LiteralText("" + staff.getSoul(ish, client.world, type)), this.scaledWidth-40, this.scaledHeight / 2 + 36 - j / 2 - start, type.getColor());
										this.client.getTextureManager().bindTexture(SOUL_STAFF_TEXTURE);
									}
									
									start = j;
								}
							}
						}
					}
				}
			}
		}
	}
	
	private ItemStack getHoldingSoulItem() {
		for(ItemStack stack : client.player.getItemsEquipped()) {
			if(stack.getItem() instanceof SoulEssenceStaffDisplayer && ((SoulEssenceStaffDisplayer) stack.getItem()).showDisplay(stack, client.player)) {
				return stack;
			}
		}
		return null;
	}
}
