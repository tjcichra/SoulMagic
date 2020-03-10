package com.rainbowluigi.soulmagic.item;

import net.minecraft.item.Item;

public class AccessoryItem extends Item implements Accessory {

	private AccessoryType type;
	
	public AccessoryItem(AccessoryType type, Settings setting) {
		super(setting);
		this.type = type;
	}
	
	public AccessoryItem(Settings setting) {
		this(null, setting);
	}
	
	@Override
	public AccessoryType getType() {
		return this.type;
	}
}