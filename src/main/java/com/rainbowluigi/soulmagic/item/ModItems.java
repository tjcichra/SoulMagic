package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.SoulMagic;
import com.rainbowluigi.soulmagic.item.bound.BoundAxeItem;
import com.rainbowluigi.soulmagic.item.bound.BoundPickaxeItem;
import com.rainbowluigi.soulmagic.item.bound.BoundShovelItem;
import com.rainbowluigi.soulmagic.item.bound.BoundSwordItem;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    
    public static final Item SOUL_ESSENCE_STAFF = new BaseSoulEssenceStaffItem(50, new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item GREATER_SOUL_ESSENCE_STAFF = new BaseSoulEssenceStaffItem(150, new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item CREATIVE_SOUL_ESSENCE_STAFF = new CreativeSoulEssenceStaffItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item SOUL_GEM = new SoulGemItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item SOUL_QUIVER = new SoulQuiverItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item SOUL_LANTERN = new SoulLanternItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item FOOD_CHARM = new FoodCharmItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item REFERENCE_STAFF = new ReferenceStaffItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item ENCHANTMENT_CHARM = new AccessoryItem(new Item.Settings().maxDamage(64).group(SoulMagic.ITEM_GROUP));
    public static final Item EARRING = new AccessoryItem(Accessory.EARRINGS, new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item RING_OF_RECKLESSNESS = new AccessoryItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item AMULET_OF_BLINDED_RAGE = new ItemBlindedRage(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item UNIVERSE_RING = new UniverseRingItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item VACCUM = new VacuumItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item MAGICAL_BALL_OF_YARN = new MagicalBallOfYarnItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    public static final Item SOUL_MAGIC_BOOK = new SoulMagicBookItem(new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP));
    
    public static final Item LIGHT_SOUL_POWDER = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
    public static final Item DARK_SOUL_POWDER = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
    public static final Item PRIDEFUL_SOUL_POWDER = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
    
    public static final Item LIGHT_SOUL_INGOT = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
    public static final Item DARK_SOUL_INGOT = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
    public static final Item PRIDEFUL_SOUL_INGOT = new Item(new Item.Settings().group(SoulMagic.ITEM_GROUP));
    
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
        registerItem(GREATER_SOUL_ESSENCE_STAFF, "greater_soul_essence_staff");
        registerItem(CREATIVE_SOUL_ESSENCE_STAFF, "creative_soul_essence_staff");
        registerItem(SOUL_GEM, "soul_gem");
        registerItem(SOUL_QUIVER, "soul_quiver");
        registerItem(SOUL_LANTERN, "soul_lantern");
        registerItem(FOOD_CHARM, "food_charm");
        registerItem(REFERENCE_STAFF, "reference_staff");
        registerItem(ENCHANTMENT_CHARM, "enchantment_charm");
        registerItem(EARRING, "earring");
        registerItem(RING_OF_RECKLESSNESS, "ring_of_recklessness");
        registerItem(AMULET_OF_BLINDED_RAGE, "amulet_of_blinded_rage");
        registerItem(UNIVERSE_RING, "universe_ring");
        registerItem(VACCUM, "vacuum");
        registerItem(MAGICAL_BALL_OF_YARN, "magical_ball_of_yarn");
        registerItem(SOUL_MAGIC_BOOK, "soul_magic_book");
        
        registerItem(LIGHT_SOUL_POWDER, "light_soul_powder");
        registerItem(DARK_SOUL_POWDER, "dark_soul_powder");
        registerItem(PRIDEFUL_SOUL_POWDER, "prideful_soul_powder");
        
        registerItem(LIGHT_SOUL_INGOT, "light_soul_ingot");
        registerItem(DARK_SOUL_INGOT, "dark_soul_ingot");
        registerItem(PRIDEFUL_SOUL_INGOT, "prideful_soul_ingot");
        
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