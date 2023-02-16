package com.rainbowluigi.soulmagic.item;

import com.google.common.collect.Multimap;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class MagicArmorItem extends TrinketItem implements Upgradeable {

    public MagicArmorItem(Settings settings) {
        super(settings);
//        TrinketRendererRegistry.registerRenderer();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        modifiers.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uuid, "guidemod:movement_speed", 5, EntityAttributeModifier.Operation.ADDITION));
        // If the player has access to ring slots, this will give them an extra one
//        SlotAttributes.addSlotModifier(modifiers, "hand/ring", uuid, 1, EntityAttributeModifier.Operation.ADDITION);
        return modifiers;
    }

    @Override
    public List<Upgrade> getPossibleUpgrades(ItemStack stack) {
        return List.of();
    }
}
