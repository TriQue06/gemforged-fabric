package net.trique.gemforged.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.client.gl.Uniform;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
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
        addDrop(GemforgedBlocks.VERDANTITE_BLOCK);

    }

    public LootTable.Builder oreDrops(Block block, Item item) {
        return BlockLootTableGenerator.dropsWithSilkTouch(block, (LootPoolEntry.Builder<?>) this.applyExplosionDecay(block,
                ((LeafEntry.Builder<?>) ItemEntry.builder(item)
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE)))));
    }
}
