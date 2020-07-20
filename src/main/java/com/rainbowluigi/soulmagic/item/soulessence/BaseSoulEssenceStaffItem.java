package com.rainbowluigi.soulmagic.item.soulessence;

import java.util.Arrays;
import java.util.List;

import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class BaseSoulEssenceStaffItem extends Item implements SoulEssenceStaff, Upgradeable {

	private final int maxSoul;

	public BaseSoulEssenceStaffItem(int maxSoul, Item.Settings settings) {
		super(settings);
		this.maxSoul = maxSoul;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			if (this.getSoul(stack, world, st) > 0) {
				tooltip.add(new TranslatableText("soulmagic.soul_essence_staff.amount", st.getName(),
						this.getSoul(stack, world, st), this.getMaxSoul(stack, world, st))
								.formatted(st.getTextColor()));
			}
		}
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
		if (this.isIn(group)) {
			items.add(new ItemStack(this));

			ItemStack stack = new ItemStack(this);

			for (SoulType st : ModSoulTypes.SOUL_TYPE) {
				MinecraftClient client = MinecraftClient.getInstance();
				this.setSoul(stack, client.world, st, this.getMaxSoul(stack, client.world, st));
			}

			items.add(stack);
		}
	}

	@Override
	public int getMaxSoul(ItemStack stack, World world, SoulType type) {
		float multiplier = 1;

		if(this.hasUpgradeSelected(stack, ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_1)) {
			multiplier += 0.30;
			if(this.hasUpgradeSelected(stack, ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_2)) {
				multiplier += 0.20;
			}
		}

		return (int) (this.maxSoul * multiplier);
	}

	@Override
	public List<Upgrade> getPossibleUpgrades(ItemStack stack) {
		return Arrays.asList(ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_1, ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_2);
	}

	@Override
	public void onUnselection(ItemStack stack, World w, Upgrade u) {
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			if (this.getSoul(stack, w, st) > this.getMaxSoul(stack, w, st)) {
				this.setSoul(stack, w, st, this.getMaxSoul(stack, w, st));
			}
		}
	}
}
