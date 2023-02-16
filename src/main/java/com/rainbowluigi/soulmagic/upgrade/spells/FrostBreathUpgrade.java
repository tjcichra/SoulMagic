package com.rainbowluigi.soulmagic.upgrade.spells;

import com.rainbowluigi.soulmagic.statuseffects.ModStatusEffects;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

public class FrostBreathUpgrade extends SpellUpgrade {

    public FrostBreathUpgrade(int x, int y, Upgrade prev, UpgradeSprite icon, UpgradeSprite s) {
        super(x, y, prev, icon, s);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        player.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        List<Entity> entities = world.getOtherEntities(user, user.getBoundingBox().expand(4));

        for (Entity e : entities) {
            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) e;
                if (!le.isTeammate(user)) {
                    le.addStatusEffect(new StatusEffectInstance(ModStatusEffects.FROZEN, 5 * 20, 1, false, false));
                }
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
}
