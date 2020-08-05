package com.rainbowluigi.soulmagic.upgrade.spells;

import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class SpellUpgrade extends Upgrade {

	private String spellTranslationKey;

	public SpellUpgrade(int x, int y, Upgrade prev, UpgradeSprite icon, UpgradeSprite s) {
		super(x, y, prev, icon, s);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}

	public ActionResult useOnBlock(ItemUsageContext itemUsageContext_1) {
		return ActionResult.PASS;
	}

	public int getMaxUseTime(ItemStack stack) {
		return 0;
	}

	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		
	}

	public UseAction getUseAction(ItemStack stack) {
		return UseAction.NONE;
	}
	
	public UpgradeSprite getSpellTexture() {
		return this.getIcon();
	}
	
	public SoulType[] getSoulTypesToShow() {
		return null;
	}

	@Environment(EnvType.CLIENT)
	public MutableText getSpellName() {
		return new TranslatableText(this.getOrCreateSpellTranslationKey());
	}

	public String getOrCreateSpellTranslationKey() {
		if (this.spellTranslationKey == null) {
			this.spellTranslationKey = Util.createTranslationKey("spell", ModUpgrades.UPGRADE.getId(this));
		}

		return this.spellTranslationKey;
	}
}