package com.rainbowluigi.soulmagic.client.screen;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.SoulMagic;
import com.rainbowluigi.soulmagic.inventory.UpgradeStationScreenHandler;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.network.UpgradeStationMessage;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

public class UpgradeStationScreen extends HandledScreen<UpgradeStationScreenHandler> {
	
	private static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/container/upgrade_station.png");

	private int innerLength = 500;
	private int innerHeight = 500;
	private int innerDisplayLength = 176;
	private int innerDisplayHeight = 166;

	private int innerX = -innerDisplayLength / 2;
	private int innerY = -innerDisplayHeight / 2;

	private Upgrade selectedUpgrade = null;
	private ButtonWidget unlockButton;

	public UpgradeStationScreen(UpgradeStationScreenHandler container_1, PlayerInventory playerInventory_1, Text text_1) {
		super(container_1, playerInventory_1, text_1);
	}

	@Override
	public void init() {
		super.init();

		this.unlockButton = new ButtonWidget(this.x, this.y + this.backgroundHeight - 20, 40, 20, new TranslatableText("unlock"), (buttonWidgetx) -> {
			if(!this.handler.getSlot(0).getStack().isEmpty()) {
				if(this.selectedUpgrade != null) {
					ItemStack stack = this.handler.getSlot(0).getStack();
					Upgradeable u = (Upgradeable) stack.getItem();
					u.addUpgrade(stack, this.selectedUpgrade, false);

					ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.UPGRADE_STATION, UpgradeStationMessage.makePacket(stack));
				}
			}
		});

		this.addButton(this.unlockButton);
		this.addButton(new ButtonWidget(this.x + this.backgroundWidth - 40, this.y + this.backgroundHeight - 20, 40, 20, new TranslatableText("add select point"), (buttonWidgetx) -> {
			if(!this.handler.getSlot(0).getStack().isEmpty()) {
				ItemStack stack = this.handler.getSlot(0).getStack();
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
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int i = this.x;
		int j = this.y;
		this.drawTexture(matrices, i, j, this.innerX + this.innerLength / 2, this.innerY + this.innerHeight / 2, this.innerDisplayLength, this.innerDisplayHeight);

		ItemStack item = this.handler.slots.get(0).getStack();
		if(item.getItem() instanceof Upgradeable) {
			Upgradeable upgradeable = (Upgradeable) item.getItem();

			this.drawCenteredString(matrices, textRenderer, "" + upgradeable.getSelectorPointsNumber(item), x, y, 0xFFFFFF);

			List<Upgrade> upgrades = upgradeable.getPossibleUpgrades(item);
			
			ItemRenderer itemRenderer = client.getItemRenderer();

			RenderSystem.enableRescaleNormal();
			for(Upgrade u : upgrades) {
				//if(u.equals(ModUpgrades.FLAMING_TOUCH))
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.drawLineToPrev(matrices, u, 2, 0xFFFF00);

				this.itemRenderer.zOffset = 100.0F;
				itemRenderer.renderGuiItemIcon(u.getIcon(), this.innerXPointToActualXPoint(u.getX() - 8), this.innerYPointToActualYPoint(u.getY() - 8));
				this.itemRenderer.zOffset = 0.0F;
			}

			if(this.selectedUpgrade != null && !upgradeable.hasUpgrade(item, this.selectedUpgrade)) {
				for(int r = 0; r < this.selectedUpgrade.getRequirements().length; r++) {
					itemRenderer.renderGuiItemIcon(this.selectedUpgrade.getRequirements()[r], i + 41 + r * 18, j + this.backgroundHeight - 18);
				}
			}
		}
	}

	public void drawLineToPrev(MatrixStack matrices, Upgrade u, int length, int color) {
		Matrix4f matrix = matrices.peek().getModel();
		int x = this.innerXPointToActualXPoint(u.getX());
		int y = this.innerYPointToActualYPoint(u.getY());

		int prevX = this.innerXPointToActualXPoint(u.getPrev() != null ? u.getPrev().getX() : 0);
		int prevY = this.innerYPointToActualYPoint(u.getPrev() != null ? u.getPrev().getY() : 0);

		double deltaX = x - prevX;
		double deltaY = y - prevY;

		double angle = (Math.PI / 2) - Math.atan(deltaY / deltaX);

		double xoffset = length / 2 * Math.sin(angle);
		double yoffset = length / 2 * Math.cos(angle);

		double x1 = x - xoffset, y1 = y + yoffset;
		double x2 = x + xoffset, y2 = y - yoffset;
		double x3 = prevX - xoffset, y3 = prevY + yoffset;
		double x4 = prevX + xoffset, y4 = prevY - yoffset;

		float f = (float)(color >> 24 & 255) / 255.0F;
		float g = (float)(color >> 16 & 255) / 255.0F;
		float h = (float)(color >> 8 & 255) / 255.0F;
		float k = (float)(color & 255) / 255.0F;
		
		//System.out.printf("(%f,%f), (%f,%f), (%f,%f), (%f,%f)\n", x1, y1, x2, y2, x3, y3, x4, y4);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		//RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		//RenderSystem.defaultBlendFunc();
		bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(g, h, k, f).next();
		bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(g, h, k, f).next();
		bufferBuilder.vertex(matrix, (float)x3, (float)y3, 0.0F).color(g, h, k, f).next();
		bufferBuilder.vertex(matrix, (float)x4, (float)y4, 0.0F).color(g, h, k, f).next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
		RenderSystem.enableTexture();
		//RenderSystem.disableBlend();
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

		ItemStack item = this.handler.slots.get(0).getStack();
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