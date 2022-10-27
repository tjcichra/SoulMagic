package com.rainbowluigi.soulmagic.block.entity;

import com.google.common.collect.Maps;
import com.rainbowluigi.soulmagic.inventory.SoulInfuserScreenHandler;
import com.rainbowluigi.soulmagic.item.SoulGemItem;
import com.rainbowluigi.soulmagic.item.crafting.ModRecipes;
import com.rainbowluigi.soulmagic.item.crafting.SoulInfusionRecipe;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;

public class SoulEssenceInfuserBlockEntity extends LockableContainerBlockEntity implements SidedInventory, ExtendedScreenHandlerFactory {
	private static final int[] TOP_SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private static final int[] SIDE_SLOTS = new int[] { 10 };
	private static final int[] BOTTOM_SLOTS = new int[] { 9 };

	private static final int CENTER_SLOT = 8;
	private static final int OUTPUT_SLOT = 9;
	private static final int STAFF_SLOT = 10;

	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);

	private List<UpgradeAndSelection> upgrades = Collections.emptyList();
	private int selectorPoints;

	private Map<SoulType, Integer> cookSoulMap = Maps.newHashMap();
	private Map<SoulType, Integer> recipeSoulMap = Maps.newHashMap();

	private int progressColor = 0xFFFFFF;

	private final RecipeManager.MatchGetter<SoulEssenceInfuserBlockEntity, SoulInfusionRecipe> matchGetter;

	public SoulEssenceInfuserBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntity.SOUL_INFUSER, pos, state);
		this.matchGetter = RecipeManager.createCachedMatchGetter(ModRecipes.SOUL_ESSENCE_INFUSION_TYPE);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		this.inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory);

		NbtCompound cookSoulNbt = nbt.getCompound("cookSoul");
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			this.cookSoulMap.put(st, cookSoulNbt.getInt(ModSoulTypes.SOUL_TYPE.getId(st).toString()));
		}

		NbtCompound recipeSoulNbt = nbt.getCompound("recipeSoul");
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			this.recipeSoulMap.put(st, recipeSoulNbt.getInt(ModSoulTypes.SOUL_TYPE.getId(st).toString()));
		}

		this.progressColor = nbt.getInt("progressColor");

		NbtList upgradesNbt = (NbtList) nbt.get("upgrades");

		if (upgradesNbt != null) {
			for (NbtElement nbtElement : upgradesNbt) {
				NbtCompound tag = (NbtCompound) nbtElement;
				upgrades.add(new UpgradeAndSelection(ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name"))), tag.getBoolean("selected")));
			}
		}

		this.selectorPoints = nbt.getInt("selectorPoints");
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.inventory);

		NbtCompound cookSoul = new NbtCompound();
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			int x = this.cookSoulMap.get(st) != null ? this.cookSoulMap.get(st) : 0;
			cookSoul.putInt(ModSoulTypes.SOUL_TYPE.getId(st).toString(), x);
		}

		NbtCompound recipeSoul = new NbtCompound();
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			// System.out.println("Recipe Soul: " + e);
			int x = this.recipeSoulMap.get(st) != null ? this.recipeSoulMap.get(st) : 0;
			recipeSoul.putInt(ModSoulTypes.SOUL_TYPE.getId(st).toString(), x);
			// recipeSoul.putInt(ModSoulTypes.SOUL_TYPE_REG.getId(e.getKey()).toString(),
			// e.getValue());
		}

		nbt.putInt("progressColor", this.progressColor);
		nbt.put("cookSoul", cookSoul);
		nbt.put("recipeSoul", recipeSoul);

		nbt.putInt("selectorPoints", this.selectorPoints);

		if(!this.upgrades.isEmpty()) {
			if(!nbt.contains("upgrades")) {
				NbtList list = new NbtList();
				nbt.put("upgrades", list);
			}

			NbtList list = (NbtList) nbt.get("upgrades");

			for(UpgradeAndSelection u : this.upgrades) {
				NbtCompound upgradeTag = new NbtCompound();
				upgradeTag.putString("name", ModUpgrades.UPGRADE.getId(u.u).toString());
				upgradeTag.putBoolean("selected", u.selected);
				list.add(upgradeTag);
			}
		}
		nbt.putInt("selectorPoints", this.selectorPoints);
	}

	public static void tick(World world, BlockPos pos, BlockState state, SoulEssenceInfuserBlockEntity blockEntity) {
		if (tickCrafting(world, blockEntity)) {
			//Updates on the client said
			world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
		}
	}

	public static boolean tickCrafting(World world, SoulEssenceInfuserBlockEntity blockEntity) {
		if (blockEntity.inventory.get(CENTER_SLOT).isEmpty()) {
			blockEntity.cookSoulMap.clear();
			return true;
		}

		ItemStack staffStack = blockEntity.inventory.get(STAFF_SLOT);
		if (!(staffStack.getItem() instanceof SoulEssenceStaff)) {
			return false;
		}

		Optional<SoulInfusionRecipe> optionalRecipe = blockEntity.matchGetter.getFirstMatch(blockEntity, world);

		if (optionalRecipe.isEmpty()) {
			blockEntity.cookSoulMap.clear();
			return true;
		}

		SoulInfusionRecipe recipe = optionalRecipe.get();

		blockEntity.recipeSoulMap = recipe.getSoulMap(blockEntity, world);
		blockEntity.progressColor = recipe.getProgressColor();

		int maxCount = blockEntity.getMaxCountPerStack();
		if (!canAcceptRecipeOutput(recipe, blockEntity.inventory, maxCount)) {
			return false;
		}

		if (!useSoulEssence(recipe, staffStack, blockEntity, blockEntity.cookSoulMap, blockEntity.world)) {
			return true;
		}

		craft(recipe, blockEntity, blockEntity.inventory);
		blockEntity.cookSoulMap.clear();
		return true;
	}

	public static boolean canAcceptRecipeOutput(SoulInfusionRecipe recipe, DefaultedList<ItemStack> inventory, int inventoryMaxCount) {
		ItemStack recipeOutput = recipe.getOutput();
		ItemStack outputSlot = inventory.get(OUTPUT_SLOT);
		if (outputSlot.isEmpty()) {
			return true;
		}
		if (!outputSlot.isItemEqualIgnoreDamage(recipeOutput)) {
			return false;
		}
		int maxCount = Math.min(outputSlot.getMaxCount(), inventoryMaxCount);
		return maxCount >= outputSlot.getCount() + recipeOutput.getCount();
	}

	public static boolean useSoulEssence(SoulInfusionRecipe recipe, ItemStack staffStack, Inventory inventory, Map<SoulType, Integer> cookSoulMap, World world) {
		if (!(staffStack.getItem() instanceof SoulEssenceStaff staff)) {
			return false;
		}

		boolean noSoulEssenceUsed = true;

		for (Entry<SoulType, Integer> entry : recipe.getSoulMap(inventory, world).entrySet()) {
			SoulType neededSoulType = entry.getKey();
			Integer neededSoulEssenceForType = entry.getValue();

			Integer currentSoulEssenceForType = cookSoulMap.get(neededSoulType);
			int normalizeCurrentSoulEssenceForType = currentSoulEssenceForType != null ? currentSoulEssenceForType : 0;

			if (normalizeCurrentSoulEssenceForType < neededSoulEssenceForType) {
				if (staff.subtractSoul(staffStack, world, neededSoulType, 1)) {
					cookSoulMap.put(neededSoulType, normalizeCurrentSoulEssenceForType + 1);
				}
				noSoulEssenceUsed = false;
			}
		}

		return noSoulEssenceUsed;
	}

	public static void craft(SoulInfusionRecipe recipe, SoulEssenceInfuserBlockEntity blockEntity, DefaultedList<ItemStack> inventory) {
		ItemStack craftResult = recipe.craft(blockEntity);

		ItemStack outputSlot = inventory.get(OUTPUT_SLOT);
		if (outputSlot.isEmpty()) {
			inventory.set(OUTPUT_SLOT, craftResult);
		} else if (outputSlot.isOf(craftResult.getItem())) {
			outputSlot.increment(craftResult.getCount());
		}

		DefaultedList<ItemStack> results = recipe.getRemainder(blockEntity);

		for (int i = 0; i < 9; i++) {
			ItemStack stack = inventory.get(i);

			if (!stack.isEmpty()) {
				stack.decrement(1);

				if (stack.isEmpty()) {
					inventory.set(i, results.get(i));
				}
			}
		}
	}

	public void setUpgrades(List<UpgradeAndSelection> upgrades) {
		this.upgrades = upgrades;
	}

	public void setSelectorPoints(int selectorPoints) {
		this.selectorPoints = selectorPoints;
	}

	@Override
	public int size() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.inventory) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStack(int i) {
		return i >= 0 && i < this.inventory.size() ? this.inventory.get(i) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int i1, int i2) {
		return Inventories.splitStack(this.inventory, i1, i2);
	}

	@Override
	public ItemStack removeStack(int i) {
		return Inventories.removeStack(this.inventory, i);
	}

	@Override
	public void setStack(int i, ItemStack stack) {
		if (i >= 0 && i < this.inventory.size()) {
			this.inventory.set(i, stack);
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		}
		return !(player.squaredDistanceTo((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) > 64.0);
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public int[] getAvailableSlots(Direction d) {
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
	public boolean canInsert(int var1, ItemStack var2, Direction var3) {
		return this.isValid(var1, var2);
	}

	@Override
	public boolean canExtract(int var1, ItemStack var2, Direction var3) {
		return true;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		if (slot == 9) {
			return false;
		} else if (slot != 10) {
			return !(slot != 8 && this.inventory.get(8).isEmpty() && stack.getItem() instanceof SoulGemItem);
		} else {
			return stack.getItem() instanceof SoulEssenceStaff;
		}
	}

	@Override
	protected Text getContainerName() {
		return Text.translatable("container.soulmagic.soul_essence_infuser");
	}

	@Override
	protected ScreenHandler createScreenHandler(int i, PlayerInventory pi) {
		return new SoulInfuserScreenHandler(i, pi, this);
	}

	public int getProgressColor() {
		return this.progressColor;
	}

	public int getCookProgress() {
		double current = 0;
		double total = 0;

		if (!recipeSoulMap.isEmpty()) {
			for (double d : cookSoulMap.values()) {
				current += d;
			}

			for (double d : recipeSoulMap.values()) {
				total += d;
			}

			return (int) ((current / (total)) * 100);
		}

		return 0;
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(this.pos);
	}

	public List<Upgrade> getSelectedUpgrades() {
		List<Upgrade> lu = new ArrayList<>();

		for(UpgradeAndSelection u : this.upgrades) {
			if(u.selected) {
				lu.add(u.u);
			}
		}

		return lu;
	}

//	@Nullable
//	@Override
//	public Packet<ClientPlayPacketListener> toUpdatePacket() {
//		return BlockEntityUpdateS2CPacket.create(this);
//	}
//
//	@Override
//	public NbtCompound toInitialChunkDataNbt() {
//		return createNbt();
//	}

	public static class UpgradeAndSelection {
		private Upgrade u;
		private boolean selected;

		public UpgradeAndSelection(Upgrade u, boolean selected) {
			this.u = u;
			this.selected = selected;
		}
	}
}
