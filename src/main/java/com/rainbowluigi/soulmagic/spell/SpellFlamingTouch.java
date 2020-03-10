package com.rainbowluigi.soulmagic.spell;

import java.util.Random;

import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

public class SpellFlamingTouch extends Spell {

	private SoulType[] types = new SoulType[] {ModSoulTypes.LIGHT, ModSoulTypes.DARK};
	
	public SpellFlamingTouch() {
		super(ModSpellTypes.FIERY);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext iuc) {
		if(SoulEssenceStaff.hasSoul(iuc.getPlayer(), iuc.getWorld(), ModSoulTypes.LIGHT, 5, ModSoulTypes.DARK, 5)) {
			BlockState state = iuc.getWorld().getBlockState(iuc.getBlockPos());
			BasicInventory inv = new BasicInventory(new ItemStack(state.getBlock()));
			
			SmeltingRecipe irecipe = iuc.getWorld().getRecipeManager().getFirstMatch(RecipeType.SMELTING, inv, iuc.getWorld()).orElse(null);
			
			if(irecipe != null) {
				ItemStack result = irecipe.craft(inv);
				
				Block block = Block.getBlockFromItem(result.getItem());
				
				if (!result.isEmpty() && block != Blocks.AIR) {
					iuc.getWorld().setBlockState(iuc.getBlockPos(), block.getDefaultState());
					
					iuc.getWorld().playSound(null, iuc.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, MathHelper.nextFloat(iuc.getWorld().random, 0.5f, 1));
		
					Random random = iuc.getWorld().random;
					double d0 = 0.0625D;
		
					for (int i = 0; i < 12; ++i) {
						double d1 = (double) ((float) iuc.getBlockPos().getX() + random.nextFloat());
						double d2 = (double) ((float) iuc.getBlockPos().getY() + random.nextFloat());
						double d3 = (double) ((float) iuc.getBlockPos().getZ() + random.nextFloat());
		
						if (i == 0 && !iuc.getWorld().getBlockState(iuc.getBlockPos().up()).isOpaque())
							d2 = (double) iuc.getBlockPos().getY() + d0 + 1.0D;
						if (i == 1 && !iuc.getWorld().getBlockState(iuc.getBlockPos().down(1)).isOpaque())
							d2 = (double) iuc.getBlockPos().getY() - d0;
						if (i == 2 && !iuc.getWorld().getBlockState(iuc.getBlockPos().south()).isOpaque())
							d3 = (double) iuc.getBlockPos().getZ() + d0 + 1.0D;
						if (i == 3 && !iuc.getWorld().getBlockState(iuc.getBlockPos().north()).isOpaque())
							d3 = (double) iuc.getBlockPos().getZ() - d0;
						if (i == 4 && !iuc.getWorld().getBlockState(iuc.getBlockPos().east()).isOpaque())
							d1 = (double) iuc.getBlockPos().getX() + d0 + 1.0D;
						if (i == 5 && !iuc.getWorld().getBlockState(iuc.getBlockPos().west()).isOpaque())
							d1 = (double) iuc.getBlockPos().getX() - d0;
		
						if (d1 < (double) iuc.getBlockPos().getX() || d1 > (double) (iuc.getBlockPos().getX() + 1) || d2 < 0.0D
								|| d2 > (double) (iuc.getBlockPos().getY() + 1) || d3 < (double) iuc.getBlockPos().getZ() || d3 > (double) (iuc.getBlockPos().getZ() + 1))
							iuc.getWorld().addParticle(ParticleTypes.FLAME, d1, d2, d3, 0.0D, 0.0D, 0.0D);
					}
					
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.PASS;
	}
	
	@Override
	public SoulType[] getSoulTypesToShow() {
		return types;
	}
}
