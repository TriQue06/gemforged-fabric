package net.trique.gemforged.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.item.GemforgedItems;

public class GemforgedBlockLootTableProvider extends FabricBlockLootTableProvider {
    public GemforgedBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(GemforgedBlocks.NYXITE_BLOCK);
        addDrop(GemforgedBlocks.BLOODSTONE_BLOCK);
        addDrop(GemforgedBlocks.SOLARIUM_BLOCK);
        addDrop(GemforgedBlocks.VENOMYTE_BLOCK);
        addDrop(GemforgedBlocks.PHOENIXTONE_BLOCK);
        addDrop(GemforgedBlocks.PRISMYTE_BLOCK);
        addDrop(GemforgedBlocks.GRAVITIUM_BLOCK);

        addDrop(GemforgedBlocks.RANDOM_GEM_VEIN, block -> randomOreDrop(block));
        addDrop(GemforgedBlocks.DEEPSLATE_RANDOM_GEM_VEIN, block -> randomOreDrop(block));
    }

    private LootTable.Builder randomOreDrop(Block block) {
        LootPool.Builder pool = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1.0f))
                .with(ItemEntry.builder(GemforgedItems.NYXITE))
                .with(ItemEntry.builder(GemforgedItems.BLOODSTONE))
                .with(ItemEntry.builder(GemforgedItems.SOLARIUM))
                .with(ItemEntry.builder(GemforgedItems.VENOMYTE))
                .with(ItemEntry.builder(GemforgedItems.PHOENIXTONE))
                .with(ItemEntry.builder(GemforgedItems.PRISMYTE))
                .with(ItemEntry.builder(GemforgedItems.GRAVITIUM))
                .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
                .apply(ExplosionDecayLootFunction.builder());

        return LootTable.builder().pool(pool);
    }
}