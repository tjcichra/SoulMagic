package com.rainbowluigi.soulmagic.item;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class ItemBlindedRage extends Item implements Accessory {

	private AccessoryType type;
	
	public ItemBlindedRage(Settings setting, AccessoryType type) {
		super(setting);
		this.type = type;
	}
	
	public ItemBlindedRage(Settings setting) {
		this(setting, null);
	}
	
	@Override
	public AccessoryType getType() {
		return this.type;
	}
	
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> list, TooltipContext tooltipcontext) {
		list.add(new LiteralText("Increases magical strength at the"));
		list.add(new LiteralText("cost of less magical efficency."));
	}
}