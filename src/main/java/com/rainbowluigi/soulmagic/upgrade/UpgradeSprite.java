package com.rainbowluigi.soulmagic.upgrade;

import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.util.Identifier;

public class UpgradeSprite {

	public static final Identifier baseTexture = new Identifier(Reference.MOD_ID, "textures/gui/container/upgrade_sprites.png");

	public static final UpgradeSprite CIRCLE_SPRITE = new UpgradeSprite(baseTexture, 0, 0, 24, 24);
	public static final UpgradeSprite GEM_SPRITE = new UpgradeSprite(baseTexture, 0, 232, 20, 24);
	public static final UpgradeSprite STAR_SPRITE = new UpgradeSprite(baseTexture, 0, 0, 24, 24);

	private Identifier texture;
	private int textureX, textureY, length, height;

	public UpgradeSprite(Identifier texture, int textureX, int textureY, int length, int height) {
		this.texture = texture;
		this.textureX = textureX;
		this.textureY = textureY;
		this.length = length;
		this.height = height;
	}

	public Identifier getTexture() {
		return this.texture;
	}

	public int getTextureX() {
		return this.textureX;
	}

	public int getTextureY() {
		return this.textureY;
	}
	
	public int getLength() {
		return this.length;
	}

	public int getHeight() {
		return this.height;
	}
}