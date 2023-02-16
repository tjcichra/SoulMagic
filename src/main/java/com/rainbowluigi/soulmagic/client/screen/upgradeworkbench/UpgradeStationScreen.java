package com.rainbowluigi.soulmagic.client.screen.upgradeworkbench;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.UpgradeStationScreenHandler;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.network.UpgradeStationMessage;
import com.rainbowluigi.soulmagic.network.UpgradeStationTakeItemsMessage;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;
import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class UpgradeStationScreen extends HandledScreen<UpgradeStationScreenHandler> {
	private static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/container/upgrade_station.png");
	private static final Identifier BORDER_TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/container/upgrade_station_border.png");

	protected int backgroundWidth;

	// The total length of the "spacy" area
	private final int innerLength = 1080;
	// The total height of the "spacy" area
	private final int innerHeight = 1080;

	// The length of the window of the "spacy" area
	private final int windowLength = 182;
	// The height of the window of the "spacy" area
	private final int windowHeight = 128;
	// The x position of the "spacy area" relative to the left window border
	private double innerX = (innerLength / 2) - (windowLength / 2);
	// The y position of the "spacy area" relative to the top window border
	private double innerY = (innerHeight / 2) - (windowHeight / 2);

	private boolean movingTab;

	private Upgrade selectedUpgrade = null;

	private final PlayerInventory playerInventory;

	public UpgradeStationScreen(UpgradeStationScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.playerInventory = playerInventory;
		this.backgroundWidth = 204;
	}

	public ItemStack getStack() {
		//return this.handler.getSlot(0).getStack();
		return this.playerInventory.getMainHandStack(); //TODO Remove this
	}

	@Override
	public void init() {
		super.init();
		this.x = (this.width - this.backgroundWidth) / 2;

		//u.setUpgradeSelection(stack, this.selectedUpgrade, !u.hasUpgradeSelected(stack, this.selectedUpgrade));

		this.addDrawableChild(ButtonWidget.builder(Text.translatable("unlock"), this::unlockUpgrade).dimensions(this.x + 10, this.y + this.backgroundHeight - 24, 40, 20).build());
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("add select point"), this::addSelectPoint).dimensions(this.x + this.backgroundWidth - 50, this.y + this.backgroundHeight - 24, 40, 20).build());
	}

	public void unlockUpgrade(ButtonWidget buttonWidgetx) {
		if (!this.getStack().isEmpty() && this.selectedUpgrade != null) {
			ItemStack stack = this.getStack();
			Upgradeable u = (Upgradeable) stack.getItem();

			if (!u.hasUpgradeUnlocked(stack, this.selectedUpgrade)) {
				if (this.selectedUpgrade.getPrev() == null || u.hasUpgradeUnlocked(stack, this.selectedUpgrade.getPrev())) {
					if (tryToTakeItems()) {
						u.addUpgrade(stack, this.selectedUpgrade);
						ClientPlayNetworking.send(ModNetwork.UPGRADE_STATION, UpgradeStationMessage.makePacket(stack));
						if (!this.playerInventory.player.isCreative()) {
							ClientPlayNetworking.send(ModNetwork.UPGRADE_STATION_TAKE_ITEMS, UpgradeStationTakeItemsMessage.makePacket(this.selectedUpgrade.getRequirements()));
						}
					}
				}
			} else {
				if (!u.hasUpgradeSelected(stack, this.selectedUpgrade)) {
					if (u.getUpgradesSelected(stack).size() < u.getSelectorPointsNumber(stack)) {
						if (this.selectedUpgrade.getPrev() == null || u.hasUpgradeSelected(stack, this.selectedUpgrade.getPrev())) {
							u.setUpgradeSelection(stack, this.selectedUpgrade, true);
							u.onSelection(stack, this.client.world, this.selectedUpgrade);
							//u.setUpgradeSelection(stack, this.selectedUpgrade, !u.hasUpgradeSelected(stack, this.selectedUpgrade));
							ClientPlayNetworking.send(ModNetwork.UPGRADE_STATION, UpgradeStationMessage.makePacket(stack));
							this.playerInventory.player.totalExperience -= 1;
						}
					}
				} else {
					boolean isGood = true;

					for (Upgrade currentu : u.getUpgradesSelected(stack)) {
						if (currentu.getPrev() != null && currentu.getPrev().equals(this.selectedUpgrade)) {
							isGood = false;
							break;
						}
					}

					if (isGood) {
						u.setUpgradeSelection(stack, this.selectedUpgrade, false);
						u.onUnselection(stack, this.client.world, this.selectedUpgrade);
						ClientPlayNetworking.send(ModNetwork.UPGRADE_STATION, UpgradeStationMessage.makePacket(stack));
					}
				}
			}
		}
	}

	// IDEA: Item that could add 3 or 5 "select points" to an item. Can only be used once.
	public void addSelectPoint(ButtonWidget buttonWidgetx) {
		if (!this.getStack().isEmpty()) {
			ItemStack stack = this.getStack();
			Upgradeable u = (Upgradeable) stack.getItem();
			u.incrementSelectorPoints(stack);

			ClientPlayNetworking.send(ModNetwork.UPGRADE_STATION, UpgradeStationMessage.makePacket(stack));
		}
	}

	public boolean tryToTakeItems() {
		if(this.playerInventory.player.isCreative()) {
			return true;
		}

		boolean good;

		for(ItemStack requirement : this.selectedUpgrade.getRequirements()) {
			good = false;

			for(ItemStack stack : this.playerInventory.main) {
				if(stack.isItemEqual(requirement) && stack.getCount() >= requirement.getCount()) {
					good = true;
					break;
				}
			}

			if(!good) {
				return false;
			}
		}

		for(ItemStack requirement : this.selectedUpgrade.getRequirements()) {
			for(ItemStack stack : this.playerInventory.main) {
				if(stack.isItemEqual(requirement) && stack.getCount() >= requirement.getCount()) {
					stack.decrement(requirement.getCount());
				}
			}
		}

		return true;
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

		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		RenderSystem.setShaderTexture(0, BORDER_TEXTURE);
		this.drawTexture(matrices, i, j, 0, 0, 204, 166);

		RenderSystem.setShaderTexture(0, TEXTURE);
		drawTexture(matrices, i + 11, j + 11, (float) this.innerX, (float) this.innerY, this.windowLength, this.windowHeight, this.innerLength, this.innerHeight); //CLEAR!

		ItemStack item = this.getStack();
		if(item.getItem() instanceof Upgradeable upgradeable) {
			drawCenteredText(matrices, textRenderer, "" + upgradeable.getSelectorPointsNumber(item), x, y, 0xFFFFFF);

			List<Upgrade> upgrades = upgradeable.getPossibleUpgrades(item);

//			RenderSystem.enableRescaleNormal();
			for(Upgrade u : upgrades) {
				//if(u.equals(ModUpgrades.FLAMING_TOUCH))
				this.drawLineToPrev(matrices, u, upgradeable);
			}

			for(Upgrade u : upgrades) {
				this.drawUpgradeOutline(matrices, u);
				this.drawUpgradeIcon(matrices, u);
			}

			itemRenderer.renderGuiItemIcon(item, this.innerXPointToActualXPoint(innerLength / 2) - 8, this.innerYPointToActualYPoint(innerHeight / 2) - 8);

			if(this.selectedUpgrade != null && !upgradeable.hasUpgradeUnlocked(item, this.selectedUpgrade)) {
				for(int r = 0; r < this.selectedUpgrade.getRequirements().length; r++) {
					itemRenderer.renderGuiItemIcon(this.selectedUpgrade.getRequirements()[r], i + 41 + r * 18, j + this.backgroundHeight - 18);
					itemRenderer.renderGuiItemOverlay(textRenderer, this.selectedUpgrade.getRequirements()[r], i + 41 + r * 18, j + this.backgroundHeight - 18);
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

			RenderSystem.setShaderTexture(0, s.getTexture());
			drawTexture(matrices, x, y, 16, 16, s.getTextureX(), s.getTextureY(), 32, 32, 256, 256);
		}
	}

	public void drawUpgradeOutline(MatrixStack matrices, Upgrade u) {
		UpgradeSprite s = u.getUpgradeSprite();

		int x = this.innerXPointToActualXPoint(u.getX() - s.getLength() / 2);
		int y = this.innerYPointToActualYPoint(u.getY() - s.getHeight() / 2);

		RenderSystem.setShaderTexture(0, s.getTexture());
		this.drawTexture(matrices, x, y, s.getTextureX(), s.getTextureY(), s.getLength(), s.getHeight());
	}

	public void drawLineToPrev(MatrixStack matrices, Upgrade u, Upgradeable upgradeable) {
		Matrix4f matrix = matrices.peek().getPositionMatrix();
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
		

		bb.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
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
		if (button != 0) {
			this.movingTab = false;
			return false;
		}

		if (!this.movingTab) {
			this.movingTab = true;
		} else {
			this.innerX = MathHelper.clamp(this.innerX - deltaX, 0, this.innerLength - this.windowLength); //CLEAR
			this.innerY = MathHelper.clamp(this.innerY - deltaY, 0, this.innerHeight - this.windowHeight); //CLEAR
		}

		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean b = super.mouseClicked(mouseX, mouseY, button);

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
		return (int) (actualX - this.x + this.innerX);
	}

	public int innerXPointToActualXPoint(int innerXPoint) {
		return (int) (innerXPoint + this.x - this.innerX);
	}

	public int actualYPointToInnerYPoint(int actualY) {
		return (int) (actualY - this.y + this.innerY);
	}

	public int innerYPointToActualYPoint(int innerYPoint) {
		return (int) (innerYPoint + this.y - this.innerY);
	}

	private ButtonWidget createButton(Text text, Supplier<Screen> screenSupplier) {
		return ButtonWidget.builder(text, (button) -> {
			this.client.setScreen(screenSupplier.get());
		}).width(98).build();
	}
}