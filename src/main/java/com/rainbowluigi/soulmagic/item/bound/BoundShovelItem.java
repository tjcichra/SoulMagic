package com.rainbowluigi.soulmagic.item.bound;

import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaffDisplayer;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BoundShovelItem extends ShovelItem implements SoulEssenceStaffDisplayer {

    private SoulType[] types = {ModSoulTypes.LIGHT, ModSoulTypes.DARK};

    public BoundShovelItem(Settings item$Settings_1) {
        super(BoundToolMaterial.BOUND_MATERIAL, 1.5F, -3.0F, item$Settings_1);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.hasNbt() && stack.getNbt().contains("soulGem")) {
            ItemStack gem = ItemStack.fromNbt(stack.getNbt().getCompound("soulGem"));
            player.setStackInHand(hand, gem);
        }
        return new TypedActionResult<ItemStack>(ActionResult.PASS, stack);
    }

    @Override
    public SoulType[] getSoulTypesToShow(ItemStack stack, PlayerEntity player) {
        return types;
    }
}
