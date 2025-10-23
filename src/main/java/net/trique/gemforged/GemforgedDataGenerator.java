package net.trique.gemforged;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.trique.gemforged.data.*;

public class GemforgedDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(GemforgedBlockLootTableProvider::new);
        pack.addProvider(GemforgedRecipeProvider::new);
        pack.addProvider(GemforgedModelProvider::new);
        pack.addProvider(GemforgedWorldGenerator::new);
    }
}
