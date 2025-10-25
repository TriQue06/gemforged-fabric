package net.trique.gemforged.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import net.trique.gemforged.world.GemforgedPlacedFeatures;

public class GemforgedOreGeneration {
    public static void generateOres() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, GemforgedPlacedFeatures.RANDOM_GEM_VEIN_PLACED_KEY);
    }
}