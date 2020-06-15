package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.AccessoryContainer;
import com.rainbowluigi.soulmagic.tabs.ModTabs;
import com.rainbowluigi.soulmagic.tabs.TabHelper;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AccessoryScreen extends AbstractInventoryScreen<AccessoryContainer> {

	public static final Identifier BACKGROUND_TEXTURE = new Identifier("soulmagic", "textures/gui/container/accessory_screen.png");

	public AccessoryScreen(AccessoryContainer container, PlayerEntity playerInventory_1, Text text_1) {
		super(container, playerInventory_1.inventory, text_1);
		this.passEvents = false;
	}

	@Override
	protected void drawForeground(MatrixStack matrix, int int_1, int int_2) {
		//this.font.draw(this.title.asFormattedString(), 97.0F, 8.0F, 4210752);
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float float_1) {
		this.renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, float_1);
		
		TabHelper.tabsRender(ModTabs.ACCESSORIES, matrix, this, x, y, mouseX, mouseY);
		this.drawMouseoverTooltip(matrix, mouseX, mouseY);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrix, float float_1, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.client.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		this.drawTexture(matrix, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
		InventoryScreen.drawEntity(this.x + 32, this.y + 75, 30, (float) (this.x + 32) - mouseX, (float) (this.y + 75 - 50) - mouseY, this.client.player);

		TabHelper.drawTabsBackground(ModTabs.ACCESSORIES, matrix, this, this.x, this.y);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int int_1) {
		TabHelper.tabsMouseReleased(ModTabs.ACCESSORIES, this, this.x, this.y, mouseX, mouseY);
		return super.mouseReleased(mouseX, mouseY, int_1);
	}
}
