package com.rainbowluigi.soulmagic.upgrade;

import com.mojang.serialization.Lifecycle;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.upgrade.spells.*;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ModUpgrades {
	public static final RegistryKey<Registry<Upgrade>> UPGRADE_KEY= RegistryKey.ofRegistry(new Identifier(Reference.MOD_ID, "upgrade"));
	public static final DefaultedRegistry<Upgrade> UPGRADE = new DefaultedRegistry<Upgrade>(new Identifier(Reference.MOD_ID, "fireball").toString(), UPGRADE_KEY, Lifecycle.experimental());

	public static final Upgrade FIREBALL = new FireballUpgrade(new ItemStack(Items.POTATO), -32, -8, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade TRIPLE_FIREBALL = new Upgrade(new ItemStack(Items.BAKED_POTATO), -64, -14, FIREBALL, UpgradeSprite.CIRCLE_SPRITE).setRequirements(new ItemStack(Items.STICK), new ItemStack(Blocks.QUARTZ_BRICKS));
	public static final Upgrade FLAMING_TOUCH = new FlamingTouchUpgrade(new ItemStack(Items.SADDLE), 0, 32, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));

	public static final Upgrade ICEBALL = new IceballUpgrade(new ItemStack(Items.POTATO), -32, -8, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade BARRAGE = new BarrageUpgrade(new ItemStack(Items.POTATO), -32, -8, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade TENDRILS = new TendrilsUpgrade(new ItemStack(Items.POTATO), -32, -8, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade AIR_STRIKE = new AirStrikeUpgrade(new ItemStack(Items.POTATO), -32, -8, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade BOUND_PICKAXE = new BoundUpgrade(ModItems.BOUND_PICKAXE, new ItemStack(ModItems.BOUND_PICKAXE), -69, 2, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_AXE = new BoundUpgrade(ModItems.BOUND_AXE, new ItemStack(ModItems.BOUND_AXE), -37, -58, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_SHOVEL = new BoundUpgrade(ModItems.BOUND_SHOVEL, new ItemStack(ModItems.BOUND_SHOVEL), 15, -57, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_SWORD = new BoundUpgrade(ModItems.BOUND_SWORD, new ItemStack(ModItems.BOUND_SWORD), 58, -40, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_SCYTHE = new BoundUpgrade(ModItems.BOUND_SWORD, new ItemStack(ModItems.BOUND_SWORD), 74, -13, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade TOOL_SWITCHING = new Upgrade(new ItemStack(ModItems.FLYING_CHEST), -34, 38, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_ENCHANTMENTS = new Upgrade(new ItemStack(ModItems.FLYING_CHEST), 34, 38, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade SOUL_ESSENCE_STAFF_INCREASE_1 = new Upgrade(new ItemStack(Items.ENDER_EYE), 32, -32, null, UpgradeSprite.CIRCLE_SPRITE).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));
	public static final Upgrade SOUL_ESSENCE_STAFF_INCREASE_2 = new Upgrade(new ItemStack(Items.POISONOUS_POTATO), 0, -16, SOUL_ESSENCE_STAFF_INCREASE_1, UpgradeSprite.CIRCLE_SPRITE).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));

	public static final Upgrade ENCHANTMENT_OBELISK = new Upgrade(new ItemStack(Blocks.ENCHANTING_TABLE), -64, -14, null, UpgradeSprite.CIRCLE_SPRITE).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));

	public static final Upgrade SHARPNESS_1 = new EnchantmentUpgrade(new ItemStack(Blocks.ENCHANTING_TABLE), -68, 1, null, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 1).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));
	public static final Upgrade SHARPNESS_2 = new EnchantmentUpgrade(new ItemStack(Blocks.ENCHANTING_TABLE), -115, 13, SHARPNESS_1, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 2).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));
	public static final Upgrade SHARPNESS_3 = new EnchantmentUpgrade(new ItemStack(Blocks.ENCHANTING_TABLE), -157, 25, SHARPNESS_2, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 3).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));
	public static final Upgrade SHARPNESS_4 = new EnchantmentUpgrade(new ItemStack(Blocks.ENCHANTING_TABLE), -192, 62, SHARPNESS_3, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 4).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));
	public static final Upgrade SHARPNESS_5 = new EnchantmentUpgrade(new ItemStack(Blocks.ENCHANTING_TABLE), -208, 99, SHARPNESS_4, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 5).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));

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
		registerUpgrade(TOOL_SWITCHING, "tool_switching");
		registerUpgrade(BOUND_ENCHANTMENTS, "bound_enchantments");

		registerUpgrade(ENCHANTMENT_OBELISK, "enchantment_obelisk");

		registerUpgrade(SHARPNESS_1, "sharpness_1");
		registerUpgrade(SHARPNESS_2, "sharpness_2");
		registerUpgrade(SHARPNESS_3, "sharpness_3");
		registerUpgrade(SHARPNESS_4, "sharpness_4");
		registerUpgrade(SHARPNESS_5, "sharpness_5");
	}
	
	public static Upgrade registerUpgrade(Upgrade u, String name) {
		return Registry.register(UPGRADE, new Identifier(Reference.MOD_ID, name), u);
	}
}