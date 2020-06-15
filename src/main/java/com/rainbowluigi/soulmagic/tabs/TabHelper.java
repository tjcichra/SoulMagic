package com.rainbowluigi.soulmagic.tabs;

import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TabHelper {
	
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");
	
	public static void drawTabsBackground(Tab currentTab, MatrixStack matrix, Screen s, int x, int y) {
		MinecraftClient client = MinecraftClient.getInstance();
		ItemRenderer itemRenderer = client.getItemRenderer();

		itemRenderer.zOffset = 100.0F;
		RenderSystem.enableLighting();
		RenderSystem.enableRescaleNormal();

		int i = 0;
		for(Tab t: ModTabs.tabsList) {
			if(t.isVisible(client.player, client.world)) {
				itemRenderer.renderGuiItemIcon(t.getIcon(), x + 6 + i, y - 20);
				i += 28;
			}
		}
		
		RenderSystem.disableLighting();
		itemRenderer.zOffset = 0.0F;
		//this.blitOffset = 0;
		
		client.getTextureManager().bindTexture(TEXTURE);
		int j = 0;
		for(i = 0; i < ModTabs.tabsList.size(); i++) {
			Tab t = ModTabs.tabsList.get(i);
			
			if(t.isVisible(client.player, client.world)) {
				if(t.equals(currentTab)) {
					s.drawTexture(matrix, x + j, y - 28, j == 0 ? j : 28, 32, 28, 32);
				} else {
					s.drawTexture(matrix, x + j, y - 28, 0, 0, 28, 28);
				}
				j += 28;
			}
		}
	}

	public static void tabsRender(Tab currentTab, MatrixStack matrix, Screen s, int x, int y, int mouseX, int mouseY) {
		MinecraftClient client = MinecraftClient.getInstance();

		for(int i = 0; i < ModTabs.tabsList.size(); i++) {
			Tab t = ModTabs.tabsList.get(i);
			if(t.isVisible(client.player, client.world)) {
				int j = i * 28;
				if(mouseX >= x + j && mouseX < x + j + 28 && mouseY >= y - 28 && mouseY < y) {
					s.renderTooltip(matrix, Collections.singletonList(t.getText()), mouseX, mouseY);
				}
			}
		}
	}

	public static void tabsMouseReleased(Tab currentTab, Screen s, int x, int y, double mouseX, double mouseY) {
		MinecraftClient client = MinecraftClient.getInstance();

		for(int i = 0; i < ModTabs.tabsList.size(); i++) {
			Tab t = ModTabs.tabsList.get(i);
			if(t != currentTab && t.isVisible(client.player, client.world)) {
				int j = i * 28;
				if(mouseX >= x + j && mouseX < x + j + 28 && mouseY >= y - 28 && mouseY < y) {
					t.whenClicked(client.player, client.world);
				}
			}
		}
	}
}