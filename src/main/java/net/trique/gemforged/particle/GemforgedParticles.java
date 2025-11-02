package net.trique.gemforged.particle;

import net.minecraft.particle.DefaultParticleType;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.trique.gemforged.Gemforged;

public class GemforgedParticles {

    public static final DefaultParticleType PHOENIX_BEAM =
            FabricParticleTypes.simple(true);

    public static void register() {
        Registry.register(
                Registries.PARTICLE_TYPE,
                new Identifier(Gemforged.MOD_ID, "phoenix_beam"),
                PHOENIX_BEAM
        );
    }
}
