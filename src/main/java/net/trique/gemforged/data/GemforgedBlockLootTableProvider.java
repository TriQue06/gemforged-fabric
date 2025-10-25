package net.trique.gemforged.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.item.GemforgedItems;

import java.util.concurrent.CompletableFuture;

public class GemforgedBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected final RegistryWrapper.WrapperLookup registries;

    public GemforgedBlockLootTableProvider(FabricDataOutput dataOutput,
                                           CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
        this.registries = registriesFuture.join();
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
        addDrop(GemforgedBlocks.RANDOM_GEM_VEIN, this::randomOreDrop);
        addDrop(GemforgedBlocks.DEEPSLATE_RANDOM_GEM_VEIN, this::randomOreDrop);
    }

    private LootTable.Builder randomOreDrop(Block block) {
        RegistryEntry<Enchantment> fortune = registries
                .getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
                .getOrThrow(Enchantments.FORTUNE);

        LootPool.Builder pool = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1.0f))
                .with(ItemEntry.builder(GemforgedItems.NYXITE))
                .with(ItemEntry.builder(GemforgedItems.BLOODSTONE))
                .with(ItemEntry.builder(GemforgedItems.SOLARIUM))
                .with(ItemEntry.builder(GemforgedItems.VENOMYTE))
                .with(ItemEntry.builder(GemforgedItems.PHOENIXTONE))
                .with(ItemEntry.builder(GemforgedItems.PRISMYTE))
                .with(ItemEntry.builder(GemforgedItems.GRAVITIUM))
                .apply(ApplyBonusLootFunction.oreDrops(fortune))
                .apply(ExplosionDecayLootFunction.builder());

        return LootTable.builder().pool(pool);
    }
}