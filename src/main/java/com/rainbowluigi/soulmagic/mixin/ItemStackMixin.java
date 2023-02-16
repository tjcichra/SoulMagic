package com.rainbowluigi.soulmagic.mixin;

import com.rainbowluigi.soulmagic.item.Upgradeable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @ModifyVariable(method = "getTooltip", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EquipmentSlot;values()[Lnet/minecraft/entity/EquipmentSlot;"))
    public List<Text> onCraft(List<Text> tooltip, PlayerEntity player, TooltipContext context) {
        if (this.getItem() instanceof Upgradeable) {
            tooltip.add(Text.translatable("text.soulmagic.upgradeable").formatted(Formatting.DARK_GRAY));
        }
        return tooltip;
    }

    @Shadow
    public abstract Item getItem();
}
