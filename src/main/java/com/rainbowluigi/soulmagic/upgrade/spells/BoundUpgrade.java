package com.rainbowluigi.soulmagic.upgrade.spells;

import java.util.Map.Entry;

import com.rainbowluigi.soulmagic.SoulMagic;
import com.rainbowluigi.soulmagic.item.EnchantmentTemplateItem;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;
import com.rainbowluigi.soulmagic.util.ItemHelper;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BoundUpgrade extends SpellUpgrade {

	private Item item;
	
	private SoulType[] types = new SoulType[] {ModSoulTypes.LIGHT, ModSoulTypes.DARK};
	
	public BoundUpgrade(Item item, ItemStack icon, int x, int y, Upgrade prev, UpgradeSprite s) {
		super(icon, x, y, prev, s);
		this.item = item;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack boundStack = new ItemStack(this.item);

		ItemStack gem = player.getStackInHand(hand);
		Upgradeable u = (Upgradeable) gem.getItem();
		
		if(u.hasUpgradeSelected(gem, ModUpgrades.BOUND_ENCHANTMENTS)) {
			ItemStack template = ItemHelper.findItem(player, ModItems.WEAPON_ENCHANTMENT_TEMPLATE);

			if(template != null) {
				EnchantmentTemplateItem eti = (EnchantmentTemplateItem) template.getItem();
				if(eti.getTarget().isAcceptableItem(this.item)) {
					for(Entry<Enchantment, Integer> e : eti.getEnchantments(template).entrySet()) {
						boundStack.addEnchantment(e.getKey(), e.getValue());
					}
				}
			}
		}

		CompoundTag tag = boundStack.getOrCreateTag();
		
		tag.put("soulGem", new CompoundTag());
		
		player.getStackInHand(hand).toTag(tag.getCompound("soulGem"));
		
		player.setStackInHand(hand, boundStack);
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}
	
	@Override
	public SoulType[] getSoulTypesToShow() {
		return types;
	}
}
