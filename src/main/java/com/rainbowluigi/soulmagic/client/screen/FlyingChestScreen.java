package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.FlyingChestScreenHandler;
import com.rainbowluigi.soulmagic.tabs.ModTabs;
import com.rainbowluigi.soulmagic.tabs.TabHelper;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FlyingChestScreen extends HandledScreen<FlyingChestScreenHandler> {
	
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/shulker_box.png");
	
	public FlyingChestScreen(FlyingChestScreenHandler container_1, PlayerInventory playerInventory_1, Text text_1) {
		super(container_1, playerInventory_1, text_1);
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float float_1) {
		this.renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, float_1);
		TabHelper.tabsRender(ModTabs.FLYING_CHEST, matrix, this, x, y, mouseX, mouseY);
		this.drawMouseoverTooltip(matrix, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrix, float float_1, int int_1, int int_2) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int int_3 = (this.width - this.backgroundWidth) / 2;
		int int_4 = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrix, int_3, int_4, 0, 0, this.backgroundWidth, this.backgroundHeight);

		TabHelper.drawTabsBackground(ModTabs.FLYING_CHEST, matrix, this, this.x, this.y);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int int_1) {
		TabHelper.tabsMouseReleased(ModTabs.FLYING_CHEST, this, this.x, this.y, mouseX, mouseY);
		return super.mouseReleased(mouseX, mouseY, int_1);
	}
}
