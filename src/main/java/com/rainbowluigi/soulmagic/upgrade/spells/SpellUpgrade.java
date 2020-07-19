package com.rainbowluigi.soulmagic.upgrade.spells;

import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class SpellUpgrade extends Upgrade {

	private Identifier spellTexture;
	private String spellTranslationKey;

	public SpellUpgrade(ItemStack icon, int x, int y, Upgrade prev) {
		super(icon, x, y, prev);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}

	public ActionResult useOnBlock(ItemUsageContext itemUsageContext_1) {
		return ActionResult.PASS;
	}

	public Identifier getDefaultSpellTexture() {
		if(this.spellTexture == null) {
			Identifier rl = ModUpgrades.UPGRADE.getId(this);
			this.spellTexture = rl == null ? null : new Identifier(rl.getNamespace(), "textures/gui/spellicons/" + rl.getPath().replace('/', '.') + ".png");
		}
		return this.spellTexture;
	}
	
	public Identifier getSpellTexture() {
		return this.getDefaultSpellTexture();
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