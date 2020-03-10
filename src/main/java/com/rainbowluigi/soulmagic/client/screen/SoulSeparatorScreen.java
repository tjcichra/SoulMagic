package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.SoulSeparatorContainer;
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
public class SoulSeparatorScreen extends ContainerScreen<SoulSeparatorContainer> {
	
	public static Identifier rl = new Identifier(Reference.MOD_ID, "textures/gui/container/soul_essence_separator.png");
	public SoulSeparatorContainer sic;
	
	public SoulSeparatorScreen(SoulSeparatorContainer sic, PlayerInventory playerInv, Text text) {
		super(sic, playerInv, text);
		this.sic = sic;
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
		
		int i = this.sic.getCookProgress();
		
		if (i > 0) {
			RenderSystem.color4f(1, 1, 1, 1);
			this.blit(this.x + 67, this.y + 35, 176, 14, i, 17);
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
						int j = (int) (((double) staff.getSoul(ish, client.world, type) / total) * 58 + 0.5);
						//this.blit(this.x + 12, (int) (this.y - j + 94 - start), 226, (int) (77 - j - start), 8, (int) j);
						this.blit(this.x + 136, this.y - j + 72 - start, 176, 89 - j - start, 8, j);
						
						start += j;
					}
				}
			}
		}
		
	}
}