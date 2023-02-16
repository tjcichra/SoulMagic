package com.rainbowluigi.soulmagic.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.logging.LogUtils;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceContainer;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.util.JsonHelper;
import org.slf4j.Logger;

import java.util.Set;

public class RandomSoulEssenceFunction extends ConditionalLootFunction {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final LootNumberProvider range;

    protected RandomSoulEssenceFunction(LootCondition[] conditions, LootNumberProvider range) {
        super(conditions);
        this.range = range;
    }

    @Override
    public LootFunctionType getType() {
        return ModLoot.RANDOM_SOUL_ESSENCE;
    }

    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return this.range.getRequiredParameters();
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        if (!(stack.getItem() instanceof SoulEssenceContainer)) {
            LOGGER.warn("Could not add soul essence to {} as it cannot store soul essence", stack);
            return stack;
        }

        SoulEssenceContainer sec = (SoulEssenceContainer) stack.getItem();

        sec.setSoul(stack, context.getWorld(), ModSoulTypes.LIGHT, this.range.nextInt(context));
        sec.setSoul(stack, context.getWorld(), ModSoulTypes.DARK, this.range.nextInt(context));

        return stack;
    }

    public static Builder builder(LootNumberProvider range) {
        return new Builder(range);
    }

    public static class Builder extends ConditionalLootFunction.Builder<Builder> {
        private final LootNumberProvider range;

        public Builder(LootNumberProvider range) {
            this.range = range;
        }

        @Override
        protected Builder getThisBuilder() {
            return this;
        }

        public LootFunction build() {
            return new RandomSoulEssenceFunction(this.getConditions(), this.range);
        }
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<RandomSoulEssenceFunction> {
        @Override
        public void toJson(JsonObject jsonObject, RandomSoulEssenceFunction enchantWithLevelsLootFunction, JsonSerializationContext jsonSerializationContext) {
            super.toJson(jsonObject, enchantWithLevelsLootFunction, jsonSerializationContext);
            jsonObject.add("amount", jsonSerializationContext.serialize(enchantWithLevelsLootFunction.range));
        }

        @Override
        public RandomSoulEssenceFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            LootNumberProvider lootNumberProvider = JsonHelper.deserialize(jsonObject, "amount", jsonDeserializationContext, LootNumberProvider.class);
            return new RandomSoulEssenceFunction(lootConditions, lootNumberProvider);
        }
    }
}