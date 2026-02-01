package net.trique.gemforged.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.item.GemforgedItems;

public class GemforgedModelProvider extends FabricModelProvider {
    public GemforgedModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.NYXITE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.BLOODSTONE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.SOLARIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.VENOMYTE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.PHOENIXTONE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.PRISMYTE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.GRAVITIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.VERDANTITE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.RANDOM_GEM_VEIN);
        blockStateModelGenerator.registerSimpleCubeAll(GemforgedBlocks.DEEPSLATE_RANDOM_GEM_VEIN);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(GemforgedItems.SHADOWSTEP_DAGGER, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.BATTLE_CHARM, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.SANDBURST_STAFF, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.VENOMFANG_BLADE, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.PHOENIX_CHARM, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.THUNDER_PRISM, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.GRAVITY_HORN, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.VERDANT_TOTEM, Models.GENERATED);
        itemModelGenerator.register(GemforgedItems.PHOENIXFIRE_STAFF, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.GLACIAL_CHARM, Models.HANDHELD);

        itemModelGenerator.register(GemforgedItems.NYXITE, Models.GENERATED);
        itemModelGenerator.register(GemforgedItems.BLOODSTONE, Models.GENERATED);
        itemModelGenerator.register(GemforgedItems.SOLARIUM, Models.GENERATED);
        itemModelGenerator.register(GemforgedItems.VENOMYTE, Models.GENERATED);
        itemModelGenerator.register(GemforgedItems.PHOENIXTONE, Models.GENERATED);
        itemModelGenerator.register(GemforgedItems.PRISMYTE, Models.GENERATED);
        itemModelGenerator.register(GemforgedItems.GRAVITIUM, Models.GENERATED);
        itemModelGenerator.register(GemforgedItems.VERDANTITE, Models.GENERATED);

        itemModelGenerator.register(GemforgedItems.DAGGER_TEMPLATE, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.CHARM_TEMPLATE, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.STAFF_TEMPLATE, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.PRISM_TEMPLATE, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.BLADE_TEMPLATE, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.HORN_TEMPLATE, Models.HANDHELD);
        itemModelGenerator.register(GemforgedItems.TOTEM_TEMPLATE, Models.GENERATED);
        itemModelGenerator.register(GemforgedItems.BOW_TEMPLATE, Models.HANDHELD);
    }
}