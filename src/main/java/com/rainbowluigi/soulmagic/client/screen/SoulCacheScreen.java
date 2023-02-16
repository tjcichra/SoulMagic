package com.rainbowluigi.soulmagic.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.inventory.SoulCacheScreenHandler;
import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SoulCacheScreen extends HandledScreen<SoulCacheScreenHandler> {

    public static Identifier rl = new Identifier(Reference.MOD_ID, "textures/gui/container/soul_cache.png");
    public SoulCacheScreenHandler sic;

    public SoulCacheScreen(SoulCacheScreenHandler sic, PlayerInventory playerInv, Text text) {
        super(sic, playerInv, text);
        this.sic = sic;
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(matrix, mouseX, mouseY);
    }
	
	/*@Override
	public void drawForeground(MatrixStack matrix, int mouseX, int mouseY) {
		String s = this.title.asFormattedString();
		this.textRenderer.draw(s, (float) (this.containerWidth - this.textRenderer.getStringWidth(s)) / 2, 6, 4210752);
		this.textRenderer.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float)(this.containerHeight - 96 + 3), 4210752);
	}*/

    @Override
    public void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        //this.guiy = (this.height - this.ySize) / 2;


        RenderSystem.setShaderColor(1, 1, 1, 1);


        this.client.getTextureManager().bindTexture(rl);
        this.drawTexture(matrix, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

    }
}