package com.rainbowluigi.soulmagic.client.screen;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.SoulMagic;
import com.rainbowluigi.soulmagic.inventory.UpgradeStationScreenHandler;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.network.UpgradeStationMessage;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

public class UpgradeStationScreen extends HandledScreen<UpgradeStationScreenHandler> {
	
	private static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/container/upgrade_station.png");
	private static final Identifier BORDER_TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/container/upgrade_station_border.png");

	protected int backgroundWidth = 204;

	private int innerLength = 500;
	private int innerHeight = 500;
	private int innerDisplayLength = 182;
	private int innerDisplayHeight = 128;

	private int innerX = -innerDisplayLength / 2;
	private int innerY = -innerDisplayHeight / 2;

	private Upgrade selectedUpgrade = null;
	private ButtonWidget unlockButton;

	public UpgradeStationScreen(UpgradeStationScreenHandler container_1, PlayerInventory playerInventory_1, Text text_1) {
		super(container_1, playerInventory_1, text_1);
	}

	public ItemStack getStack() {
		//return this.handler.getSlot(0).getStack();
		return this.playerInventory.getMainHandStack();
	}

	@Override
	public void init() {
		super.init();
		this.x = (this.width - this.backgroundWidth) / 2;
		this.y = (this.height - this.backgroundHeight) / 2;

		this.unlockButton = new ButtonWidget(this.x + 10, this.y + this.backgroundHeight - 24, 40, 20, new TranslatableText("unlock"), (buttonWidgetx) -> {
			if(!this.getStack().isEmpty() && this.selectedUpgrade != null) {
				ItemStack stack = this.getStack();
				Upgradeable u = (Upgradeable) stack.getItem();

				if(!u.hasUpgradeUnlocked(stack, this.selectedUpgrade)) {
					if(this.selectedUpgrade.getPrev() == null || u.hasUpgradeUnlocked(stack, this.selectedUpgrade.getPrev())) {
						u.addUpgrade(stack, this.selectedUpgrade);
						ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.UPGRADE_STATION, UpgradeStationMessage.makePacket(stack));
					}
				} else {
					if(!u.hasUpgradeSelected(stack, this.selectedUpgrade)) {
						if(u.getUpgradesSelected(stack).size() < u.getSelectorPointsNumber(stack)) {
							if(this.selectedUpgrade.getPrev() == null || u.hasUpgradeSelected(stack, this.selectedUpgrade.getPrev())) {
								u.setUpgradeSelection(stack, this.selectedUpgrade, true);
								u.onSelection(stack, this.client.world, this.selectedUpgrade);
								//u.setUpgradeSelection(stack, this.selectedUpgrade, !u.hasUpgradeSelected(stack, this.selectedUpgrade));
								ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.UPGRADE_STATION, UpgradeStationMessage.makePacket(stack));
							}
						}
					} else {
						boolean isGood = true;

						for(Upgrade currentu : u.getUpgradesSelected(stack)) {
							if(isGood && currentu.getPrev() != null && currentu.getPrev().equals(this.selectedUpgrade)) {
								isGood = false;
							}
						}

						if(isGood) {
							u.setUpgradeSelection(stack, this.selectedUpgrade, false);
							u.onUnselection(stack, this.client.world, this.selectedUpgrade);
							ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.UPGRADE_STATION, UpgradeStationMessage.makePacket(stack));
						}
					}
				}
			}
		});

		this.addButton(this.unlockButton);
		this.addButton(new ButtonWidget(this.x + this.backgroundWidth - 50, this.y + this.backgroundHeight - 24, 40, 20, new TranslatableText("add select point"), (buttonWidgetx) -> {
			if(!this.getStack().isEmpty()) {
				ItemStack stack = this.getStack();
				Upgradeable u = (Upgradeable) stack.getItem();
				u.incrementSelectorPoints(stack);

				ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.UPGRADE_STATION, UpgradeStationMessage.makePacket(stack));
			}
		}));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		int i = this.x;
		int j = this.y;

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(BORDER_TEXTURE);
		this.drawTexture(matrices, i, j, 0, 0, 204, 166);
		this.client.getTextureManager().bindTexture(TEXTURE);
		
		this.drawTexture(matrices, i + 11, j + 11, this.innerX + this.innerLength / 2, this.innerY + this.innerHeight / 2, this.innerDisplayLength, this.innerDisplayHeight);

		

		ItemStack item = this.getStack();
		if(item.getItem() instanceof Upgradeable) {
			Upgradeable upgradeable = (Upgradeable) item.getItem();

			this.drawCenteredString(matrices, textRenderer, "" + upgradeable.getSelectorPointsNumber(item), x, y, 0xFFFFFF);

			List<Upgrade> upgrades = upgradeable.getPossibleUpgrades(item);

			RenderSystem.enableRescaleNormal();
			for(Upgrade u : upgrades) {
				//if(u.equals(ModUpgrades.FLAMING_TOUCH))
				this.drawLineToPrev(matrices, u, upgradeable);
			}

			for(Upgrade u : upgrades) {
				this.drawUpgradeOutline(matrices, u);
				this.drawUpgradeIcon(matrices, u);
			}

			itemRenderer.renderGuiItemIcon(item, this.innerXPointToActualXPoint(0) - 8, this.innerYPointToActualYPoint(0) - 8);

			if(this.selectedUpgrade != null && !upgradeable.hasUpgradeUnlocked(item, this.selectedUpgrade)) {
				for(int r = 0; r < this.selectedUpgrade.getRequirements().length; r++) {
					itemRenderer.renderGuiItemIcon(this.selectedUpgrade.getRequirements()[r], i + 41 + r * 18, j + this.backgroundHeight - 18);
				}
			}
		}
	}

	public void drawUpgradeIcon(MatrixStack matrices, Upgrade u) {
		UpgradeSprite s = u.getIcon();
		UpgradeSprite s2 = u.getUpgradeSprite();

		if(s != null) {
			int x = this.innerXPointToActualXPoint(u.getX() - 8);
			int y = this.innerYPointToActualYPoint(u.getY() - 8);

			this.client.getTextureManager().bindTexture(s.getTexture());
			drawTexture(matrices, x, y, 16, 16, s.getTextureX(), s.getTextureY(), 32, 32, 256, 256);
		}
	}

	public void drawUpgradeOutline(MatrixStack matrices, Upgrade u) {
		UpgradeSprite s = u.getUpgradeSprite();

		int x = this.innerXPointToActualXPoint(u.getX() - s.getLength() / 2);
		int y = this.innerYPointToActualYPoint(u.getY() - s.getHeight() / 2);

		this.client.getTextureManager().bindTexture(s.getTexture());
		this.drawTexture(matrices, x, y, s.getTextureX(), s.getTextureY(), s.getLength(), s.getHeight());
	}

	public void drawLineToPrev(MatrixStack matrices, Upgrade u, Upgradeable upgradeable) {
		Matrix4f matrix = matrices.peek().getModel();
		int x = this.innerXPointToActualXPoint(u.getX());
		int y = this.innerYPointToActualYPoint(u.getY());

		int prevX = this.innerXPointToActualXPoint(u.getPrev() != null ? u.getPrev().getX() : 0);
		int prevY = this.innerYPointToActualYPoint(u.getPrev() != null ? u.getPrev().getY() : 0);

		int color = this.getColor(u, upgradeable);

		float f = (float)(color >> 24 & 255) / 255.0F;
		float g = (float)(color >> 16 & 255) / 255.0F;
		float h = (float)(color >> 8 & 255) / 255.0F;
		float k = (float)(color & 255) / 255.0F;
		
		//System.out.printf("(%f,%f), (%f,%f), (%f,%f), (%f,%f)\n", x1, y1, x2, y2, x3, y3, x4, y4);
		BufferBuilder bb = Tessellator.getInstance().getBuffer();

		//RenderSystem.pushMatrix();
		
		/*RenderSystem.depthMask(true);
		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();*/
		RenderSystem.disableTexture();
		

		bb.begin(1, VertexFormats.POSITION_COLOR);
		RenderSystem.lineWidth(6f);
		bb.vertex(matrix, (float)prevX, (float)prevY, 0.0F).color(g, h, k, f).next();
		bb.vertex(matrix, (float)x, (float)y, 0.0F).color(g, h, k, f).next();
		Tessellator.getInstance().draw();

		/*RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
		RenderSystem.enableCull();*/
		RenderSystem.enableTexture();

		//RenderSystem.popMatrix();
	}

	public int getColor(Upgrade u, Upgradeable upgradeable) {
		ItemStack stack = this.getStack();

		if(upgradeable.getUpgradesSelected(stack).contains(u)) {
			float brightness = (MathHelper.sin(System.currentTimeMillis() % 1000 / 1000f * 2 * 3.141592f) / 4) + 0.75f;
			return Color.HSBtoRGB(0.33333f, 1, brightness);
			//return 0x00FF00;
		} else if(upgradeable.getUpgradesUnlocked(stack, true).contains(u)) {
			float brightness = (MathHelper.sin(System.currentTimeMillis() % 1000 / 1000f * 2 * 3.141592f) / 4) + 0.75f;
			return Color.HSBtoRGB(0, 0, brightness);
		} else {
			return 0x333333;
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

		SoulMagic.LOGGER.info(this.actualXPointToInnerXPoint((int) mouseX) + " " + this.actualYPointToInnerYPoint((int) mouseY));

		ItemStack item = this.getStack();
		if(item.getItem() instanceof Upgradeable) {
			Upgradeable upgradeable = (Upgradeable) item.getItem();
			List<Upgrade> upgrades = upgradeable.getPossibleUpgrades(item);

			for(Upgrade u : upgrades) {
				int x = this.innerXPointToActualXPoint(u.getX() - 8);
				int y = this.innerYPointToActualYPoint(u.getY() - 8);

				if(mouseX > x && mouseX <= x + 16 && mouseY > y && mouseY <= y + 16) {
					this.selectedUpgrade = u;
				}
			}
		}

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

		ItemStack item = this.getStack();
		if(item.getItem() instanceof Upgradeable) {
			Upgradeable upgradeable = (Upgradeable) item.getItem();
			List<Upgrade> upgrades = upgradeable.getPossibleUpgrades(item);

			for(Upgrade u : upgrades) {
				int x = this.innerXPointToActualXPoint(u.getX() - 8);
				int y = this.innerYPointToActualYPoint(u.getY() - 8);

				if(mouseX > x && mouseX <= x + 16 && mouseY > y && mouseY <= y + 16) {
					this.renderTooltip(matrices, Arrays.asList(u.getName(), u.getDesc()), mouseX, mouseY);
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