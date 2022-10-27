package com.rainbowluigi.soulmagic.statuseffects;

import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStatusEffects {
	
	public static final StatusEffect FROZEN = new SoulMagicStatusEffect(StatusEffectCategory.HARMFUL, 0xFFFFFF).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", -1, EntityAttributeModifier.Operation.MULTIPLY_BASE);

	public static void registerStatusEffects() {
		registerStatusEffect(FROZEN, "frozen");
	}

	private static void registerStatusEffect(StatusEffect effect, String name) {
		Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, name), effect);
	}
}