package com.rainbowluigi.soulmagic.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.EnchantmentUpgrade;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.world.World;

public class EnchantmentTemplateItem extends Item implements Upgradeable {

	private EnchantmentTarget target;

	public EnchantmentTemplateItem(Settings settings, EnchantmentTarget target) {
		super(settings);
		this.target = target;
	}

	@Override
	public List<Upgrade> getPossibleUpgrades(ItemStack stack) {
		switch (this.target) {
			case WEAPON:
				return Arrays.asList(ModUpgrades.SHARPNESS_1, ModUpgrades.SHARPNESS_2, ModUpgrades.SHARPNESS_3,
						ModUpgrades.SHARPNESS_4, ModUpgrades.SHARPNESS_5);
			default:
				return Collections.emptyList();
		}
	}

	public Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
		List<Upgrade> upgrades = this.getUpgradesSelected(stack);
		Map<Enchantment, Integer> enchantmentMap = new HashMap<>();

		for (Upgrade u : upgrades) {
			if (u instanceof EnchantmentUpgrade) {
				EnchantmentUpgrade eu = (EnchantmentUpgrade) u;
				int level = enchantmentMap.get(eu.getEnchantment()) == null ? 0
						: enchantmentMap.get(eu.getEnchantment());
				if (level < eu.getLevel()) {
					enchantmentMap.put(eu.getEnchantment(), eu.getLevel());
				}
			}
		}

		return enchantmentMap;
	}

	public Map<SoulType, Integer> getCost(ItemStack stack) {
		List<Upgrade> upgrades = this.getUpgradesSelected(stack);
		Map<SoulType, Integer> costMap = new HashMap<>();

		for (Upgrade u : upgrades) {
			if (u instanceof EnchantmentUpgrade) {
				EnchantmentUpgrade eu = (EnchantmentUpgrade) u;

				for (Entry<SoulType, Integer> entry : eu.getSoulEssenceRequirement().entrySet()) {
					int amount = costMap.getOrDefault(entry.getKey(), 0) + entry.getValue();
					costMap.put(entry.getKey(), amount);
				}
			}
		}

		return costMap;
	}

	public EnchantmentTarget getTarget() {
		return this.target;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		Map<SoulType, Integer> costMap = this.getCost(stack);

		for (Entry<SoulType, Integer> entry : costMap.entrySet()) {
			if (entry.getValue() > 0) {
				tooltip.add(Text.translatable("soulmagic.enchantment_template.amount", entry.getKey().getName(), entry.getValue()).formatted(entry.getKey().getTextColor()));
			}
		}
	}
}