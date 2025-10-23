package net.trique.gemforged;

import net.fabricmc.api.ModInitializer;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.event.PhoenixRelicEvents;
import net.trique.gemforged.item.GemforgedItemGroups;
import net.trique.gemforged.item.GemforgedItems;
import net.trique.gemforged.effect.GemforgedEffects;
import net.trique.gemforged.entity.GemforgedEntities;
import net.trique.gemforged.util.GemforgedLootTableModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gemforged implements ModInitializer {
    public static final String MOD_ID = "gemforged";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Gemforged...");

        GemforgedItems.registerItems();
        GemforgedBlocks.registerModBlocks();
        GemforgedItemGroups.registerItemGroups();
        GemforgedEffects.registerEffects();
        GemforgedEntities.registerEntities();
        GemforgedLootTableModifiers.register();
        PhoenixRelicEvents.register();

        LOGGER.info("Gemforged initialized successfully!");
    }
}
