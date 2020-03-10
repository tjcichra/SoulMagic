package com.rainbowluigi.soulmagic.spell;

import com.rainbowluigi.soulmagic.enchantment.ModEnchantments;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SpellBound extends Spell {

	private Item item;
	private boolean silk, fortune, stealer;
	
	private SoulType[] types = new SoulType[] {ModSoulTypes.LIGHT, ModSoulTypes.DARK};
	
	public SpellBound(Item item, boolean silk, boolean fortune, boolean stealer) {
		super(ModSpellTypes.BINDING);
		this.item = item;
		this.silk = silk;
		this.fortune = fortune;
		this.stealer = stealer;
	}
	
	public SpellBound(Item item) {
		this(item, false, false, false);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack pickaxe = new ItemStack(this.item);
		CompoundTag tag = pickaxe.getOrCreateTag();
		
		if(silk) {
			pickaxe.addEnchantment(Enchantments.SILK_TOUCH, 1);
		} else if(fortune) {
			pickaxe.addEnchantment(Enchantments.FORTUNE, 1);
		} else if(stealer) {
			pickaxe.addEnchantment(ModEnchantments.SOUL_STEALER, 6);
		}
		
		tag.put("soulGem", new CompoundTag());
		
		player.getStackInHand(hand).toTag(tag.getCompound("soulGem"));
		
		player.setStackInHand(hand, pickaxe);
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}
	
	@Override
	public SoulType[] getSoulTypesToShow() {
		return types;
	}
	
	@Override
	public boolean isBase() {
		return this == ModSpells.BOUND_PICKAXE;
	}
}
