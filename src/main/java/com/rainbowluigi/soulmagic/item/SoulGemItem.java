package com.rainbowluigi.soulmagic.item;

import java.util.ArrayList;
import java.util.List;

import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaffDisplayer;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.spelltype.SpellType;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.spells.SpellUpgrade;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;
import com.rainbowluigi.soulmagic.util.SpellCooldownManager;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class SoulGemItem extends Item implements SoulEssenceStaffDisplayer, CircleSelection, Upgradeable {

	public SoulGemItem(Item.Settings settings) {
		super(settings);

		// this.addPropertyGetter(new Identifier("brace"), (stack, world, player) -> {
		// return SoulGemHelper.getBrace(stack) != null ? 1 : 0;
		// });
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
		// If it is in the right group
		if (this.isIn(group)) {
			// Add the empty soul gem
			items.add(new ItemStack(this));

			for (SpellType st : ModSpellTypes.SPELL_TYPE) {
				ItemStack stack = new ItemStack(this);
				SoulGemHelper.setSpellType(stack, st);
				this.addUpgrade(stack, st.getBaseUpgrade());
				this.incrementSelectorPoints(stack);
				this.setUpgradeSelection(stack, st.getBaseUpgrade(), true);
				items.add(stack);

				ItemStack stack2 = new ItemStack(this);
				SoulGemHelper.setSpellType(stack2, st);
				for (Upgrade u : this.getPossibleUpgrades(stack2)) {
					this.addUpgrade(stack2, u);
					this.incrementSelectorPoints(stack2);
					this.setUpgradeSelection(stack2, u, true);
				}
				items.add(stack2);
			}
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);

		if (SoulGemHelper.getSpellType(stack) != null) {
			SpellUpgrade s = SoulGemHelper.getCurrentSpell(stack);

			if (s != null && !SpellCooldownManager.getSpellCooldownManager(player).hasCooldown(s)) {
				return s.use(world, player, hand);
			}
		}

		return super.use(world, player, hand);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext iuc) {
		ItemStack stack = iuc.getPlayer().getStackInHand(iuc.getHand());

		if (SoulGemHelper.getSpellType(stack) != null) {
			SpellUpgrade s = SoulGemHelper.getCurrentSpell(stack);

			if (s != null) {
				return s.useOnBlock(iuc);
			}
		}

		return super.useOnBlock(iuc);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext tooltipContext) {
		if (SoulGemHelper.getSpellType(stack) != null && SoulGemHelper.getCurrentSpell(stack) != null) {
			list.add(SoulGemHelper.getCurrentSpell(stack).getSpellName().formatted(Formatting.GRAY));
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

	@Override
	public List<Upgrade> getPossibleUpgrades(ItemStack stack) {
		SpellType type = SoulGemHelper.getSpellType(stack);
		return type != null ? type.getPossibleUpgrades() : new ArrayList<>();
	}

	@Override
	public void onUnselection(ItemStack stack, World w, Upgrade u) {
		if(u instanceof SpellUpgrade && SoulGemHelper.getCurrentSpellIndex(stack) >= SoulGemHelper.getCurrentList(stack).size()) {
			SoulGemHelper.setCurrentSpellIndex(stack, SoulGemHelper.getCurrentList(stack).size() - 1);
		}
	}

	@Override
	public void onSelection(ItemStack stack, World w, Upgrade u) {
		if(u instanceof SpellUpgrade && SoulGemHelper.getCurrentSpellIndex(stack) < 0 && SoulGemHelper.getCurrentList(stack).size() > 0) {
			SoulGemHelper.setCurrentSpellIndex(stack, 0);
		}
	}

	@Override
	public List<CircleSelectionEntry> getEntries(ItemStack stack) {
		List<SpellUpgrade> spells = SoulGemHelper.getCurrentList(stack);
		List<CircleSelectionEntry> entries = new ArrayList<>();

		for(SpellUpgrade spell : spells) {
			entries.add(new CircleSelectionEntry(spell.getSpellName(), spell.getSpellTexture()));
		}

		return entries;
	}

	@Override
	public void onSelection(int index, ItemStack stack) {
		if (index != -1) {
			SoulGemHelper.setCurrentSpellIndex(stack, index);
			
			// NetworkHandler.MOD_CHANNEL.sendToServer(new SelectSpellMessage(index));
			PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
			pbb.writeInt(index);
			ClientPlayNetworking.send(ModNetwork.SOUL_GEM_INDEX, pbb);

			MinecraftClient client = MinecraftClient.getInstance();
			client.player.sendMessage(Text.translatable("soulmagic.select_spell", SoulGemHelper.getCurrentList(stack).get(index).getSpellName()), true);
		}
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		if (SoulGemHelper.getSpellType(stack) != null) {
			SpellUpgrade s = SoulGemHelper.getCurrentSpell(stack);

			if (s != null) {
				return s.getMaxUseTime(stack);
			}
		}

		return super.getMaxUseTime(stack);
	}

	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (SoulGemHelper.getSpellType(stack) != null) {
			SpellUpgrade s = SoulGemHelper.getCurrentSpell(stack);

			if (s != null) {
				s.usageTick(world, user, stack, remainingUseTicks);
			}
		}
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		if (SoulGemHelper.getSpellType(stack) != null) {
			SpellUpgrade s = SoulGemHelper.getCurrentSpell(stack);

			if (s != null) {
				return s.getUseAction(stack);
			}
		}

		return super.getUseAction(stack);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return super.hasGlint(stack) || SoulGemHelper.getActivated(stack);
	}
}
