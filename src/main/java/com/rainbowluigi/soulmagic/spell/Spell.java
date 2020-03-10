package com.rainbowluigi.soulmagic.spell;

import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spelltype.SpellType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class Spell {

	private String translationKey;
	private SpellType parent;
	private Identifier spellTexture;

	public Spell(SpellType parent) {
		this.parent = parent;
	}
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}

	public ActionResult useOnBlock(ItemUsageContext itemUsageContext_1) {
		return ActionResult.PASS;
	}
	
	public SpellType getParent() {
		return this.parent;
	}
	
	public boolean isBase() {
		return false;
	}

	public Identifier getDefaultSpellTexture() {
		if(this.spellTexture == null) {
			Identifier rl = ModSpells.SPELL_REG.getId(this);
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
	public Text getName() {
		return new TranslatableText(this.getTranslationKey());
	}
	
	public String getOrCreateTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.createTranslationKey("spell", ModSpells.SPELL_REG.getId(this));
		}

		return this.translationKey;
	}

	public String getTranslationKey() {
		return this.getOrCreateTranslationKey();
	}
}
