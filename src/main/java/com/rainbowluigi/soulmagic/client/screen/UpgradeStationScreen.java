package com.rainbowluigi.soulmagic.client.screen;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.SoulMagic;
import com.rainbowluigi.soulmagic.inventory.UpgradeStationScreenHandler;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.util.Reference;

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

	private Upgrade selectedUpgrade = null;

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

		ItemStack item = this.handler.slots.get(0).getStack();
		if(item.getItem() instanceof Upgradeable) {
			Upgradeable upgradeable = (Upgradeable) item.getItem();

			List<Upgrade> upgrades = upgradeable.getPossibleUpgrades(item);
			
			ItemRenderer itemRenderer = client.getItemRenderer();

			this.itemRenderer.zOffset = 100.0F;
			RenderSystem.enableRescaleNormal();
			for(Upgrade u : upgrades) {
				itemRenderer.renderGuiItemIcon(u.getIcon(), this.innerXPointToActualXPoint(u.getX() - 8), this.innerYPointToActualYPoint(u.getY() - 8));
			}
			this.itemRenderer.zOffset = 0.0F;
		}
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		boolean b = super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);

		this.innerX = (int) MathHelper.clamp(this.innerX - deltaX, -(this.innerLength / 2), (this.innerLength / 2) - this.innerDisplayLength);
		this.innerY = (int) MathHelper.clamp(this.innerY - deltaY, -(this.innerHeight / 2), (this.innerHeight / 2) - this.innerDisplayHeight);
		return b;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean b = super.mouseClicked(mouseX, mouseY, button);
		return b;
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		//super.drawForeground(matrices, mouseX, mouseY);
		
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int mouseX, int mouseY) {
		super.drawMouseoverTooltip(matrices, mouseX, mouseY);

		//SoulMagic.LOGGER.info(mouseX + " " + mouseY);

		ItemStack item = this.handler.slots.get(0).getStack();
		if(item.getItem() instanceof Upgradeable) {
			Upgradeable upgradeable = (Upgradeable) item.getItem();
			List<Upgrade> upgrades = upgradeable.getPossibleUpgrades(item);

			for(Upgrade u : upgrades) {
				int x = this.innerXPointToActualXPoint(u.getX() - 8);
				int y = this.innerYPointToActualYPoint(u.getY() - 8);

				if(mouseX > x && mouseX <= x + 16 && mouseY > y && mouseY <= y + 16) {
					this.renderTooltip(matrices, u.getName(), mouseX, mouseY);
				}
			}
		}
	}

	public int actualXPointToInnerXPoint(int actualX) {
		return actualX - this.x + this.innerX;
	}

	public int innerXPointToActualXPoint(int innerXPoint) {
		return innerXPoint + this.x - this.innerX;
	}

	public int actualYPointToInnerYPoint(int actualY) {
		return actualY - this.y + this.innerY;
	}

	public int innerYPointToActualYPoint(int innerYPoint) {
		return innerYPoint + this.y - this.innerY;
	}
}