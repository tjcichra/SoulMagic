package com.rainbowluigi.soulmagic.mixin;

import com.rainbowluigi.soulmagic.enchantment.ModEnchantments;
import com.rainbowluigi.soulmagic.inventory.AccessoriesInventory;
import com.rainbowluigi.soulmagic.item.crafting.PlayerAccessories;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;

@Mixin(PlayerEntity.class)
public abstract class SoulStaffMixin implements PlayerAccessories {
	
	@Shadow
	@Final
	PlayerInventory inventory;
	
	public AccessoriesInventory accessories_inventory = new AccessoriesInventory((PlayerEntity) (Object) this);
	
	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;updateItems()V"))
	public void tickMovement(CallbackInfo info) {
		this.accessories_inventory.updateItems();
	}
	
	@Inject(at = @At("TAIL"), method = "onKilledOther")
	public void onDeath(ServerWorld world, LivingEntity killed, CallbackInfo info) {
		int level = EnchantmentHelper.getLevel(ModEnchantments.SOUL_STEALER, ((PlayerEntity) (Object) this).getMainHandStack());
		
		int amount = MathHelper.nextInt(killed.world.random, 2 + level, 5 + level);
		
		for (int i = 0; i < inventory.size(); i++) {
			if(inventory.getStack(i).getItem() instanceof SoulEssenceStaff) {
				ItemStack stack = inventory.getStack(i);
				SoulEssenceStaff staff = (SoulEssenceStaff) stack.getItem();
				
				if (killed.getGroup() == EntityGroup.UNDEAD) {
					amount = staff.addSoul(stack, killed.world, ModSoulTypes.DARK, amount);
				} else {
					amount = staff.addSoul(stack, killed.world, ModSoulTypes.LIGHT, amount);
				}
				
				if(amount <= 0) {
					break;
				}
			}
		}
	}
	
	@Inject(at = @At("TAIL"), method = "readCustomDataFromTag")
	public void readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		ListTag accessoriesList = tag.getList("Accessories", 10);
		this.accessories_inventory.deserialize(accessoriesList);
	}
	
	@Inject(at = @At("TAIL"), method = "writeCustomDataToTag")
	public void writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		tag.put("Accessories", this.accessories_inventory.serialize(new ListTag()));
	}
	
	@Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
	public void dropInventory(CallbackInfo info) {
		this.accessories_inventory.dropAccessories();
	}
	
	public AccessoriesInventory getAccessories() {
		return this.accessories_inventory;
	}
}
