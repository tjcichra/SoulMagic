package com.rainbowluigi.soulmagic.upgrade.spells;

import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SpellUpgrade extends Upgrade {

	private Identifier spellTexture;

	public SpellUpgrade(ItemStack icon, MutableText name, Text desc, int x, int y, Upgrade prev, ItemStack... stacks) {
		super(icon, name, desc, x, y, prev, stacks);
	}

	public SpellUpgrade(ItemStack icon, String name, String desc, int x, int y, Upgrade prev, ItemStack... stacks) {
		super(icon, name, desc, x, y, prev, stacks);
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
}