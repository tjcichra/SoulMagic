package com.rainbowluigi.soulmagic.upgrade;

import com.mojang.serialization.Lifecycle;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ModUpgrades {
	public static final RegistryKey<Registry<Upgrade>> UPGRADE_KEY= RegistryKey.ofRegistry(new Identifier(Reference.MOD_ID, "upgrade"));
	public static final DefaultedRegistry<Upgrade> UPGRADE = new DefaultedRegistry<Upgrade>("fireball", UPGRADE_KEY, Lifecycle.experimental());

	public static final Upgrade FIREBALL = new Upgrade(new ItemStack(Items.CHEST), "upgrade.soulmagic.fireball", "upgrade.soulmagic.fireball", 2, 0, new ItemStack(Items.POTATO));
	public static final Upgrade FLAMING_TOUCH = new Upgrade(new ItemStack(Items.SADDLE), "upgrade.soulmagic.flaming_touch", "upgrade.soulmagic.flaming_touch", 4, 0, new ItemStack(Blocks.ACACIA_PLANKS));

	public static void registerUpgrades() {
		registerUpgrade(FIREBALL, "fireball");
		registerUpgrade(FLAMING_TOUCH, "flaming_touch");
	}
	
	public static void registerUpgrade(Upgrade u, String name) {
		Registry.register(UPGRADE, new Identifier(Reference.MOD_ID, name), u);
	}
}