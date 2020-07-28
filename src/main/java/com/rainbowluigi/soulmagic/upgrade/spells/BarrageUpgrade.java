package com.rainbowluigi.soulmagic.upgrade.spells;

import com.rainbowluigi.soulmagic.entity.BarrageEntity;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BarrageUpgrade extends SpellUpgrade {

	private SoulType[] types = new SoulType[] {ModSoulTypes.LIGHT};
	
	public BarrageUpgrade(int x, int y, Upgrade prev, UpgradeSprite icon, UpgradeSprite s) {
		super(x, y, prev, icon, s);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if(!world.isClient) {
			BarrageEntity ure = new BarrageEntity(world, player);
			world.spawnEntity(ure);
		}
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}
	
	@Override
	public SoulType[] getSoulTypesToShow() {
		return this.types;
	}
}
