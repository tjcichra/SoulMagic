package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.SoulInfuserScreenHandler;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.util.ColorUtils;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SoulInfuserScreen extends HandledScreen<SoulInfuserScreenHandler> {
	
	private static Identifier rl = new Identifier(Reference.MOD_ID, "textures/gui/container/soul_infuser.png");
	
	public SoulInfuserScreen(SoulInfuserScreenHandler sic, PlayerInventory playerInv, Text text) {
		super(sic, playerInv, text);
		this.backgroundHeight = 211;
		this.field_25270 = this.backgroundHeight - 93;
	}
	
	@Override
	public void init() {
		super.init();
		this.y = (this.height - this.backgroundHeight) / 2;
	}
	
	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		this.drawMouseoverTooltip(matrix, mouseX, mouseY);
	}

	@Override
	public void drawBackground(MatrixStack matrix, float float_1, int int_1, int int_2) {
		//this.guiy = (this.height - this.ySize) / 2;
		
		
		
		RenderSystem.color4f(1, 1, 1, 1);
		
		
		this.client.getTextureManager().bindTexture(rl);
		this.drawTexture(matrix, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
		
		int i = this.getScreenHandler().getCookProgress();
		
		if(i > 0) {
			int color = this.getScreenHandler().getProgressColor();
			RenderSystem.color4f((color >>> 16) / 255f, (color >>> 8 & 255) / 255f, (color & 255) / 255f, 1);
			this.drawTexture(matrix, this.x + 38, this.y - i + 116, 176, 100 - i, 50, i);
			this.drawTexture(matrix, this.x + 88, this.y - i + 116, 176, 100 + 100 - i, 50, i);
		}
		
		ItemStack ish = this.getScreenHandler().getStaffCap();
		int total = 0;

		if(ish != null) {
			SoulEssenceStaff staff = (SoulEssenceStaff) ish.getItem();
			
			for (SoulType type : ModSoulTypes.SOUL_TYPE) {
				if (staff.getSoul(ish, client.world, type) > 0) {
					total += staff.getMaxSoul(ish, client.world, type);
				}
			}
			
			int start = 0;
			if (total > 0) {
				for (SoulType type : ModSoulTypes.SOUL_TYPE) {
					if (staff.getSoul(ish, client.world, type) > 0) {
						float[] colors = ColorUtils.colorToFloats(type.getColor());
						RenderSystem.color4f(colors[0], colors[1], colors[2], 1);
						
						//double j = ((double) SoulStaffHelper.getSoul(ish, type) / total) * 77;
						int j = (int) (((double) staff.getSoul(ish, client.world, type) / total) * 77 + 0.5);
						
						//this.blit(this.x + 12, (int) (this.y - j + 94 - start), 226, (int) (77 - j - start), 8, (int) j);
						this.drawTexture(matrix, this.x + 12, this.y - j + 94 - start, 226, 77 - j - start, 8, j);
						
						start += j;
					}
				}
			}
		}
	}
	
	@Override
	public boolean isClickOutsideBounds(double double_1, double double_2, int int_1, int int_2, int int_3) {
		return double_1 < (double) int_1 || double_2 < (double) int_2 || double_1 >= (double) (int_1 + this.backgroundWidth) || double_2 >= (double) (int_2 + this.backgroundHeight);
	}
}