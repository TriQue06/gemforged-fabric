package net.trique.gemforged.util;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import net.trique.gemforged.item.GemforgedItems;

public final class GemforgedLootTableModifiers {
    private GemforgedLootTableModifiers() {}

    private static final Identifier ANCIENT_CITY_CHEST = new Identifier("minecraft", "chests/ancient_city");
    private static final Identifier BASTION_BRIDGE = new Identifier("minecraft", "chests/bastion_bridge");
    private static final Identifier BASTION_HOGLIN = new Identifier("minecraft", "chests/bastion_hoglin_stable");
    private static final Identifier BASTION_OTHER = new Identifier("minecraft", "chests/bastion_other");
    private static final Identifier BASTION_TREASURE = new Identifier("minecraft", "chests/bastion_treasure");
    private static final Identifier DESERT_PYRAMID = new Identifier("minecraft", "chests/desert_pyramid");
    private static final Identifier JUNGLE_TEMPLE = new Identifier("minecraft", "chests/jungle_temple");
    private static final Identifier END_CITY_TREASURE = new Identifier("minecraft", "chests/end_city_treasure");
    private static final Identifier OCEAN_RUIN_COLD_ARCH = new Identifier("minecraft", "archaeology/ocean_ruin_cold");
    private static final Identifier PILLAGER_OUTPOST = new Identifier("minecraft", "chests/pillager_outpost");
    private static final Identifier WOODLAND_MANSION = new Identifier("minecraft", "chests/woodland_mansion");

    public static void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.equals(ANCIENT_CITY_CHEST)) {
                tableBuilder.pool(makePoolWithChance(ItemEntry.builder(GemforgedItems.DAGGER_TEMPLATE), 0.25f));
            } else if (id.equals(BASTION_BRIDGE) || id.equals(BASTION_HOGLIN) || id.equals(BASTION_OTHER)) {
                tableBuilder.pool(makePoolWithChance(ItemEntry.builder(GemforgedItems.CHARM_TEMPLATE), 0.25f));
            } else if (id.equals(BASTION_TREASURE)) {
                tableBuilder.pool(makePoolWithChance(ItemEntry.builder(GemforgedItems.CHARM_TEMPLATE), 0.50f));
            } else if (id.equals(DESERT_PYRAMID)) {
                tableBuilder.pool(makePoolWithChance(ItemEntry.builder(GemforgedItems.STAFF_TEMPLATE), 0.10f));
            } else if (id.equals(JUNGLE_TEMPLE)) {
                tableBuilder.pool(makePoolWithChance(ItemEntry.builder(GemforgedItems.BLADE_TEMPLATE), 0.25f));
            } else if (id.equals(OCEAN_RUIN_COLD_ARCH)) {
                tableBuilder.pool(makePoolWithChance(ItemEntry.builder(GemforgedItems.PRISM_TEMPLATE), 0.25f));
            } else if (id.equals(END_CITY_TREASURE)) {
                tableBuilder.pool(makePoolWithChance(ItemEntry.builder(GemforgedItems.HORN_TEMPLATE), 0.25f));
            } else if (id.equals(PILLAGER_OUTPOST)) {
                tableBuilder.pool(makePoolWithChance(ItemEntry.builder(GemforgedItems.TOTEM_TEMPLATE), 0.25f));
            } else if (id.equals(WOODLAND_MANSION)) {
                tableBuilder.pool(makePoolWithChance(ItemEntry.builder(GemforgedItems.BOW_TEMPLATE), 0.50f));
            }
        });
    }

    private static LootPool makePoolWithChance(ItemEntry.Builder<?> entry, float chance) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(chance))
                .with(entry)
                .build();
    }
}