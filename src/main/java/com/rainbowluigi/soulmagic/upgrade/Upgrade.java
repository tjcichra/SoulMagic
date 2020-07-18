package com.rainbowluigi.soulmagic.upgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class Upgrade {
	private ItemStack icon;
	private MutableText name;
	private Text desc;
	private int x, y;
	private Upgrade prev;
	private ItemStack[] requirements;

	public Upgrade(ItemStack icon, MutableText name, Text desc, int x, int y, Upgrade prev, ItemStack... stacks) {
		this.icon = icon;
		this.name = name;
		this.desc = desc;
		this.x = x;
		this.y = y;
		this.prev = prev;
		this.requirements = stacks;
	}

	public Upgrade(ItemStack icon, String name, String desc, int x, int y, Upgrade prev, ItemStack... stacks) {
		this(icon, new TranslatableText(name), new TranslatableText(desc), x, y, prev, stacks);
	}

	public ItemStack getIcon() {
		return this.icon;
	}

	public MutableText getName() {
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

	public Upgrade getPrev() {
		return this.prev;
	}

	public ItemStack[] getRequirements() {
		return this.requirements;
	}
}