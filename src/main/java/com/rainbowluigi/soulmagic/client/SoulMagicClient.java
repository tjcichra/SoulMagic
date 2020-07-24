package com.rainbowluigi.soulmagic.client;

import java.awt.Color;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.block.entity.ModBlockEntity;
import com.rainbowluigi.soulmagic.client.screen.SelectSpellScreen;
import com.rainbowluigi.soulmagic.entity.ModEntityTypes;
import com.rainbowluigi.soulmagic.item.BraceItem;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.item.SoulGemItem;
import com.rainbowluigi.soulmagic.item.bound.BoundItem;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.spelltype.SpellType;
import com.rainbowluigi.soulmagic.util.Reference;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;
import com.rainbowluigi.soulmagic.util.SoulQuiverHelper;

import org.lwjgl.glfw.GLFW;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SoulMagicClient implements ClientModInitializer {

	public static final KeyBinding SPELL_SELECT = KeyBindingHelper.registerKeyBinding(new KeyBinding("soulmagic.key.select_spell", GLFW.GLFW_KEY_R, "soulmagic.key.category"));
	public static final KeyBinding ACCESSORY_SCREEN_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("soulmagic.key.accessory_screen_key", GLFW.GLFW_KEY_P, "soulmagic.key.category"));
	
	
	
	public static final Identifier SOUL_MAGIC_TEXTURE_ATLAS = new Identifier(Reference.MOD_ID, "textures/atlas/soulmagic.png");
	
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.MAGIC_FIREBALL, (manager, context) -> new MagicFireballRender(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.UNIVERSE_RING, (manager, context) -> new BlankRender(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.BARRAGE, (manager, context) -> new BlankRender(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.TENDRIL, (manager, context) -> new BlankRender(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.SOUL_ARROW_ENTITY, (manager, context) -> new SoulArrowEntityRenderer(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.SPIRIT_FLAME, (manager, context) -> new SpiritFlameRender(manager));
		
		ModNetwork.registerServerToClientPackets();
		
		BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntity.SOUL_INFUSER, BlockEntitySpecialRendererSoulInfuser::new);
		
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_ESSENCE_INFUSER, RenderLayer.getTranslucent());
		
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((a, c) -> {
			c.register(new Identifier(Reference.MOD_ID, "blocks/soul_essence_infuser"));
		});
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			return 0xFFFFFF;
		}, ModItems.SOUL_ESSENCE_STAFF);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			return ((BraceItem) stack.getItem()).getColor(stack);
		}, ModItems.IRON_BRACE, ModItems.LIGHT_SOUL_BRACE, ModItems.DARK_SOUL_BRACE, ModItems.PRIDEFUL_SOUL_BRACE, ModItems.CREATIVE_BRACE);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			return SoulQuiverHelper.getSoulType(stack).getColor();
		}, ModItems.SOUL_ESSENCE_QUIVER);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			if(tint == 0) {
				SpellType st = SoulGemHelper.getSpellType(stack);
				
				if(st != null) {
					if(st != ModSpellTypes.ULTIMATE) {
						return st.getColor();
					} else {
						return Color.HSBtoRGB(System.currentTimeMillis() % 10000 / 9999f, 1, 1);
					}
				}
			} else {
				ItemStack brace = SoulGemHelper.getBrace(stack);
				
				if(brace != null) {
					return ((BraceItem) brace.getItem()).getColor(brace);
				}
			}
			
			return 0xFFFFFF;
		}, ModItems.SOUL_GEM);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			return tint == 1 ? ((DyeableItem) stack.getItem()).getColor(stack) : 0xFFFFFF;
		}, ModItems.SOUL_ESSENCE_LANTERN, ModItems.MAGICAL_BALL_OF_YARN);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			return tint == 0 ? ((DyeableItem) stack.getItem()).getColor(stack) : 0xFFFFFF;
		}, ModItems.MAGICAL_BALL_OF_YARN);
		
		ClientTickEvents.END_CLIENT_TICK.register(e -> {
			if(SPELL_SELECT.isPressed() && !(MinecraftClient.getInstance().currentScreen instanceof SelectSpellScreen)) {
				MinecraftClient mc = MinecraftClient.getInstance();
				ClientPlayerEntity player = mc.player;
				
				ItemStack stack = player.getMainHandStack();
				if(stack.getItem() instanceof SoulGemItem) {
					mc.openScreen(new SelectSpellScreen(stack, player));
				} else if(stack.getItem() instanceof BoundItem && stack.hasTag() && stack.getTag().contains("soulGem")) {
					ItemStack gem = ItemStack.fromTag(stack.getTag().getCompound("soulGem"));
					mc.openScreen(new SelectSpellScreen(gem, player));
					CompoundTag tag = new CompoundTag();
					tag = gem.toTag(tag);
					stack.getTag().put("soulGem", tag);
				}
			} else if(ACCESSORY_SCREEN_KEY.isPressed()) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.ACCESSORIES_OPEN, new PacketByteBuf(Unpooled.buffer()));
			}
		});
	}
}
