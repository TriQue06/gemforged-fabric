package net.trique.gemforged;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.trique.gemforged.data.*;
import net.trique.gemforged.world.GemforgedConfiguredFeatures;
import net.trique.gemforged.world.GemforgedPlacedFeatures;

public class GemforgedDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(GemforgedBlockLootTableProvider::new);
        pack.addProvider(GemforgedRecipeProvider::new);
        pack.addProvider(GemforgedModelProvider::new);
        pack.addProvider(GemforgedWorldGenerator::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, GemforgedConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, GemforgedPlacedFeatures::bootstrap);
    }
}