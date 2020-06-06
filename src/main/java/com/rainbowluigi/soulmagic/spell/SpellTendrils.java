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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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

			float pitch = player.pitch;
			float yaw = player.yaw;

			//System.out.println("pitch: " + player.pitch + " yaw: " + player.yaw);
			
			float float_6 = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
			float float_8 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
			ure.setVelocity((double) float_6, 0, (double) float_8);

			Vec3d vec3d_1 = player.getVelocity();
			//ure.setVelocity(ure.getVelocity().add(vec3d_1.x, vec3d_1.y, vec3d_1.z));
			
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
