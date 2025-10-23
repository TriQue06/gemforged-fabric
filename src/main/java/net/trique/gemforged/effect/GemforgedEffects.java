package net.trique.gemforged.effect;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.trique.gemforged.Gemforged;

public class GemforgedEffects {
    public static final Identifier RAGE_ID = new Identifier(Gemforged.MOD_ID, "rage");
    public static final Identifier PHOENIX_ID = new Identifier(Gemforged.MOD_ID, "phoenix");

    public static final StatusEffect RAGE = register("rage", new RageEffect());
    public static final StatusEffect PHOENIX = register("phoenix", new PhoenixEffect());

    private static StatusEffect register(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Gemforged.MOD_ID, name), effect);
    }

    public static void registerEffects() {
        Gemforged.LOGGER.info("Registering Gemforged effects");
    }
}
