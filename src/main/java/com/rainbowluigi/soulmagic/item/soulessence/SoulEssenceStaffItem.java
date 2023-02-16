package com.rainbowluigi.soulmagic.item.soulessence;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SoulEssenceStaffItem extends Item implements SoulEssenceStaff, Upgradeable {

    public SoulEssenceStaffItem(Item.Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        for (SoulType st : ModSoulTypes.SOUL_TYPE) {
            if (this.getSoul(stack, world, st) > 0) {
                tooltip.add(Text.translatable("soulmagic.soul_essence_staff.amount", st.getName(), this.getSoul(stack, world, st), this.getMaxSoul(stack, world, st)).formatted(st.getTextColor()));
            }
        }
    }

    // Adds empty and full soul essence staffs to the creative menu
    public void addToCreativeMenu(FabricItemGroupEntries entries) {
        entries.add(this);

        MinecraftClient client = MinecraftClient.getInstance();
        ItemStack stack = new ItemStack(this);
        for (SoulType type : ModSoulTypes.SOUL_TYPE) {
            this.setSoul(stack, null, type, this.getMaxSoul(stack, client.world, type));
        }

        entries.add(stack);
    }

    @Override
    public int getMaxSoul(ItemStack stack, World world, SoulType type) {
        float multiplier = 1;

        if (this.hasUpgradeSelected(stack, ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_1)) {
            multiplier += 0.30;
        }

        if (this.hasUpgradeSelected(stack, ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_2)) {
            multiplier += 0.20;
        }

        return (int) (100 * multiplier);
    }

    @Override
    public List<Upgrade> getPossibleUpgrades(ItemStack stack) {
        return Arrays.asList(ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_1, ModUpgrades.SOUL_ESSENCE_STAFF_INCREASE_2);
    }

    // If soul essence staff upgrades are removed, ensure that the current soul essence is not over the max soul essence
    @Override
    public void onUnselection(ItemStack stack, World w, Upgrade u) {
        for (SoulType type : ModSoulTypes.SOUL_TYPE) {
            int maxSoulEssence = this.getMaxSoul(stack, w, type);

            if (this.getSoul(stack, w, type) > maxSoulEssence) {
                this.setSoul(stack, w, type, maxSoulEssence);
            }
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        BlockPos clickedBlockPos = context.getBlockPos();
        BlockState clickedBlockState = world.getBlockState(clickedBlockPos);
        Block clickedBlock = clickedBlockState.getBlock();

        BlockPos plankBlockPos = null;
        BlockState plankBlockState = null;

        BlockPos bookshelfBlockPos = null;
        BlockState bookshelfBlockState = null;

        Direction[] directions = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

        if (clickedBlockState.isIn(BlockTags.PLANKS)) {
            plankBlockPos = clickedBlockPos;
            plankBlockState = clickedBlockState;
            for (Direction direction : directions) {
                BlockPos adjacentBlockPos = plankBlockPos.offset(direction);
                BlockState adjacentBlockState = world.getBlockState(adjacentBlockPos);

                if (adjacentBlockState.getBlock().equals(Blocks.BOOKSHELF)) {
                    bookshelfBlockPos = adjacentBlockPos;
                    bookshelfBlockState = adjacentBlockState;
                }
            }
        } else if (clickedBlock.equals(Blocks.BOOKSHELF)) {
            bookshelfBlockPos = clickedBlockPos;
            bookshelfBlockState = clickedBlockState;
            for (Direction direction : directions) {
                BlockPos adjacentBlockPos = bookshelfBlockPos.offset(direction);
                BlockState adjacentBlockState = world.getBlockState(adjacentBlockPos);

                if (adjacentBlockState.isIn(BlockTags.PLANKS)) {
                    plankBlockPos = adjacentBlockPos;
                    plankBlockState = adjacentBlockState;
                }
            }
        } else {
            super.useOnBlock(context);
        }

        if (plankBlockPos == null || bookshelfBlockPos == null) {
            return ActionResult.FAIL;
        }

        BlockPos abovePlankBlockPos = plankBlockPos.up();
        BlockState abovePlankBlockState = world.getBlockState(abovePlankBlockPos);

        BlockPos aboveBookshelfBlockPos = bookshelfBlockPos.up();
        BlockState aboveBookshelfBlockState = world.getBlockState(aboveBookshelfBlockPos);

        if (!abovePlankBlockState.isIn(BlockTags.WOOL_CARPETS) || !aboveBookshelfBlockState.isIn(BlockTags.WOOL_CARPETS)) {
            return ActionResult.FAIL;
        }

        world.addBlockBreakParticles(plankBlockPos, plankBlockState);
        world.addBlockBreakParticles(bookshelfBlockPos, bookshelfBlockState);
        world.addBlockBreakParticles(abovePlankBlockPos, abovePlankBlockState);
        world.addBlockBreakParticles(aboveBookshelfBlockPos, aboveBookshelfBlockState);

        world.removeBlock(abovePlankBlockPos, false);
        world.removeBlock(aboveBookshelfBlockPos, false);
        world.setBlockState(plankBlockPos, ModBlocks.UPGRADE_STATION.getDefaultState());
        world.setBlockState(bookshelfBlockPos, ModBlocks.UPGRADE_STATION.getDefaultState());

        Random random = world.random;
        world.playSoundAtBlockCenter(plankBlockPos, plankBlockState.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1.0f, random.nextFloat() * 0.1f + 0.9f, false);
        world.playSoundAtBlockCenter(bookshelfBlockPos, bookshelfBlockState.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1.0f, random.nextFloat() * 0.1f + 0.9f, false);
        world.playSoundAtBlockCenter(abovePlankBlockPos, abovePlankBlockState.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1.0f, random.nextFloat() * 0.1f + 0.9f, false);
        world.playSoundAtBlockCenter(aboveBookshelfBlockPos, aboveBookshelfBlockState.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1.0f, random.nextFloat() * 0.1f + 0.9f, false);

        return ActionResult.SUCCESS;
    }
}
