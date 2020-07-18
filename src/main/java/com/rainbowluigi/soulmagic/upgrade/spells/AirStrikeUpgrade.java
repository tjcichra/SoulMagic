package com.rainbowluigi.soulmagic.upgrade.spells;

import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.minecraft.item.ItemStack;

public class AirStrikeUpgrade extends SpellUpgrade {

	public AirStrikeUpgrade(ItemStack icon, String name, String desc, int x, int y, Upgrade prev, ItemStack... stacks) {
		super(icon, name, desc, x, y, prev, stacks);
	}
}
