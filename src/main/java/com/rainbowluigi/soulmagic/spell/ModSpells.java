package com.rainbowluigi.soulmagic.spell;

import com.mojang.serialization.Lifecycle;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ModSpells {
	
    public static final RegistryKey<Registry<Spell>> SPELL_KEY = RegistryKey.ofRegistry(new Identifier(Reference.MOD_ID, "fireball"));
    public static final DefaultedRegistry<Spell> SPELL = new DefaultedRegistry<Spell>("light", SPELL_KEY, Lifecycle.experimental());
	
	public static final Spell FIREBALL = new SpellFireball();
	public static final Spell FLAMING_TOUCH = new SpellFlamingTouch();
	public static final Spell ICEBALL = new SpellIceball();
	public static final Spell BARRAGE = new SpellBarrage();
	public static final Spell TENDRILS = new SpellTendrils();
	public static final Spell AIR_STRIKE = new SpellAirStrike();
	
	public static final Spell BOUND_PICKAXE = new SpellBound(ModItems.BOUND_PICKAXE);
	public static final Spell BOUND_PICKAXE_SILK = new SpellBound(ModItems.BOUND_PICKAXE, true, false, false);
	public static final Spell BOUND_PICKAXE_FORTUNE = new SpellBound(ModItems.BOUND_PICKAXE, false, true, false);
	public static final Spell BOUND_AXE = new SpellBound(ModItems.BOUND_AXE);
	public static final Spell BOUND_SHOVEL = new SpellBound(ModItems.BOUND_SHOVEL);
	public static final Spell BOUND_SWORD = new SpellBound(ModItems.BOUND_SWORD);
	public static final Spell BOUND_SCYTHE = new SpellBound(ModItems.BOUND_SWORD, false, false, true);

	public static void registerSpells() {
		//Registry.register(Registry.REGISTRIES, new Identifier(Reference.MOD_ID, "spell"), SPELL);

		registerSpell(FIREBALL, "fireball");
		registerSpell(FLAMING_TOUCH, "flaming_touch");
		registerSpell(ICEBALL, "iceball");
		registerSpell(BARRAGE, "barrage");
		registerSpell(TENDRILS, "tendrils");
		registerSpell(AIR_STRIKE, "air_strike");
		
		registerSpell(BOUND_PICKAXE, "bound_pickaxe");
		registerSpell(BOUND_PICKAXE_SILK, "bound_pickaxe_silk");
		registerSpell(BOUND_PICKAXE_FORTUNE, "bound_pickaxe_fortune");
		registerSpell(BOUND_AXE, "bound_axe");
		registerSpell(BOUND_SHOVEL, "bound_shovel");
		registerSpell(BOUND_SWORD, "bound_sword");
		registerSpell(BOUND_SCYTHE, "bound_scythe");
	}
    
    private static void registerSpell(Spell s, String name) {
    	Registry.register(SPELL, new Identifier(Reference.MOD_ID, name), s);
    }
}
