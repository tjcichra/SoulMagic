package com.rainbowluigi.soulmagic.upgrade;

import com.mojang.serialization.Lifecycle;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.upgrade.spells.*;
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
	public static final DefaultedRegistry<Upgrade> UPGRADE = new DefaultedRegistry<Upgrade>(new Identifier(Reference.MOD_ID, "fireball").toString(), UPGRADE_KEY, Lifecycle.experimental());

	public static final Upgrade FIREBALL = new FireballUpgrade(new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade TRIPLE_FIREBALL = new Upgrade(new ItemStack(Items.BAKED_POTATO), -64, -14, FIREBALL).setRequirements(new ItemStack(Items.STICK), new ItemStack(Blocks.QUARTZ_BRICKS));
	public static final Upgrade FLAMING_TOUCH = new FlamingTouchUpgrade(new ItemStack(Items.SADDLE), 0, 32, null).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));

	public static final Upgrade ICEBALL = new IceballUpgrade(new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade BARRAGE = new BarrageUpgrade(new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade TENDRILS = new TendrilsUpgrade(new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade AIR_STRIKE = new AirStrikeUpgrade(new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade BOUND_PICKAXE = new BoundUpgrade(ModItems.BOUND_PICKAXE, new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_AXE = new BoundUpgrade(ModItems.BOUND_AXE, new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_SHOVEL = new BoundUpgrade(ModItems.BOUND_SHOVEL, new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_SWORD = new BoundUpgrade(ModItems.BOUND_SWORD, new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_SCYTHE = new BoundUpgrade(ModItems.BOUND_SWORD, new ItemStack(Items.POTATO), -32, -8, null).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade SOUL_ESSENCE_STAFF_INCREASE_1 = new Upgrade(new ItemStack(Items.ENDER_EYE), 32, -32, null).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));
	public static final Upgrade SOUL_ESSENCE_STAFF_INCREASE_2 = new Upgrade(new ItemStack(Items.POISONOUS_POTATO), 0, -16, SOUL_ESSENCE_STAFF_INCREASE_1).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));

	public static void registerUpgrades() {
		registerUpgrade(FIREBALL, "fireball");
		registerUpgrade(TRIPLE_FIREBALL, "triple_fireball");
		registerUpgrade(FLAMING_TOUCH, "flaming_touch");

		registerUpgrade(SOUL_ESSENCE_STAFF_INCREASE_1, "soul_essence_staff_increase_1");
		registerUpgrade(SOUL_ESSENCE_STAFF_INCREASE_2, "soul_essence_staff_increase_2");

		registerUpgrade(ICEBALL, "iceball");

		registerUpgrade(BARRAGE, "barrage");

		registerUpgrade(TENDRILS, "tendrils");

		registerUpgrade(AIR_STRIKE, "air_strike");

		registerUpgrade(BOUND_PICKAXE, "bound_pickaxe");
		registerUpgrade(BOUND_AXE, "bound_axe");
		registerUpgrade(BOUND_SHOVEL, "bound_shovel");
		registerUpgrade(BOUND_SWORD, "bound_sword");
		registerUpgrade(BOUND_SCYTHE, "bound_scythe");
	}
	
	public static void registerUpgrade(Upgrade u, String name) {
		Registry.register(UPGRADE, new Identifier(Reference.MOD_ID, name), u);
	}
}