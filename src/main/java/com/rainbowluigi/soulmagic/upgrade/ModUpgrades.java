package com.rainbowluigi.soulmagic.upgrade;

import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.upgrade.spells.*;
import com.rainbowluigi.soulmagic.util.Reference;
import com.rainbowluigi.soulmagic.util.SoulEssenceHelper;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ModUpgrades {

	public static final SimpleRegistry<Upgrade> UPGRADE = FabricRegistryBuilder.createSimple(Upgrade.class, new Identifier(Reference.MOD_ID, "upgrade")).buildAndRegister();

	public static final Upgrade FIREBALL = new FireballUpgrade(-32, -8, null, FireballUpgrade.ICON, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade TRIPLE_FIREBALL = new Upgrade(-64, -14, FIREBALL, null, UpgradeSprite.CIRCLE_SPRITE).setRequirements(new ItemStack(Items.STICK), new ItemStack(Blocks.QUARTZ_BRICKS));
	public static final Upgrade FLAMING_TOUCH = new FlamingTouchUpgrade(0, -32, null, FlamingTouchUpgrade.ICON, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));
	public static final Upgrade FLAMING_EDGE = new Upgrade(0, -48, FLAMING_TOUCH, FlamingTouchUpgrade.ICON, UpgradeSprite.CIRCLE_SPRITE).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));

	public static final Upgrade FROST_BREATH = new FrostBreathUpgrade(-32, -8, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade BARRAGE = new BarrageUpgrade(-32, -8, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade TENDRILS = new TendrilsUpgrade(-32, -8, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade AIR_STRIKE = new AirStrikeUpgrade(-32, -8, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade BOUND_PICKAXE = new BoundUpgrade(ModItems.BOUND_PICKAXE, -69, 2, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_AXE = new BoundUpgrade(ModItems.BOUND_AXE, -37, -58, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_SHOVEL = new BoundUpgrade(ModItems.BOUND_SHOVEL, 15, -57, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_SWORD = new BoundUpgrade(ModItems.BOUND_SWORD, 58, -40, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_SCYTHE = new BoundUpgrade(ModItems.BOUND_SWORD, 74, -13, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade TOOL_SWITCHING = new Upgrade(-34, 38, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));
	public static final Upgrade BOUND_ENCHANTMENTS = new Upgrade(34, 38, null, null, UpgradeSprite.GEM_SPRITE).setRequirements(new ItemStack(Items.POTATO), new ItemStack(Items.ACACIA_BOAT), new ItemStack(Blocks.JUKEBOX));

	public static final Upgrade SOUL_ESSENCE_STAFF_INCREASE_1 = new Upgrade(540, 480, null, null, UpgradeSprite.CIRCLE_SPRITE).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));
	public static final Upgrade SOUL_ESSENCE_STAFF_INCREASE_2 = new Upgrade( 540, 530, SOUL_ESSENCE_STAFF_INCREASE_1, null, UpgradeSprite.CIRCLE_SPRITE).setRequirements(new ItemStack(Blocks.ACACIA_PLANKS));

	public static final Upgrade ENCHANTING_COMPONENT = new Upgrade(-64, -14, null, null, UpgradeSprite.CIRCLE_SPRITE).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.BOOKSHELF, 4), new ItemStack(Blocks.ENCHANTING_TABLE));

	public static final Upgrade SHARPNESS_1 = new EnchantmentUpgrade(-68, 1, null, null, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 1, SoulEssenceHelper.createMap(ModSoulTypes.DARK, 10)).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));
	public static final Upgrade SHARPNESS_2 = new EnchantmentUpgrade(-115, 13, SHARPNESS_1, null, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 2, SoulEssenceHelper.createMap(ModSoulTypes.DARK, 10)).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));
	public static final Upgrade SHARPNESS_3 = new EnchantmentUpgrade(-157, 25, SHARPNESS_2, null, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 3, SoulEssenceHelper.createMap(ModSoulTypes.DARK, 10)).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));
	public static final Upgrade SHARPNESS_4 = new EnchantmentUpgrade(-192, 62, SHARPNESS_3, null, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 4, SoulEssenceHelper.createMap(ModSoulTypes.DARK, 10)).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));
	public static final Upgrade SHARPNESS_5 = new EnchantmentUpgrade(-208, 99, SHARPNESS_4, null, UpgradeSprite.CIRCLE_SPRITE, Enchantments.SHARPNESS, 5, SoulEssenceHelper.createMap(ModSoulTypes.DARK, 10)).setRequirements(new ItemStack(ModItems.LIGHT_SOUL_ESSENCE_POWDER, 4), new ItemStack(Blocks.ENCHANTING_TABLE));

	public static void registerUpgrades() {
		registerUpgrade(FIREBALL, "fireball");
		registerUpgrade(TRIPLE_FIREBALL, "triple_fireball");
		registerUpgrade(FLAMING_TOUCH, "flaming_touch");
		registerUpgrade(FLAMING_EDGE, "flaming_edge");

		registerUpgrade(SOUL_ESSENCE_STAFF_INCREASE_1, "soul_essence_staff_increase_1");
		registerUpgrade(SOUL_ESSENCE_STAFF_INCREASE_2, "soul_essence_staff_increase_2");

		registerUpgrade(FROST_BREATH, "frost_breath");

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

		registerUpgrade(SHARPNESS_1, "sharpness_1");
		registerUpgrade(SHARPNESS_2, "sharpness_2");
		registerUpgrade(SHARPNESS_3, "sharpness_3");
		registerUpgrade(SHARPNESS_4, "sharpness_4");
		registerUpgrade(SHARPNESS_5, "sharpness_5");

		registerUpgrade(ENCHANTING_COMPONENT, "enchanting_component");
	}
	
	public static Upgrade registerUpgrade(Upgrade u, String name) {
		return Registry.register(UPGRADE, new Identifier(Reference.MOD_ID, name), u);
	}
}