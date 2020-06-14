package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.AccessoryContainer;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class AccessoryScreen extends AbstractInventoryScreen<AccessoryContainer> {

	public static final Identifier BACKGROUND_TEXTURE = new Identifier("soulmagic", "textures/gui/container/accessory_screen.png");
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");
	private static final ItemStack CHEST = new ItemStack(Blocks.CHEST);
	private static final ItemStack CHEST_PLATE = new ItemStack(Items.POTATO);
	
	private float mouseX;
	private float mouseY;

	public AccessoryScreen(AccessoryContainer container, PlayerEntity playerInventory_1, Text text_1) {
		super(container, playerInventory_1.inventory, text_1);
		this.passEvents = false;
	}

	@Override
	protected void drawForeground(MatrixStack matrix, int int_1, int int_2) {
		//this.font.draw(this.title.asFormattedString(), 97.0F, 8.0F, 4210752);
	}

	@Override
	public void render(MatrixStack matrix, int int_1, int int_2, float float_1) {
		this.renderBackground(matrix);
		super.render(matrix, int_1, int_2, float_1);
		
		if(int_1 >= this.x && int_1 <= this.x + 28 && int_2 >= this.y - 28 && int_2 <= this.y) {
			this.renderTooltip(matrix, new TranslatableText("container.soulmagic.inventory"), int_1, int_2);
		} else if (int_1 >= this.x + 28 && int_1 <= this.x + 56 && int_2 >= this.y - 28 && int_2 <= this.y) {
			this.renderTooltip(matrix, new TranslatableText("container.soulmagic.accessories"), int_1, int_2);
		}
		this.drawMouseoverTooltip(matrix, int_1, int_2);
		this.mouseX = (float) int_1;
		this.mouseY = (float) int_2;
	}
	
	@Override
	protected void drawBackground(MatrixStack matrix, float float_1, int int_1, int int_2) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		//GuiLighting.enableForItems();
		
		this.setZOffset(100);
	    this.itemRenderer.zOffset = 100.0F;
		RenderSystem.enableLighting();
		RenderSystem.enableRescaleNormal();
		this.itemRenderer.renderGuiItemIcon(CHEST, this.x + 6, this.y - 20);
		this.itemRenderer.renderGuiItemIcon(CHEST_PLATE, this.x + 34, this.y - 20);
		RenderSystem.disableLighting();
		this.itemRenderer.zOffset = 0.0F;
	    this.setZOffset(0);
		
		this.client.getTextureManager().bindTexture(TEXTURE);
		this.drawTexture(matrix, this.x, this.y - 28, 0, 0, 28, 30);
		
		this.client.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int int_3 = this.x;
		int int_4 = this.y;
		this.drawTexture(matrix, int_3, int_4, 0, 0, this.backgroundWidth, this.backgroundHeight);
		
		this.client.getTextureManager().bindTexture(TEXTURE);
		this.drawTexture(matrix, this.x + 28, this.y - 28, 28, 32, 28, 32);
		InventoryScreen.drawEntity(int_3 + 32, int_4 + 75, 30, (float) (int_3 + 32) - this.mouseX, (float) (int_4 + 75 - 50) - this.mouseY, this.client.player);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int int_1) {
		if(mouseX >= this.x && mouseX <= this.x + 28 && mouseY >= this.y - 32 && mouseY <= this.y) {
			this.client.openScreen(new InventoryScreen(this.client.player));
		}
		return super.mouseReleased(mouseX, mouseY, int_1);
	}
	
	//@Override
	//protected boolean isPointWithinBounds(int int_1, int int_2, int int_3, int int_4, double double_1, double double_2) {
	//	return super.isPointWithinBounds(int_1, int_2, int_3, int_4, double_1, double_2);
	//}
	
	//protected boolean isClickOutsideBounds(double double_1, double double_2, int int_1, int int_2, int int_3) {
	//	boolean boolean_1 = double_1 < (double) int_1 || double_2 < (double) int_2
	//			|| double_1 >= (double) (int_1 + this.containerWidth)
	//			|| double_2 >= (double) (int_2 + this.containerHeight);
	//	return boolean_1;
	//}
}
