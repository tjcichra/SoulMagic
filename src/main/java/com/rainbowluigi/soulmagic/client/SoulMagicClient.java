package com.rainbowluigi.soulmagic.client;

import java.awt.Color;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.block.entity.ModBlockEntity;
import com.rainbowluigi.soulmagic.client.screen.SelectSpellScreen;
import com.rainbowluigi.soulmagic.entity.ModEntityTypes;
import com.rainbowluigi.soulmagic.item.BraceItem;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.item.SoulGemItem;
import com.rainbowluigi.soulmagic.network.EntityRenderMessage;
import com.rainbowluigi.soulmagic.network.ItemVacuumMessage;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.network.OpenBabulesMessage;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.spelltype.SpellType;
import com.rainbowluigi.soulmagic.util.Reference;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;
import com.rainbowluigi.soulmagic.util.SoulQuiverHelper;

import org.lwjgl.glfw.GLFW;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SoulMagicClient implements ClientModInitializer {

	public static final FabricKeyBinding SPELL_SELECT = FabricKeyBinding.Builder.create(new Identifier(Reference.MOD_ID, "spell_select"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, Reference.MOD_ID).build();
	public static final FabricKeyBinding ACCESSORY_SCREEN_KEY = FabricKeyBinding.Builder.create(new Identifier(Reference.MOD_ID, "accessory_screen_key"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, Reference.MOD_ID).build();
	public static final Identifier ACCESSORY_SCREEN = new Identifier(Reference.MOD_ID, "accessory");
	
	public static final Identifier ENTITY_RENDER = new Identifier(Reference.MOD_ID, "entity_render");
	public static final Identifier ITEM_VACUUM = new Identifier(Reference.MOD_ID, "item_vacuum");
	
	public static final Identifier SOUL_MAGIC_TEXTURE_ATLAS = new Identifier(Reference.MOD_ID, "textures/atlas/soulmagic.png");
	
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.MAGIC_FIREBALL, (manager, context) -> new MagicFireballRender(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.UNIVERSE_RING, (manager, context) -> new BlankRender(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.BARRAGE, (manager, context) -> new BlankRender(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.TENDRIL, (manager, context) -> new BlankRender(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.SOUL_ARROW_ENTITY, (manager, context) -> new SoulArrowEntityRenderer(manager));
		EntityRendererRegistry.INSTANCE.register(ModEntityTypes.SPIRIT_FLAME, (manager, context) -> new BlankRender(manager));

		KeyBindingRegistry.INSTANCE.addCategory(Reference.MOD_ID);
		KeyBindingRegistry.INSTANCE.register(SPELL_SELECT);
		KeyBindingRegistry.INSTANCE.register(ACCESSORY_SCREEN_KEY);
		
		ClientSidePacketRegistry.INSTANCE.register(ENTITY_RENDER, EntityRenderMessage::handle);
		ClientSidePacketRegistry.INSTANCE.register(ITEM_VACUUM, ItemVacuumMessage::handle);
		ClientSidePacketRegistry.INSTANCE.register(ACCESSORY_SCREEN, OpenBabulesMessage::handle);
		
		BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntity.SOUL_INFUSER, BlockEntitySpecialRendererSoulInfuser::new);
		
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_INFUSER, RenderLayer.getTranslucent());
		
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((a, c) -> {
			c.register(new Identifier(Reference.MOD_ID, "blocks/soul_infuser"));
		});
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			return 0xFFFFFF;
		}, ModItems.SOUL_ESSENCE_STAFF);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			return ((BraceItem) stack.getItem()).getColor(stack);
		}, ModItems.IRON_BRACE, ModItems.LIGHT_SOUL_BRACE, ModItems.DARK_SOUL_BRACE, ModItems.PRIDEFUL_SOUL_BRACE, ModItems.CREATIVE_BRACE);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			return SoulQuiverHelper.getSoulType(stack).getColor();
		}, ModItems.SOUL_QUIVER);
		
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
		}, ModItems.SOUL_LANTERN, ModItems.MAGICAL_BALL_OF_YARN);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			return tint == 0 ? ((DyeableItem) stack.getItem()).getColor(stack) : 0xFFFFFF;
		}, ModItems.MAGICAL_BALL_OF_YARN);
		
		ClientTickCallback.EVENT.register(e -> {
			if(SPELL_SELECT.isPressed() && !(MinecraftClient.getInstance().currentScreen instanceof SelectSpellScreen)) {
				MinecraftClient mc = MinecraftClient.getInstance();
				ClientPlayerEntity player = mc.player;
				
				ItemStack stack = player.getMainHandStack();
				if(stack.getItem() instanceof SoulGemItem) {
					mc.openScreen(new SelectSpellScreen(stack, player));
				}
			} else if(ACCESSORY_SCREEN_KEY.isPressed()) {
				ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.OPEN_ACCESSORIES, new PacketByteBuf(Unpooled.buffer()));
			}
		});
	}
}
