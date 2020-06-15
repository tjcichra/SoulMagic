package com.rainbowluigi.soulmagic.item;

import net.minecraft.item.Item;

public class ItemBlindedRage extends Item implements Accessory {

	private AccessoryType type;
	
	public ItemBlindedRage(Settings setting, AccessoryType type) {
		super(setting);
		this.type = type;
	}
	
	public ItemBlindedRage(Settings setting) {
		this(setting, null);
	}
	
	@Override
	public AccessoryType getType() {
		return this.type;
	}
}