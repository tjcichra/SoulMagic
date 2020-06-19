package com.rainbowluigi.soulmagic.upgrade;

import java.util.List;

public interface Upgradeable {
	
	public default List<Upgrade> getPossibleUpgrades() {
		return null;
	}
}