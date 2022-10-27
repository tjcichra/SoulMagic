package com.rainbowluigi.soulmagic.upgrade.spells;

import com.rainbowluigi.soulmagic.entity.MagicFireballEntity;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;
import com.rainbowluigi.soulmagic.util.SpellCooldownManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireballUpgrade extends SpellUpgrade {

	public static final UpgradeSprite ICON = new UpgradeSprite(UpgradeSprite.baseTexture, 0, 0, 32, 32);
	private SoulType[] types = new SoulType[] {ModSoulTypes.DARK};
	
	public FireballUpgrade(int x, int y, Upgrade prev, UpgradeSprite icon, UpgradeSprite s) {
		super(x, y, prev, icon, s);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		//Vec3d v = player.getRotationVec(1.0F);
		//double x = player.x + v.x;
		//double y = player.y + v.y;
		//double z = player.z + v.z;

		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 2.0F, (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F);
		if(!world.isClient) {
			//MagicFireballEntity fireball = new MagicFireballEntity(world, player, x, y, z);
			
			MagicFireballEntity fireball = new MagicFireballEntity(world, player);
			
			float pitch = player.getPitch();
			float yaw = player.getYaw();
			//System.out.println("pitch: " + player.pitch + " yaw: " + player.yaw);
			float float_3 = 1.0F;
			float float_4 = 0.3F;
			float float_5 = 1.0f;
			
			float float_6 = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
			float float_7 = -MathHelper.sin((pitch + float_3) * 0.017453292F);
			float float_8 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
			fireball.setVelocity((double) float_6, (double) float_7 + 0.5f, (double) float_8, float_4, float_5);
			
			Vec3d vec3d_1 = player.getVelocity();
			fireball.setVelocity(fireball.getVelocity().add(vec3d_1.x, vec3d_1.y, vec3d_1.z));
			
			SpellCooldownManager.getSpellCooldownManager(player).setCooldown(this, 60);
			world.spawnEntity(fireball);
		}
		
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}
	
	@Override
	public SoulType[] getSoulTypesToShow() {
		return types;
	}
}
