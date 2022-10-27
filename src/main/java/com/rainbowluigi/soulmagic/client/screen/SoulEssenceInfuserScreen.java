package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.SoulInfuserScreenHandler;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SoulEssenceInfuserScreen extends HandledScreen<SoulInfuserScreenHandler> {
	private static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/container/soul_infuser.png");
	
	public SoulEssenceInfuserScreen(SoulInfuserScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundHeight = 211;
		this.playerInventoryTitleY = this.backgroundHeight - 93;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

		int cookProgress = this.getScreenHandler().getCookProgress();
//		System.out.println("cookProgress: " + cookProgress);
//
//		if(i > 0) {
//			int color = this.getScreenHandler().getProgressColor();
//			RenderSystem.setShaderColor((color >>> 16) / 255f, (color >>> 8 & 255) / 255f, (color & 255) / 255f, 1);
//			this.drawTexture(matrices, this.x + 38, this.y - i + 116, 176, 100 - i, 50, i);
//			this.drawTexture(matrices, this.x + 88, this.y - i + 116, 176, 100 + 100 - i, 50, i);
//		}
		/*
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
		}*/
	}
	
	@Override
	public boolean isClickOutsideBounds(double double_1, double double_2, int int_1, int int_2, int int_3) {
		return double_1 < (double) int_1 || double_2 < (double) int_2 || double_1 >= (double) (int_1 + this.backgroundWidth) || double_2 >= (double) (int_2 + this.backgroundHeight);
	}
}