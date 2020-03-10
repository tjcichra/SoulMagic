package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.PersonalChestContainer;

import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PersonalChestScreen extends ContainerScreen<PersonalChestContainer> {
	
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/shulker_box.png");
	
	public PersonalChestScreen(PersonalChestContainer container_1, PlayerInventory playerInventory_1, Text text_1) {
		super(container_1, playerInventory_1, text_1);
	}

	public void render(int int_1, int int_2, float float_1) {
		this.renderBackground();
		super.render(int_1, int_2, float_1);
		this.drawMouseoverTooltip(int_1, int_2);
	}

	protected void drawForeground(int int_1, int int_2) {
		this.textRenderer.draw(this.title.asFormattedString(), 8.0F, 6.0F, 4210752);
		this.textRenderer.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F,
				(float) (this.containerHeight - 96 + 2), 4210752);
	}

	protected void drawBackground(float float_1, int int_1, int int_2) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int int_3 = (this.width - this.containerWidth) / 2;
		int int_4 = (this.height - this.containerHeight) / 2;
		this.blit(int_3, int_4, 0, 0, this.containerWidth, this.containerHeight);
	}
}
