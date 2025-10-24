package net.trique.gemforged;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.event.PhoenixRelicEvents;
import net.trique.gemforged.item.GemforgedItemGroups;
import net.trique.gemforged.item.GemforgedItems;
import net.trique.gemforged.effect.GemforgedEffects;
import net.trique.gemforged.entity.GemforgedEntities;
import net.trique.gemforged.util.GemforgedLootTableModifiers;
import net.trique.gemforged.world.GemforgedConfiguredFeatures;
import net.trique.gemforged.world.GemforgedPlacedFeatures;
import net.trique.gemforged.world.gen.GemforgedWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gemforged implements ModInitializer {
    public static final String MOD_ID = "gemforged";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Gemforged...");
        new RegistryBuilder()
                .addRegistry(RegistryKeys.CONFIGURED_FEATURE, GemforgedConfiguredFeatures::bootstrap)
                .addRegistry(RegistryKeys.PLACED_FEATURE, GemforgedPlacedFeatures::bootstrap);
        GemforgedItems.registerItems();
        GemforgedBlocks.registerModBlocks();
        GemforgedItemGroups.registerItemGroups();
        GemforgedEffects.registerEffects();
        GemforgedEntities.registerEntities();
        GemforgedLootTableModifiers.register();
        PhoenixRelicEvents.register();
        GemforgedWorldGeneration.generateGemforgedWorldGen();
        LOGGER.info("Gemforged initialized successfully!");
    }
}
