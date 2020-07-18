package com.rainbowluigi.soulmagic.upgrade.spells;

import com.rainbowluigi.soulmagic.enchantment.ModEnchantments;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.minecraft.enchantment.Enchantments;
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
	
	public BoundUpgrade(Item item, ItemStack icon, String name, String desc, int x, int y, Upgrade prev, ItemStack... stacks) {
		super(icon, name, desc, x, y, prev, stacks);
		this.item = item;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack pickaxe = new ItemStack(this.item);
		CompoundTag tag = pickaxe.getOrCreateTag();
		
		tag.put("soulGem", new CompoundTag());
		
		player.getStackInHand(hand).toTag(tag.getCompound("soulGem"));
		
		player.setStackInHand(hand, pickaxe);
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}
	
	@Override
	public SoulType[] getSoulTypesToShow() {
		return types;
	}
}
