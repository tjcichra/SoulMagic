package com.rainbowluigi.soulmagic.client.screen;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.client.SoulMagicClient;
import com.rainbowluigi.soulmagic.item.CircleSelection;
import com.rainbowluigi.soulmagic.item.CircleSelection.CircleSelectionEntry;
import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class CircleSelectionScreen extends Screen {

	private final CircleSelection selection;
	private final ItemStack stack;
	double range = 0;
	int mouseX, mouseY;

	public CircleSelectionScreen(CircleSelection selection, ItemStack stack) {
		super(NarratorManager.EMPTY);
		this.passEvents = true;
		this.selection = selection;
		this.stack = stack;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public boolean keyReleased(int keyCode, int somethingelse, int somethingelse2) {
		if (SoulMagicClient.SPELL_SELECT.matchesKey(keyCode, 0)) {
			int index = this.getHoveredSpellIndex();

			this.selection.onSelection(index, this.stack);
			this.close();
		}
		return super.keyReleased(keyCode, somethingelse, somethingelse2);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		List<CircleSelectionEntry> spells = this.selection.getEntries(this.stack);
		
		if(spells.size() > 0) {
			double angle = (2 * Math.PI) / (spells.size());
			//double range = 90;
			
			for(int i = 0; i < spells.size(); i++) {
				UpgradeSprite sprite = spells.get(i).getSprite();
				
				int x =  (int) (this.range * Math.sin(angle * i));
				int y =  (int) (-this.range * Math.cos(angle * i));
				
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				this.client.getTextureManager().bindTexture(sprite.getTexture());
				//SoulMagic.LOGGER.info("hello");
				this.drawTexture(matrix, (this.width / 2) + x - (sprite.getLength() / 2), (this.height / 2) + y - (sprite.getHeight() / 2), sprite.getTextureX(), sprite.getTextureY(), sprite.getLength(), sprite.getHeight());
				//this.drawTexturedModalRect((this.width / 2) + x - 16, (this.height / 2) + y - 16, 0, 0, 32, 32);
				
				//Gui.drawRect(x + (this.width / 2) - 10, (this.height / 2) + y - 10, x + (this.width / 2) + 10, (this.height / 2) + y + 10, 0xFFFFFF);
				//this.addButton(new SelectSpellButton(i, (this.width / 2) + x - 10, (this.height / 2) + y - 10, spells.get(i).getUnlocalizedName()));
			}
			
			int index = this.getHoveredSpellIndex();
			if(index != -1) {
				this.drawCenteredText(matrix, this.textRenderer, spells.get(index).getName(), (this.width / 2), (this.height / 2) - 20, 0xFFFFFF);
			}
			
			if(this.range < this.height / 4) {
				this.range += (this.height / (4.0 * 18));
			}
		}
		super.render(matrix, mouseX, mouseY, partialTicks);
	}
	
	public int getHoveredSpellIndex() {
		double cX = ((this.mouseX) - (this.width / 2));
		double cY = -((this.mouseY) - (this.height / 2));
		double range = Math.sqrt(Math.pow(cX, 2) + Math.pow(cY, 2));
		
		List<CircleSelectionEntry> spells = this.selection.getEntries(this.stack);
		
		if(spells.size() > 0 && range >= 20) {
			double angle = Math.atan(cX / cY);
			
			if(cX > 0 && cY < 0) {
				angle += Math.PI;
			} else if(cX <= 0 && cY < 0) {
				angle += Math.PI;
			} else if(cX < 0 && cY >= 0) {
				angle += 2*Math.PI;
			}
			
			//It can be NaN
			if(!Double.isNaN(angle)) {
				int index = -1;
				
				double a2 = (2 * Math.PI) / spells.size();
				int i = 1;
				for(double a = (a2/2); a < (2 * Math.PI) - (a2/2); a += a2, i++) {
					if(angle >= a && angle < (2 * Math.PI) - (a2/2)) {
						index = i;
					} else {
						break;
					}
				}
				if(index == -1) {
					index = 0;
				}
				
				return index;
			}
		}
		
		return -1;
	}
}
