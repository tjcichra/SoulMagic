package com.rainbowluigi.soulmagic.upgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class Upgrade {
	private ItemStack icon;
	private Text name;
	private Text desc;
	private int x, y;

	public Upgrade(ItemStack icon, Text name, Text desc, int x, int y, ItemStack... stacks) {
		this.icon = icon;
		this.name = name;
		this.desc = desc;
		this.x = x;
		this.y = y;
	}

	public Upgrade(ItemStack icon, String name, String desc, int x, int y, ItemStack... stacks) {
		this(icon, new TranslatableText(name), new TranslatableText(desc), x, y, stacks);
	}

	public ItemStack getIcon() {
		return this.icon;
	}

	public Text getName() {
		return this.name;
	}

	public Text getDescription() {
		return this.desc;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}