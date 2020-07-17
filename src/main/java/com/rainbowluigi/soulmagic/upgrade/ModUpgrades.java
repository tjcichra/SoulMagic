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

	public static final Upgrade FIREBALL = new Upgrade(new ItemStack(Items.CHEST), "upgrade.soulmagic.fireball", "upgrade.soulmagic.fireball", 0, -16, null, new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade FLAMING_TOUCH = new Upgrade(new ItemStack(Items.SADDLE), "upgrade.soulmagic.flaming_touch", "upgrade.soulmagic.flaming_touch", 32, -32, FIREBALL, new ItemStack(Blocks.ACACIA_PLANKS));

	public static final Upgrade SOUL_ESSENCE_STAFF_INCREASE1 = new Upgrade(new ItemStack(Items.ENDER_EYE), "upgrade.soulmagic.soul_essence_staff_increase1", "upgrade.desc.soulmagic.soul_essence_staff_increase1", 32, -32, null, new ItemStack(Blocks.ACACIA_PLANKS));
	public static final Upgrade SOUL_ESSENCE_STAFF_INCREASE2 = new Upgrade(new ItemStack(Items.POISONOUS_POTATO), "upgrade.soulmagic.soul_essence_staff_increase2", "upgrade.desc.soulmagic.soul_essence_staff_increase2", 0, -16, SOUL_ESSENCE_STAFF_INCREASE1, new ItemStack(Blocks.ACACIA_PLANKS));

	public static void registerUpgrades() {
		registerUpgrade(FIREBALL, "fireball");
		registerUpgrade(FLAMING_TOUCH, "flaming_touch");

		registerUpgrade(SOUL_ESSENCE_STAFF_INCREASE1, "soul_essence_staff_increase1");
		registerUpgrade(SOUL_ESSENCE_STAFF_INCREASE2, "soul_essence_staff_increase2");
	}
	
	public static void registerUpgrade(Upgrade u, String name) {
		Registry.register(UPGRADE, new Identifier(Reference.MOD_ID, name), u);
	}
}