package com.rainbowluigi.soulmagic.spell;

import com.rainbowluigi.soulmagic.entity.TendrilEntity;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SpellTendrils extends Spell {

	private SoulType[] types = new SoulType[] {ModSoulTypes.LIGHT};
	
	public SpellTendrils() {
		super(ModSpellTypes.EARTHEN);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if(!world.isClient) {
			TendrilEntity ure = new TendrilEntity(world, player);
			ure.yaw = player.yaw;
			world.spawnEntity(ure);
		}
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}
	
	@Override
	public SoulType[] getSoulTypesToShow() {
		return types;
	}
	
	@Override
	public boolean isBase() {
		return false;
	}
}
