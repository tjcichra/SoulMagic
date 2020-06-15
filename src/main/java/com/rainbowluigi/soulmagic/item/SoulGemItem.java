package com.rainbowluigi.soulmagic.item;

import java.util.List;

import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaffDisplayer;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spell.ModSpells;
import com.rainbowluigi.soulmagic.spell.Spell;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.spelltype.SpellType;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class SoulGemItem extends Item implements SoulEssenceStaffDisplayer {

	public SoulGemItem(Item.Settings settings) {
		super(settings);
		
		//this.addPropertyGetter(new Identifier("brace"), (stack, world, player) -> {
		//	return SoulGemHelper.getBrace(stack) != null ? 1 : 0;
		//});
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		SpellType st = SoulGemHelper.getSpellType(stack);
		String name = super.getTranslationKey(stack);
		
		if (st != null) {
			return name + "." + st.getTranslationKey();
		}
		return name;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
		//If it is in the right group
		if (this.isIn(group)) {
			//Add the empty soul gem
			items.add(new ItemStack(this));
			
			for (SpellType st : ModSpellTypes.SPELL_TYPE) {
				ItemStack stack = new ItemStack(this);
				SoulGemHelper.setSpellType(stack, st);
				for(Spell s : ModSpells.SPELL) {
					if(s.isBase() && s.getParent() == st) {
						SoulGemHelper.addSpell(stack, s);
					}
				}
				items.add(stack);
				
				ItemStack stack2 = new ItemStack(this);
				SoulGemHelper.setSpellType(stack2, st);
				for(Spell s : ModSpells.SPELL) {
					if(s.getParent() == st || st == ModSpellTypes.ULTIMATE) {
						SoulGemHelper.addSpell(stack2, s);
					}
				}
				items.add(stack2);
			}
		}
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		
		if(SoulGemHelper.getSpellType(stack) != null) {
			Spell s = SoulGemHelper.getCurrentSpell(stack);
			
			if(s != null) {
				return s.use(world, player, hand);
			} else {
				player.sendMessage(new TranslatableText("soulmagic.spell.error"), true);
			}
		}
		
		return super.use(world, player, hand);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext iuc) {
		ItemStack stack = iuc.getPlayer().getStackInHand(iuc.getHand());
		
		if(SoulGemHelper.getSpellType(stack) != null) {
			Spell s = SoulGemHelper.getCurrentSpell(stack);
			
			if(s != null) {
				return s.useOnBlock(iuc);
			} else {
				iuc.getPlayer().sendMessage(new TranslatableText("soulmagic.spell.error"), true);
			}
		}
		
		return super.useOnBlock(iuc);
	}


	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext tooltipContext) {
		if (SoulGemHelper.getSpellType(stack) != null && SoulGemHelper.getCurrentSpell(stack) != null) {
			list.add(SoulGemHelper.getCurrentSpell(stack).getName().formatted(Formatting.GRAY));
		}
		super.appendTooltip(stack, world, list, tooltipContext);
	}

	@Override
	public SoulType[] getSoulTypesToShow(ItemStack stack, PlayerEntity player) {
		if (SoulGemHelper.getSpellType(stack) != null && SoulGemHelper.getCurrentSpell(stack) != null) {
			return SoulGemHelper.getCurrentSpell(stack).getSoulTypesToShow();
		}
		return null;
	}
}
