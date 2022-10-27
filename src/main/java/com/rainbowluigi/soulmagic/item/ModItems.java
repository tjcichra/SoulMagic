package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.SoulMagic;
import com.rainbowluigi.soulmagic.item.accessory.Accessory;
import com.rainbowluigi.soulmagic.item.accessory.AccessoryItem;
import com.rainbowluigi.soulmagic.item.accessory.FlyingChestItem;
import com.rainbowluigi.soulmagic.item.accessory.FoodCharmItem;
import com.rainbowluigi.soulmagic.item.accessory.ItemBlindedRage;
import com.rainbowluigi.soulmagic.item.bound.BoundAxeItem;
import com.rainbowluigi.soulmagic.item.bound.BoundPickaxeItem;
import com.rainbowluigi.soulmagic.item.bound.BoundShovelItem;
import com.rainbowluigi.soulmagic.item.bound.BoundSwordItem;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaffItem;
import com.rainbowluigi.soulmagic.item.soulessence.CreativeSoulEssenceStaffItem;
import com.rainbowluigi.soulmagic.item.soulessence.ReferenceStaffItem;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceOrbItem;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
	
	public static final Item SOUL_ESSENCE_STAFF = new SoulEssenceStaffItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item CREATIVE_SOUL_ESSENCE_STAFF = new CreativeSoulEssenceStaffItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item SOUL_ESSENCE_ORB = new SoulEssenceOrbItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item SOUL_GEM = new SoulGemItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item SOUL_ESSENCE_QUIVER = new SoulQuiverItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item SOUL_ESSENCE_LANTERN = new SoulLanternItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item FOOD_CHARM = new FoodCharmItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item REFERENCE_STAFF = new ReferenceStaffItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item EARRING = new AccessoryItem(Accessory.EARRINGS, new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item RING_OF_RECKLESSNESS = new AccessoryItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item BUILDING_STAFF = new BuildingStaffItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item CALMING_FLUTE = new CalmingFluteItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item AMULET_OF_BLINDED_RAGE = new ItemBlindedRage(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item UNIVERSE_RING = new UniverseRingItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item FLYING_CHEST = new FlyingChestItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item MAGICAL_BALL_OF_YARN = new MagicalBallOfYarnItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item SOUL_MAGIC_BOOK = new SoulMagicBookItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item SPIRIT_LAMP = new SpiritLampItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
	public static final Item ESSENCE_EXPERIENCE_INTERFACE = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
	public static final Item ENCHANTMENT_COMBINATION_CATALYST = new Item(new Item.Settings().maxDamage(32).group(SoulMagic.ITEM_GROUP));
	public static final Item ENCHANTMENT_SEPARATION_CATALYST = new Item(new Item.Settings().maxDamage(32).group(SoulMagic.ITEM_GROUP));
	public static final Item REPAIRING_CATALYST = new Item(new Item.Settings().maxDamage(5000).group(SoulMagic.ITEM_GROUP));

	public static final Item WEAPON_ENCHANTMENT_TEMPLATE = new EnchantmentTemplateItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP), EnchantmentTarget.WEAPON);
	
	public static final Item LIGHT_SOUL_ESSENCE_POWDER = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
	public static final Item DARK_SOUL_ESSENCE_POWDER = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
	public static final Item PRIDEFUL_SOUL_ESSENCE_POWDER = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
	
	public static final Item LIGHT_SOUL_ESSENCE_INGOT = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
	public static final Item DARK_SOUL_ESSENCE_INGOT = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
	public static final Item PRIDEFUL_SOUL_ESSENCE_INGOT = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
	
	public static final Item IRON_BRACE = new BraceItem(1.5, 0xFFFFFF, new Item.Settings().group(SoulMagic.ITEM_GROUP));
	public static final Item LIGHT_SOUL_BRACE = new BraceItem(2, 0xEEC56A, new Item.Settings().group(SoulMagic.ITEM_GROUP));
	public static final Item DARK_SOUL_BRACE = new BraceItem(3, 0x3D2E4C, new Item.Settings().group(SoulMagic.ITEM_GROUP));
	public static final Item PRIDEFUL_SOUL_BRACE = new BraceItem(5, 0xFF55FF, new Item.Settings().group(SoulMagic.ITEM_GROUP));
	public static final Item CREATIVE_BRACE = new BraceItem(9000.1, 0xE100FF, new Item.Settings().group(SoulMagic.ITEM_GROUP));
	
	public static final Item BOUND_PICKAXE = new BoundPickaxeItem(new Item.Settings());
	public static final Item BOUND_AXE = new BoundAxeItem(new Item.Settings());
	public static final Item BOUND_SHOVEL = new BoundShovelItem(new Item.Settings());
	public static final Item BOUND_SWORD = new BoundSwordItem(new Item.Settings());

	public static void registerItems() {
		registerItem(SOUL_ESSENCE_STAFF, "soul_essence_staff");
		registerItem(CREATIVE_SOUL_ESSENCE_STAFF, "creative_soul_essence_staff");
		registerItem(SOUL_ESSENCE_ORB, "soul_essence_orb");
		registerItem(SOUL_GEM, "soul_gem");
		registerItem(SOUL_ESSENCE_QUIVER, "soul_essence_quiver");
		registerItem(SOUL_ESSENCE_LANTERN, "soul_essence_lantern");
		registerItem(FOOD_CHARM, "food_charm");
		registerItem(REFERENCE_STAFF, "reference_staff");
		registerItem(EARRING, "earring");
		registerItem(RING_OF_RECKLESSNESS, "ring_of_recklessness");
		registerItem(BUILDING_STAFF, "building_staff");
		registerItem(CALMING_FLUTE, "calming_flute");
		registerItem(AMULET_OF_BLINDED_RAGE, "amulet_of_blinded_rage");
		registerItem(UNIVERSE_RING, "universe_ring");
		registerItem(FLYING_CHEST, "flying_chest");
		registerItem(MAGICAL_BALL_OF_YARN, "magical_ball_of_yarn");
		registerItem(SOUL_MAGIC_BOOK, "soul_magic_book");
		registerItem(SPIRIT_LAMP, "spirit_lamp");
		registerItem(ESSENCE_EXPERIENCE_INTERFACE, "essence_experience_interface");
		registerItem(ENCHANTMENT_COMBINATION_CATALYST, "enchantment_combination_catalyst");
		registerItem(ENCHANTMENT_SEPARATION_CATALYST, "enchantment_separation_catalyst");
		registerItem(REPAIRING_CATALYST, "repairing_catalyst");
		
		registerItem(WEAPON_ENCHANTMENT_TEMPLATE, "weapon_enchantment_template");

		registerItem(LIGHT_SOUL_ESSENCE_POWDER, "light_soul_essence_powder");
		registerItem(DARK_SOUL_ESSENCE_POWDER, "dark_soul_essence_powder");
		registerItem(PRIDEFUL_SOUL_ESSENCE_POWDER, "prideful_soul_essence_powder");
		
		registerItem(LIGHT_SOUL_ESSENCE_INGOT, "light_soul_essence_ingot");
		registerItem(DARK_SOUL_ESSENCE_INGOT, "dark_soul_essence_ingot");
		registerItem(PRIDEFUL_SOUL_ESSENCE_INGOT, "prideful_soul_essence_ingot");
		
		registerItem(IRON_BRACE, "iron_brace");
		registerItem(LIGHT_SOUL_BRACE, "light_soul_brace");
		registerItem(DARK_SOUL_BRACE, "dark_soul_brace");
		registerItem(PRIDEFUL_SOUL_BRACE, "prideful_soul_brace");
		registerItem(CREATIVE_BRACE, "creative_brace");
		
		registerItem(BOUND_PICKAXE, "bound_pickaxe");
		registerItem(BOUND_AXE, "bound_axe");
		registerItem(BOUND_SHOVEL, "bound_shovel");
		registerItem(BOUND_SWORD, "bound_sword");
	}

	private static void registerItem(Item item, String name) {
		Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, name), item);
	}
}