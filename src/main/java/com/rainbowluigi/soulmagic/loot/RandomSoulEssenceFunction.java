package com.rainbowluigi.soulmagic.loot;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceContainer;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTableRange;
import net.minecraft.loot.LootTableRanges;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class RandomSoulEssenceFunction extends ConditionalLootFunction {
	private final LootTableRange range;

	protected RandomSoulEssenceFunction(LootCondition[] conditions, LootTableRange range) {
		super(conditions);
		this.range = range;
	}

	@Override
	public LootFunctionType getType() {
		return ModLoot.RANDOM_SOUL_ESSENCE;
	}

	@Override
	protected ItemStack process(ItemStack stack, LootContext context) {
		Random random = context.getRandom();
		SoulEssenceContainer sec = (SoulEssenceContainer) stack.getItem();

		sec.setSoul(stack, context.getWorld(), ModSoulTypes.LIGHT, this.range.next(random));
		sec.setSoul(stack, context.getWorld(), ModSoulTypes.DARK, this.range.next(random));

		return stack;
	}

	public static RandomSoulEssenceFunction.Builder builder(LootTableRange range) {
		return new RandomSoulEssenceFunction.Builder(range);
	}

	public static class Serializer extends ConditionalLootFunction.Serializer<RandomSoulEssenceFunction> {

		@Override
		public void toJson(JsonObject jsonObject, RandomSoulEssenceFunction enchantWithLevelsLootFunction, JsonSerializationContext jsonSerializationContext) {
			super.toJson(jsonObject, enchantWithLevelsLootFunction, jsonSerializationContext);
			jsonObject.add("amount", LootTableRanges.toJson(enchantWithLevelsLootFunction.range, jsonSerializationContext));
		}

		@Override
		public RandomSoulEssenceFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
			LootTableRange lootTableRange = LootTableRanges.fromJson(jsonObject.get("amount"), jsonDeserializationContext);
			return new RandomSoulEssenceFunction(lootConditions, lootTableRange);
		}
	}

	public static class Builder extends ConditionalLootFunction.Builder<RandomSoulEssenceFunction.Builder> {
		private final LootTableRange range;

		public Builder(LootTableRange range) {
			this.range = range;
		}

		protected RandomSoulEssenceFunction.Builder getThisBuilder() {
			return this;
		}

		public LootFunction build() {
			return new RandomSoulEssenceFunction(this.getConditions(), this.range);
		}
	}
}