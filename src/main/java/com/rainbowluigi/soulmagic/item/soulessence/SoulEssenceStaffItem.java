package com.rainbowluigi.soulmagic.item.soulessence;

import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SoulEssenceStaffItem extends Item implements SoulEssenceStaff, Upgradeable {

	public SoulEssenceStaffItem(Item.Settings settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			if (this.getSoul(stack, world, st) > 0) {
				tooltip.add(Text.translatable("soulmagic.soul_essence_staff.amount", st.getName(), this.getSoul(stack, world, st), this.getMaxSoul(stack, world, st)).formatted(st.getTextColor()));
			}
		}
	}

	// Adds a full soul essence staff to the creative menu
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
		super.appendStacks(group, items);

		if (this.isIn(group)) {
			ItemStack stack = new ItemStack(this);

			for (SoulType type : ModSoulTypes.SOUL_TYPE) {
				this.setSoul(stack, null, type, this.getMaxSoul(stack, null, type));
			}

			items.add(stack);
		}
	}

	@Override
	public int getMaxSoul(ItemStack stack, World world, SoulType type) {
		float multiplier = 1;

		if(this.hasUpgradeSelected(stack, ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_1)) {
			multiplier += 0.30;
		}

		if(this.hasUpgradeSelected(stack, ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_2)) {
			multiplier += 0.20;
		}

		return (int) (100 * multiplier);
	}

	@Override
	public List<Upgrade> getPossibleUpgrades(ItemStack stack) {
		return Arrays.asList(ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_1, ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_2);
	}

	// If soul essence staff upgrades are removed, ensure that the current soul essence is not over the max soul essence
	@Override
	public void onUnselection(ItemStack stack, World w, Upgrade u) {
		for (SoulType type : ModSoulTypes.SOUL_TYPE) {
			int maxSoulEssence = this.getMaxSoul(stack, w, type);

			if (this.getSoul(stack, w, type) > maxSoulEssence) {
				this.setSoul(stack, w, type, maxSoulEssence);
			}
		}
	}
}
