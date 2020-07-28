package com.rainbowluigi.soulmagic.spelltype;

import java.util.Arrays;

import com.mojang.serialization.Lifecycle;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ModSpellTypes {
	
    public static final RegistryKey<Registry<SpellType>> SPELL_TYPE_KEY= RegistryKey.ofRegistry(new Identifier(Reference.MOD_ID, "spell_type"));
    public static final DefaultedRegistry<SpellType> SPELL_TYPE = new DefaultedRegistry<SpellType>("light", SPELL_TYPE_KEY, Lifecycle.experimental());

	public static final SpellType FIERY = new SpellType(0xEA3600, ModUpgrades.FIREBALL, Arrays.asList(ModUpgrades.FIREBALL, ModUpgrades.TRIPLE_FIREBALL, ModUpgrades.FLAMING_TOUCH));
	public static final SpellType ICY = new SpellType(0x34EDFF, ModUpgrades.FIREBALL, Arrays.asList(ModUpgrades.FIREBALL));
	public static final SpellType EARTHEN = new SpellType(0x89694A, ModUpgrades.FIREBALL, Arrays.asList(ModUpgrades.FIREBALL));
	public static final SpellType WINDY = new SpellType(0xF7FF96, ModUpgrades.FIREBALL, Arrays.asList(ModUpgrades.FIREBALL));
	public static final SpellType BINDING = new SpellType(0x72E9D9, ModUpgrades.BOUND_PICKAXE, Arrays.asList(ModUpgrades.BOUND_PICKAXE, ModUpgrades.BOUND_AXE, ModUpgrades.BOUND_SHOVEL, ModUpgrades.BOUND_SWORD, ModUpgrades.BOUND_SCYTHE, ModUpgrades.TOOL_SWITCHING, ModUpgrades.BOUND_ENCHANTMENTS));
	public static final SpellType LIGHTENED = new SpellType(0xEEC56A, ModUpgrades.FIREBALL, Arrays.asList(ModUpgrades.FIREBALL));
	public static final SpellType DARKENED = new SpellType(0x3D2E4C, ModUpgrades.FIREBALL, Arrays.asList(ModUpgrades.FIREBALL));
	public static final SpellType ULTIMATE = new SpellType(0xAAAAAA, ModUpgrades.FIREBALL, Arrays.asList(ModUpgrades.FIREBALL));
	
	public static void registerSpellTypes() {
		//Registry.register(Registry.REGISTRIES, new Identifier(Reference.MOD_ID, "spell_type"), SPELL_TYPE_REG);

		registerSpellType(FIERY, "fiery");
		registerSpellType(ICY, "icy");
		registerSpellType(EARTHEN, "earthen");
		registerSpellType(WINDY, "windy");
		registerSpellType(BINDING, "binding");
		registerSpellType(LIGHTENED, "lightened");
		registerSpellType(DARKENED, "darkened");
		registerSpellType(ULTIMATE, "ultimate");
	}

	private static void registerSpellType(SpellType s, String name) {
    	Registry.register(SPELL_TYPE, new Identifier(Reference.MOD_ID, name), s);
    }
}
