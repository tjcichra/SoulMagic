package com.rainbowluigi.soulmagic.block.entity;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.common.collect.Maps;
import com.rainbowluigi.soulmagic.inventory.SoulInfuserContainer;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.item.SoulGemItem;
import com.rainbowluigi.soulmagic.item.crafting.ModRecipes;
import com.rainbowluigi.soulmagic.item.crafting.SoulInfusionRecipe;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class SoulInfuserBlockEntity extends LockableContainerBlockEntity implements SidedInventory, BlockEntityClientSerializable, Tickable {

	private static final int[] TOP_SLOTS = new int[]{0,1,2,3,4,5,6,7,8};
	private static final int[] SIDE_SLOTS = new int[]{10};
	private static final int[] BOTTOM_SLOTS = new int[]{9};
	
	private DefaultedList<ItemStack> inventory;
	
	private Map<SoulType, Integer> cookSoulMap = Maps.newHashMap();
	private Map<SoulType, Integer> recipeSoulMap = Maps.newHashMap();
	private int progressColor = 0xFFFFFF;

	public SoulInfuserBlockEntity() {
		super(ModBlockEntity.SOUL_INFUSER);
		this.inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);
	}
	
	@Override
	public void fromTag(CompoundTag compound) {
		super.fromTag(compound);
		this.inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);
		this.fromClientTag(compound);
		Inventories.fromTag(compound, this.inventory);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag compound) {
		super.toTag(compound);
		this.toClientTag(compound);
		Inventories.toTag(compound, this.inventory);
		return compound;
	}
	
	@Override
	public void tick() {
		if(!this.world.isClient) {
			boolean flag = false;
			
			if(this.hasCenterItem()) {
				Optional<SoulInfusionRecipe> irecipe = this.world.getRecipeManager().getFirstMatch(ModRecipes.SOUL_INFUSION_TYPE, this, this.world);
				
				if(irecipe.isPresent()) {
					flag = true;
					
					this.recipeSoulMap = irecipe.get().getSoulMap();
					this.progressColor = irecipe.get().getProgressColor();
					
					ItemStack staff = this.getStaffCap();
					
					if(this.canCook(irecipe.get(), staff)) {
						ItemStack stack = irecipe.get().craft(this);
						
						if(this.inventory.get(9).isEmpty()) {
							this.inventory.set(9, stack);
						} else {
							this.inventory.get(9).setCount(this.inventory.get(9).getCount() + stack.getCount());
						}
						
						this.cookSoulMap.clear();
						this.sync();
						
						for (int i = 0; i < 9; i++) {
							ItemStack stackF = this.inventory.get(i);

							if (!stackF.isEmpty()) {
								
								stackF.decrement(1);

								if (stackF.isEmpty()) {
									this.inventory.set(i, irecipe.get().getRemainingStacks(this).get(i));
								}
							}
						}
					}
				}
			}
			
			if(!flag && !this.cookSoulMap.isEmpty()) {
				this.cookSoulMap.clear();
				this.sync();
			}
		}
	}
	
	public boolean hasCenterItem() {
		return !this.getInvStack(8).isEmpty();
	}
	
	public ItemStack getStaffCap() {
		return this.inventory.get(10).getItem() instanceof SoulEssenceStaff ? this.inventory.get(10) : null;
	}
	
	public boolean canCook(SoulInfusionRecipe recipe, ItemStack staff) {
		if(staff == null) {
			return false;
		}
		
		
		if(!this.inventory.get(9).isEmpty()) {
			ItemStack stack = this.inventory.get(9);
			if(!stack.isItemEqualIgnoreDamage(recipe.getOutput()) || stack.getCount() > stack.getMaxCount() - recipe.getOutput().getCount()) {
				return false;
			}
		}
		
		SoulEssenceStaff staff2 = (SoulEssenceStaff) staff.getItem();
		
		boolean good = true;
		
		for(Entry<SoulType, Integer> entry : recipe.getSoulMap().entrySet()) {
			Integer d = this.cookSoulMap.get(entry.getKey());
			int d2 = d != null ? d : 0;
			
			if(d2 < entry.getValue()) {
				if(staff2.subtractSoul(staff, this.world, entry.getKey(), 1)) {
					this.cookSoulMap.put(entry.getKey(), d2 + 1);
					
					//for(PlayerEntity p : this.world.getPlayers()) {
					//	ServerSidePacketRegistry.INSTANCE.sendToPlayer(p, SoulMagicClient.SOUL_INFUSER_PROGRESS, SoulInfuserProgressMessage.makePacket(this.pos, this.cookSoulMap, this.recipeSoulMap));
					//}
					
					this.sync();
					//NetworkHandler.MOD_CHANNEL.send(PacketDistributor.ALL.noArg(), new CookSoulMapMessage(this.cookSoulMap, irecipe.getSoulMap(), this.pos));
					
					//NetworkHandler.MOD_CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdateSoulStaffMessage((CompoundNBT) SoulManager.SOUL_CAPABILITY.writeNBT(ish, null), this.pos));
				}
				good = false;
			}
		}
		//for(Entry<SoulType, Double> entry : this.cookSoulMap.entrySet()) {
		//	SoulMagic.LOGGER.info("SoulType: " + entry.getKey().getName().getFormattedText() + "| Amount: " + entry.getValue());
		//}
		
		//SoulMagic.LOGGER.info(good);
		return good;
	}
	
	public Map<SoulType, Integer> getCookSoulMap() {
		return this.cookSoulMap;
	}

	public void setCookSoulMap(Map<SoulType, Integer> cookSoulMap) {
		this.cookSoulMap = cookSoulMap;
	}
	
	public Map<SoulType, Integer> getRecipeSoulMap() {
		return this.recipeSoulMap;
	}

	public void setRecipeSoulMap(Map<SoulType, Integer> recipeSoulMap) {
		this.recipeSoulMap = recipeSoulMap;
	}

	@Override
	public int getInvSize() {
		return this.inventory.size();
	}

	@Override
	public boolean isInvEmpty() {
		for(ItemStack stack : this.inventory) {
			if(!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getInvStack(int i) {
		return i >= 0 && i < this.inventory.size() ? this.inventory.get(i) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack takeInvStack(int i1, int i2) {
		return Inventories.splitStack(this.inventory, i1, i2);
	}

	@Override
	public ItemStack removeInvStack(int i) {
		return Inventories.removeStack(this.inventory, i);
	}

	@Override
	public void setInvStack(int i, ItemStack stack) {
		if(i >= 0 && i < this.inventory.size()) {
			this.inventory.set(i, stack);
		}
	}

	@Override
	public boolean canPlayerUseInv(PlayerEntity playerEntity_1) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return playerEntity_1.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public int[] getInvAvailableSlots(Direction d) {
		switch (d) {
		case DOWN:
			return BOTTOM_SLOTS;
		case UP:
			return TOP_SLOTS;
		default:
			return SIDE_SLOTS;
		}
	}

	@Override
	public boolean canInsertInvStack(int var1, ItemStack var2, Direction var3) {
		return this.isValidInvStack(var1, var2);
	}

	@Override
	public boolean canExtractInvStack(int var1, ItemStack var2, Direction var3) {
		return true;
	}

	@Override
	public boolean isValidInvStack(int slot, ItemStack stack) {
		if(slot == 9) {
			return false;
		} else if(slot != 10) {
			return !(slot != 8 && this.inventory.get(8).isEmpty() && stack.getItem() instanceof SoulGemItem);
		} else {
			return stack.getItem() instanceof SoulEssenceStaff;
		}
	}
	
	@Override
	protected Text getContainerName() {
		return new TranslatableText("container.soulmagic.soul_infuser");
	}

	@Override
	protected Container createContainer(int i, PlayerInventory pi) {
		return new SoulInfuserContainer(i, pi, this);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.progressColor = tag.getInt("colorProgress");
		
		CompoundTag cookSoul = tag.getCompound("cookSoul");
		
		for(SoulType st : ModSoulTypes.SOUL_TYPE_REG) {
			//if(cookSoul.contains(ModSoulTypes.SOUL_TYPE_REG.getId(e).toString())) {
				//System.out.println(e + " = " + cookSoul.getInt(ModSoulTypes.SOUL_TYPE_REG.getId(e).toString()));
				
			this.cookSoulMap.put(st, cookSoul.getInt(ModSoulTypes.SOUL_TYPE_REG.getId(st).toString()));
			//}
		}
		
		CompoundTag recipeSoul = tag.getCompound("recipeSoul");
		for(SoulType st : ModSoulTypes.SOUL_TYPE_REG) {
			//if(recipeSoul.contains(ModSoulTypes.SOUL_TYPE_REG.getId(e).toString())) {
				//System.out.println(e + " = " + cookSoul.getInt(ModSoulTypes.SOUL_TYPE_REG.getId(e).toString()));
				
			this.recipeSoulMap.put(st, recipeSoul.getInt(ModSoulTypes.SOUL_TYPE_REG.getId(st).toString()));
			//}
		}
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		CompoundTag cookSoul = new CompoundTag();
		for(SoulType st : ModSoulTypes.SOUL_TYPE_REG) {
			//System.out.println("Cook Soul: " + e);
			int x = this.cookSoulMap.get(st) != null ? this.cookSoulMap.get(st) : 0;
			cookSoul.putInt(ModSoulTypes.SOUL_TYPE_REG.getId(st).toString(), x);
		}
		
		CompoundTag recipeSoul = new CompoundTag();
		for(SoulType st : ModSoulTypes.SOUL_TYPE_REG) {
			//System.out.println("Recipe Soul: " + e);
			int x = this.recipeSoulMap.get(st) != null ? this.recipeSoulMap.get(st) : 0;
			recipeSoul.putInt(ModSoulTypes.SOUL_TYPE_REG.getId(st).toString(), x);
			//recipeSoul.putInt(ModSoulTypes.SOUL_TYPE_REG.getId(e.getKey()).toString(), e.getValue());
		}
		
		tag.putInt("colorProgress", this.progressColor);
		tag.put("cookSoul", cookSoul);
		tag.put("recipeSoul", recipeSoul);
		return tag;
	}
	
	public int getProgressColor() {
		return progressColor;
	}

	/*@Override
	public void fromClientTag(CompoundTag tag) {
		ListTag cookSoulMap = (ListTag) tag.getTag("cookSoulMap");
		
		for(int i = 0; i < cookSoulMap.size(); i++) {
			CompoundTag tag2 = (CompoundTag) cookSoulMap.get(i);
			this.cookSoulMap.put(ModSoulTypes.SOUL_TYPE_REG.get(new Identifier(tag2.getString("soulType"))), tag2.getDouble("amount"));
		}
		
		ListTag recipeSoulMap = (ListTag) tag.getTag("recipeSoulMap");
		
		for(int i = 0; i < recipeSoulMap.size(); i++) {
			CompoundTag tag2 = (CompoundTag) recipeSoulMap.get(i);
			this.recipeSoulMap.put(ModSoulTypes.SOUL_TYPE_REG.get(new Identifier(tag2.getString("soulType"))), tag2.getDouble("amount"));
		}
		//tag.put(string_1, tag_1)
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		System.out.println("tanner343");
		ListTag cookSoulMap = new ListTag();
		for(Entry<SoulType, Double> entry : this.cookSoulMap.entrySet()) {
			CompoundTag tag2 = new CompoundTag();
			tag2.putString("soulType", ModSoulTypes.SOUL_TYPE_REG.getId(entry.getKey()).toString());
			tag2.putDouble("amount", entry.getValue());
			cookSoulMap.add(tag2);
		}
		
		tag.put("cookSoulMap", cookSoulMap);
		
		ListTag recipeSoulMap = new ListTag();
		for(Entry<SoulType, Double> entry : this.recipeSoulMap.entrySet()) {
			CompoundTag tag2 = new CompoundTag();
			tag2.putString("soulType", ModSoulTypes.SOUL_TYPE_REG.getId(entry.getKey()).toString());
			tag2.putDouble("amount", entry.getValue());
			recipeSoulMap.add(tag2);
		}
		
		tag.put("recipeSoulMap", recipeSoulMap);
		
		return tag;
	}*/
}
