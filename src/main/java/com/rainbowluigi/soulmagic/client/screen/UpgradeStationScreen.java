package com.rainbowluigi.soulmagic.client.screen;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.UpgradeStationScreenHandler;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class UpgradeStationScreen extends HandledScreen<UpgradeStationScreenHandler> {
	
	private static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/container/upgrade_station.png");

	private int innerLength = 500;
	private int innerHeight = 500;
	private int innerDisplayLength = 176;
	private int innerDisplayHeight = 166;

	private int innerX = -innerDisplayLength / 2;
	private int innerY = -innerDisplayHeight / 2;

	public UpgradeStationScreen(UpgradeStationScreenHandler container_1, PlayerInventory playerInventory_1, Text text_1) {
		super(container_1, playerInventory_1, text_1);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int i = this.x;
		int j = this.y;
		this.drawTexture(matrices, i, j, this.innerX + this.innerLength / 2, this.innerY + this.innerHeight / 2, this.innerDisplayLength, this.innerDisplayHeight);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		this.innerX = (int) MathHelper.clamp(this.innerX - deltaX, -(this.innerLength / 2), this.innerLength - this.innerDisplayLength);
		this.innerY = (int) MathHelper.clamp(this.innerY - deltaY, -(this.innerHeight / 2), this.innerHeight - this.innerDisplayHeight);
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		int i = this.x;
		int j = this.y;
		super.drawForeground(matrices, mouseX, mouseY);
		ItemRenderer itemRenderer = client.getItemRenderer();

		ItemStack item = this.handler.slots.get(0).getStack();
		if(item.getItem() instanceof Upgradeable) {
			Upgradeable upgradeable = (Upgradeable) item.getItem();

			List<Upgrade> upgrades = upgradeable.getPossibleUpgrades(item);

			RenderSystem.enableLighting();
			RenderSystem.enableRescaleNormal();
			for(Upgrade u : upgrades) {
				itemRenderer.renderGuiItemIcon(u.getIcon(), i + (u.getX() - this.innerX), j + (u.getY() - this.innerY));
			}
			RenderSystem.disableLighting();
		}
	}
}