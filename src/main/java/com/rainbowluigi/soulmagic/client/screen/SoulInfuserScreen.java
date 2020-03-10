package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.SoulInfuserContainer;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.util.ColorUtils;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SoulInfuserScreen extends ContainerScreen<SoulInfuserContainer> {
	
	public static Identifier rl = new Identifier(Reference.MOD_ID, "textures/gui/container/soul_infuser.png");
	protected int containerHeight = 211;
	
	public SoulInfuserScreen(SoulInfuserContainer sic, PlayerInventory playerInv, Text text) {
		super(sic, playerInv, text);
	}
	
	@Override
	public void init() {
		super.init();
		this.y = (this.height - this.containerHeight) / 2;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();

		super.render(mouseX, mouseY, partialTicks);
		this.drawMouseoverTooltip(mouseX, mouseY);
	}
	
	@Override
	public void drawForeground(int mouseX, int mouseY) {
		String s = this.title.asFormattedString();
		this.textRenderer.draw(s, (float) (this.containerWidth - this.textRenderer.getStringWidth(s)) / 2, 6, 4210752);
		this.textRenderer.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float)(this.containerHeight - 96 + 3), 4210752);
	}

	@Override
	public void drawBackground(float partialTicks, int mouseX, int mouseY) {
		//this.guiy = (this.height - this.ySize) / 2;
		
		
		
		RenderSystem.color4f(1, 1, 1, 1);
		
		
		this.client.getTextureManager().bindTexture(rl);
		this.blit(this.x, this.y, 0, 0, this.containerWidth, this.containerHeight);
		
		int i = this.getContainer().getCookProgress();
		
		if(i > 0) {
			
			//Set<SoulType> stset = this.getContainer().getRecipeSoulMap().keySet();
			//float[] colors = new float[s.size()];
			//
			//for(int j = 0; j < stset.size(); j++) {
			//	color[j] = stset.
			//}
			//ColorUtils.colorsToFloats(this.field_147002_h.getRecipeSoulMap().keySet().toArray());
			//System.out.println(i);
			
			//state = state.with(SoulFlameBlock.RED, (int) ((color >>> 16) / 255f * 7 + 0.5));
			//state = state.with(SoulFlameBlock.GREEN, (int) ((color >>> 8 & 255) / 255f * 7 + 0.5));
			//state = state.with(SoulFlameBlock.BLUE, (int) ((color & 255) / 255f * 7 + 0.5));
			int color = this.getContainer().getProgressColor();
			
			RenderSystem.color4f((color >>> 16) / 255f, (color >>> 8 & 255) / 255f, (color & 255) / 255f, 1);
			this.blit(this.x + 38, this.y - i + 117, 176, 101 - i, 50, i);
			this.blit(this.x + 88, this.y - i + 117, 176, 101 - i + 101, 50, i);
		}
		
		ItemStack ish = this.container.getStaffCap();
		int total = 0;

		if(ish != null) {
			SoulEssenceStaff staff = (SoulEssenceStaff) ish.getItem();
			
			for (SoulType type : ModSoulTypes.SOUL_TYPE_REG) {
				if (staff.getSoul(ish, client.world, type) > 0) {
					total += staff.getMaxSoul(ish, client.world, type);
				}
			}
			
			int start = 0;
			if (total > 0) {
				for (SoulType type : ModSoulTypes.SOUL_TYPE_REG) {
					if (staff.getSoul(ish, client.world, type) > 0) {
						float[] colors = ColorUtils.colorToFloats(type.getColor());
						RenderSystem.color4f(colors[0], colors[1], colors[2], 1);
						
						//double j = ((double) SoulStaffHelper.getSoul(ish, type) / total) * 77;
						int j = (int) (((double) staff.getSoul(ish, client.world, type) / total) * 77 + 0.5);
						
						//this.blit(this.x + 12, (int) (this.y - j + 94 - start), 226, (int) (77 - j - start), 8, (int) j);
						this.blit(this.x + 12, this.y - j + 94 - start, 226, 77 - j - start, 8, j);
						
						start += j;
					}
				}
			}
		}
	}
	
	@Override
	public boolean isClickOutsideBounds(double double_1, double double_2, int int_1, int int_2, int int_3) {
		return double_1 < (double) int_1 || double_2 < (double) int_2 || double_1 >= (double) (int_1 + this.containerWidth) || double_2 >= (double) (int_2 + this.containerHeight);
	}
}