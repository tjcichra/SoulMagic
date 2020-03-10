package com.rainbowluigi.soulmagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

public class SoulFlameBlock extends Block {

	public static final IntProperty RED;
	public static final IntProperty GREEN;
	public static final IntProperty BLUE;
	
	public SoulFlameBlock(Block.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(RED, 0).with(GREEN, 0).with(BLUE, 0));
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager$Builder_1) {
		stateManager$Builder_1.add(RED, GREEN, BLUE);
	}
	
	static {
		RED = IntProperty.of("red", 0, 7);
		GREEN = IntProperty.of("green", 0, 7);
		BLUE = IntProperty.of("blue", 0, 7);
	}
}
