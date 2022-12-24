package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.block.entity.ModBlockEntity;
import com.rainbowluigi.soulmagic.client.screen.*;
import com.rainbowluigi.soulmagic.entity.ModEntityTypes;
import com.rainbowluigi.soulmagic.item.BraceItem;
import com.rainbowluigi.soulmagic.item.CircleSelection;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.util.Reference;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;
import com.rainbowluigi.soulmagic.util.SoulQuiverHelper;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static com.rainbowluigi.soulmagic.inventory.ModScreenHandlerTypes.*;

@Environment(EnvType.CLIENT)
public class SoulMagicClient implements ClientModInitializer {

	public static final KeyBinding SPELL_SELECT = KeyBindingHelper.registerKeyBinding(new KeyBinding("soulmagic.key.select_spell", GLFW.GLFW_KEY_R, "soulmagic.key.category"));
	public static final KeyBinding ACCESSORY_SCREEN_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("soulmagic.key.accessory_screen_key", GLFW.GLFW_KEY_P, "soulmagic.key.category"));
	
	
	
	public static final Identifier SOUL_MAGIC_TEXTURE_ATLAS = new Identifier(Reference.MOD_ID, "textures/atlas/soulmagic.png");
	
	@Override
	public void onInitializeClient() {
		HandledScreens.register(SOUL_ESSENCE_INFUSER, SoulEssenceInfuserScreen::new);
		HandledScreens.register(SOUL_ESSENCE_SEPARATOR, SoulSeparatorScreen::new);
		HandledScreens.register(SOUL_STAFF_CACHE, SoulCacheScreen::new);
		HandledScreens.register(UPGRADE_STATION, UpgradeStationScreen::new);
		HandledScreens.register(ACCESSORIES, AccessoryScreen::new);
		HandledScreens.register(FLYING_CHEST, FlyingChestScreen::new);

		EntityRendererRegistry.register(ModEntityTypes.MAGIC_FIREBALL, MagicFireballRender::new);
		EntityRendererRegistry.register(ModEntityTypes.UNIVERSE_RING, BlankRender::new);
		EntityRendererRegistry.register(ModEntityTypes.BARRAGE, BlankRender::new);
		EntityRendererRegistry.register(ModEntityTypes.TENDRIL, BlankRender::new);
		EntityRendererRegistry.register(ModEntityTypes.SOUL_ARROW_ENTITY, SoulArrowEntityRenderer::new);
		EntityRendererRegistry.register(ModEntityTypes.SPIRIT_FLAME, SpiritFlameRender::new);
		
		ModNetwork.registerServerToClientPackets();
		
		BlockEntityRendererRegistry.register(ModBlockEntity.SOUL_INFUSER, BlockEntitySpecialRendererSoulInfuser::new);
		
//		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_ESSENCE_INFUSER, RenderLayer.getTranslucent());
		
//		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((a, c) -> {
//			c.register(new Identifier(Reference.MOD_ID, "blocks/soul_essence_infuser"));
//		});

		ColorProviderRegistry.ITEM.register(SoulStaffColorProvider::getColor, ModItems.SOUL_ESSENCE_STAFF);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> ((BraceItem) stack.getItem()).getColor(stack), ModItems.IRON_BRACE, ModItems.LIGHT_SOUL_BRACE, ModItems.DARK_SOUL_BRACE, ModItems.PRIDEFUL_SOUL_BRACE, ModItems.CREATIVE_BRACE);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> SoulQuiverHelper.getSoulType(stack).getColor(), ModItems.SOUL_ESSENCE_QUIVER);
		
		ColorProviderRegistry.ITEM.register((stack, tint) -> {
			if(tint == 0) {
//				SpellType st = SoulGemHelper.getSpellType(stack);
//
//				if(st != null) {
//					if(st != ModSpellTypes.ULTIMATE) {
//						return st.getColor();
//					} else {
//						return Color.HSBtoRGB(System.currentTimeMillis() % 10000 / 9999f, 1, 1);
//					}
//				}
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
			if(SPELL_SELECT.isPressed() && !(MinecraftClient.getInstance().currentScreen instanceof CircleSelectionScreen)) {
				MinecraftClient mc = MinecraftClient.getInstance();
				ClientPlayerEntity player = mc.player;
				
				ItemStack stack = player.getMainHandStack();
				if(stack.getItem() instanceof CircleSelection) {
					mc.setScreen(new CircleSelectionScreen((CircleSelection) stack.getItem(), stack));
				}
			} else if(ACCESSORY_SCREEN_KEY.isPressed()) {
				ClientPlayNetworking.send(ModNetwork.ACCESSORIES_OPEN, new PacketByteBuf(Unpooled.buffer()));
			}
		});
	}
}
