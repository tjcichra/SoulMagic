package com.rainbowluigi.soulmagic;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.block.entity.ModBlockEntity;
import com.rainbowluigi.soulmagic.enchantment.ModEnchantments;
import com.rainbowluigi.soulmagic.entity.ModEntityTypes;
import com.rainbowluigi.soulmagic.entity.villager.ModVillagerProfessions;
import com.rainbowluigi.soulmagic.inventory.ModScreenHandlerTypes;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.item.crafting.ModRecipes;
import com.rainbowluigi.soulmagic.item.soulessence.CreativeSoulEssenceStaffItem;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceOrbItem;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaffItem;
import com.rainbowluigi.soulmagic.loot.ModLoot;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.stats.ModStats;
import com.rainbowluigi.soulmagic.statuseffects.ModStatusEffects;
import com.rainbowluigi.soulmagic.tabs.ModTabs;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoulMagic implements ModInitializer {

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier(Reference.MOD_ID, "creative_tab")).icon(() -> new ItemStack(ModItems.SOUL_ESSENCE_STAFF)).build();

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModEnchantments.registerEnchantments();
        ModEntityTypes.registerEntityTypes();
        ModBlockEntity.registerBlockEntityTypes();
        ModStatusEffects.registerStatusEffects();
        ModRecipes.registerRecipeTypes();
        ModRecipes.registerRecipeSerializers();

        ModVillagerProfessions.registerPointsOfInterest();
        ModVillagerProfessions.registerVillagerProfessions();

        ModUpgrades.registerUpgrades();
        ModSoulTypes.registerSoulTypes();

        ModScreenHandlerTypes.registerScreenHandlerTypes();
        ModNetwork.registerClientToServerPackets();
        ModTabs.registerTabs();
        ModLoot.handleLoot();

        ModStats.registerStats();

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (!(entity instanceof PlayerEntity playerEntity)) {
                return;
            }

            int soulStealerLevel = EnchantmentHelper.getLevel(ModEnchantments.SOUL_STEALER, playerEntity.getMainHandStack());
            int soulEssenceAmount = MathHelper.nextInt(world.random, 2 + soulStealerLevel, 5 + soulStealerLevel);

            PlayerInventory playerInventory = playerEntity.getInventory();

            for (int i = 0; i < playerInventory.size(); i++) {
                ItemStack stack = playerInventory.getStack(i);

                if (stack.getItem() instanceof SoulEssenceStaff staff) {
                    if (killedEntity.getGroup() == EntityGroup.UNDEAD) {
                        soulEssenceAmount = staff.addSoul(stack, world, ModSoulTypes.DARK, soulEssenceAmount);
                    } else {
                        soulEssenceAmount = staff.addSoul(stack, world, ModSoulTypes.LIGHT, soulEssenceAmount);
                    }

                    if (soulEssenceAmount <= 0) {
                        break;
                    }
                }
            }
        });

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register((content) -> {
            // Blocks
            content.add(ModBlocks.SOUL_ESSENCE_INFUSER);
            content.add(ModBlocks.SOUL_SEPARATOR);
            content.add(ModBlocks.UPGRADE_STATION);
            content.add(ModBlocks.SOUL_CACHE);
            content.add(ModBlocks.INFINITE_WELL);

            // Items
            ((SoulEssenceStaffItem) ModItems.SOUL_ESSENCE_STAFF).addToCreativeMenu(content);
            ((CreativeSoulEssenceStaffItem) ModItems.CREATIVE_SOUL_ESSENCE_STAFF).addToCreativeMenu(content);
            ((SoulEssenceOrbItem) ModItems.SOUL_ESSENCE_ORB).addToCreativeMenu(content);
            content.add(ModItems.SOUL_GEM);
            content.add(ModItems.SOUL_ESSENCE_QUIVER);
            content.add(ModItems.SOUL_ESSENCE_LANTERN);
            content.add(ModItems.FOOD_CHARM);
            content.add(ModItems.REFERENCE_STAFF);
            content.add(ModItems.EARRING);
            content.add(ModItems.RING_OF_RECKLESSNESS);
            content.add(ModItems.BUILDING_STAFF);
            content.add(ModItems.CALMING_FLUTE);
            content.add(ModItems.AMULET_OF_BLINDED_RAGE);
            content.add(ModItems.UNIVERSE_RING);
            content.add(ModItems.FLYING_CHEST);
            content.add(ModItems.MAGICAL_BALL_OF_YARN);
            content.add(ModItems.SOUL_MAGIC_BOOK);
            content.add(ModItems.SPIRIT_LAMP);
            content.add(ModItems.ESSENCE_EXPERIENCE_INTERFACE);
            content.add(ModItems.ENCHANTMENT_COMBINATION_CATALYST);
            content.add(ModItems.ENCHANTMENT_SEPARATION_CATALYST);
            content.add(ModItems.REPAIRING_CATALYST);
            content.add(ModItems.WEAPON_ENCHANTMENT_TEMPLATE);
            content.add(ModItems.LIGHT_SOUL_ESSENCE_POWDER);
            content.add(ModItems.DARK_SOUL_ESSENCE_POWDER);
            content.add(ModItems.PRIDEFUL_SOUL_ESSENCE_POWDER);
            content.add(ModItems.LIGHT_SOUL_ESSENCE_INGOT);
            content.add(ModItems.DARK_SOUL_ESSENCE_INGOT);
            content.add(ModItems.PRIDEFUL_SOUL_ESSENCE_INGOT);
            content.add(ModItems.IRON_BRACE);
            content.add(ModItems.LIGHT_SOUL_BRACE);
            content.add(ModItems.DARK_SOUL_BRACE);
            content.add(ModItems.PRIDEFUL_SOUL_BRACE);
            content.add(ModItems.CREATIVE_BRACE);
        });
    }
}
