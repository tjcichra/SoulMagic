package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.SoulCacheContainer;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SoulCacheScreen extends ContainerScreen<SoulCacheContainer> {
	
	public static Identifier rl = new Identifier(Reference.MOD_ID, "textures/gui/container/soul_cache.png");
	public SoulCacheContainer sic;
	
	public SoulCacheScreen(SoulCacheContainer sic, PlayerInventory playerInv, Text text) {
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
		
	}
}