package com.rainbowluigi.soulmagic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.network.ModNetwork;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

	private static final Identifier TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");
	private static final ItemStack CHEST = new ItemStack(Blocks.CHEST);
	private static final ItemStack CHEST_PLATE = new ItemStack(Items.POTATO);
	
	public InventoryScreenMixin(PlayerScreenHandler container_1, PlayerInventory playerInventory_1, Text text_1) {
		super(container_1, playerInventory_1, text_1);
	}
	
	@Inject(method = "drawBackground", at = @At("TAIL"))
	protected void drawBackground(MatrixStack matrix, float float_1, int int_1, int int_2, CallbackInfo info) {
		//GuiLighting.enableForItems(null);
		
		//this.blitOffset = 100;
		this.itemRenderer.zOffset = 100.0F;
		RenderSystem.enableLighting();
		RenderSystem.enableRescaleNormal();
		this.itemRenderer.renderGuiItem(CHEST, this.x + 6, this.y - 20);
		this.itemRenderer.renderGuiItem(CHEST_PLATE, this.x + 34, this.y - 20);
		RenderSystem.disableLighting();
		this.itemRenderer.zOffset = 0.0F;
		//this.blitOffset = 0;
		
		this.client.getTextureManager().bindTexture(TEXTURE);
		this.drawTexture(matrix, this.x, this.y - 28, 0, 32, 28, 32);
		this.drawTexture(matrix, this.x + 28, this.y - 28, 0, 0, 28, 28);
	}
	
	@Inject(method = "render", at = @At("TAIL"))
	public void render(MatrixStack matrix, int mouseX, int mouseY, float float_1, CallbackInfo info) {
		if(mouseX >= this.x && mouseX <= this.x + 28 && mouseY >= this.y - 28 && mouseY <= this.y) {
			this.renderTooltip(matrix, Collections.singletonList(new LiteralText("Inventory")), mouseX, mouseY);
		} else if (mouseX >= this.x + 28 && mouseX <= this.x + 56 && mouseY >= this.y - 28 && mouseY <= this.y) {
			this.renderTooltip(matrix, Collections.singletonList(new LiteralText("Accessories")), mouseX, mouseY);
		}
	}
	
	@Inject(method = "mouseReleased", at = @At("HEAD"))
	protected void mouseReleased(double mouseX, double mouseY, int int_1, CallbackInfoReturnable<Boolean> info) {
		if(mouseX >= this.x + 28 && mouseX <= this.x + 56 && mouseY >= this.y - 28 && mouseY <= this.y) {
			ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.OPEN_ACCESSORIES, new PacketByteBuf(Unpooled.buffer()));
		}
	}
}
