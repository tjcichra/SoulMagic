package com.rainbowluigi.soulmagic.mixin;

import com.rainbowluigi.soulmagic.inventory.AccessoriesInventory;
import com.rainbowluigi.soulmagic.item.crafting.PlayerAccessories;
import com.rainbowluigi.soulmagic.util.SpellCooldownManager;
import com.rainbowluigi.soulmagic.util.SpellCooldownProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class SoulStaffMixin implements PlayerAccessories, SpellCooldownProvider {

    @Shadow
    @Final
    PlayerInventory inventory;

    public AccessoriesInventory accessories_inventory = new AccessoriesInventory((PlayerEntity) (Object) this);
    public SpellCooldownManager spellCooldownManager = new SpellCooldownManager();

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;updateItems()V"))
    public void tickMovement(CallbackInfo info) {
        this.accessories_inventory.updateItems();
        this.spellCooldownManager.update();
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound tag, CallbackInfo info) {
        NbtList accessoriesList = tag.getList("Accessories", 10);
        this.accessories_inventory.deserialize(accessoriesList);
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.put("Accessories", this.accessories_inventory.serialize(new NbtList()));
    }

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    public void dropInventory(CallbackInfo info) {
        this.accessories_inventory.dropAccessories();
    }

    public AccessoriesInventory getAccessories() {
        return this.accessories_inventory;
    }

    public SpellCooldownManager getSpellCooldownManager() {
        return this.spellCooldownManager;
    }
}
