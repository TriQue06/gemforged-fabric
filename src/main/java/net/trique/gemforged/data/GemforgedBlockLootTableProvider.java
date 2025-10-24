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
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
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

        addDrop(GemforgedBlocks.NYXITE_ORE, oreDrops(GemforgedBlocks.NYXITE_ORE, GemforgedItems.NYXITE));
        addDrop(GemforgedBlocks.DEEPSLATE_NYXITE_ORE, oreDrops(GemforgedBlocks.DEEPSLATE_NYXITE_ORE, GemforgedItems.NYXITE));

        addDrop(GemforgedBlocks.BLOODSTONE_ORE, oreDrops(GemforgedBlocks.BLOODSTONE_ORE, GemforgedItems.BLOODSTONE));
        addDrop(GemforgedBlocks.DEEPSLATE_BLOODSTONE_ORE, oreDrops(GemforgedBlocks.DEEPSLATE_BLOODSTONE_ORE, GemforgedItems.BLOODSTONE));

        addDrop(GemforgedBlocks.SOLARIUM_ORE, oreDrops(GemforgedBlocks.SOLARIUM_ORE, GemforgedItems.SOLARIUM));
        addDrop(GemforgedBlocks.DEEPSLATE_SOLARIUM_ORE, oreDrops(GemforgedBlocks.DEEPSLATE_SOLARIUM_ORE, GemforgedItems.SOLARIUM));

        addDrop(GemforgedBlocks.VENOMYTE_ORE, oreDrops(GemforgedBlocks.VENOMYTE_ORE, GemforgedItems.VENOMYTE));
        addDrop(GemforgedBlocks.DEEPSLATE_VENOMYTE_ORE, oreDrops(GemforgedBlocks.DEEPSLATE_VENOMYTE_ORE, GemforgedItems.VENOMYTE));

        addDrop(GemforgedBlocks.PHOENIXTONE_ORE, oreDrops(GemforgedBlocks.PHOENIXTONE_ORE, GemforgedItems.PHOENIXTONE));
        addDrop(GemforgedBlocks.DEEPSLATE_PHOENIXTONE_ORE, oreDrops(GemforgedBlocks.DEEPSLATE_PHOENIXTONE_ORE, GemforgedItems.PHOENIXTONE));

        addDrop(GemforgedBlocks.PRISMYTE_ORE, oreDrops(GemforgedBlocks.PRISMYTE_ORE, GemforgedItems.PRISMYTE));
        addDrop(GemforgedBlocks.DEEPSLATE_PRISMYTE_ORE, oreDrops(GemforgedBlocks.DEEPSLATE_PRISMYTE_ORE, GemforgedItems.PRISMYTE));

        addDrop(GemforgedBlocks.GRAVITIUM_ORE, oreDrops(GemforgedBlocks.GRAVITIUM_ORE, GemforgedItems.GRAVITIUM));
        addDrop(GemforgedBlocks.DEEPSLATE_GRAVITIUM_ORE, oreDrops(GemforgedBlocks.DEEPSLATE_GRAVITIUM_ORE, GemforgedItems.GRAVITIUM));
    }

    public LootTable.Builder oreDrops(Block block, Item item) {
        return BlockLootTableGenerator.dropsWithSilkTouch(block, (LootPoolEntry.Builder<?>) this.applyExplosionDecay(block,
                ((LeafEntry.Builder<?>) ItemEntry.builder(item)
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE)))));
    }
}
