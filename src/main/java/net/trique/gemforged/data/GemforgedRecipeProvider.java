package net.trique.gemforged.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.item.GemforgedItems;

import java.util.List;
import java.util.function.Consumer;

public class GemforgedRecipeProvider extends FabricRecipeProvider {
    public GemforgedRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        // --- ORE SMELTING & BLASTING ---
        offerSmelting(exporter, List.of(GemforgedBlocks.NYXITE_ORE, GemforgedBlocks.DEEPSLATE_NYXITE_ORE),
                RecipeCategory.MISC, GemforgedItems.NYXITE, 1.0f, 200, "nyxite");
        offerBlasting(exporter, List.of(GemforgedBlocks.NYXITE_ORE, GemforgedBlocks.DEEPSLATE_NYXITE_ORE),
                RecipeCategory.MISC, GemforgedItems.NYXITE, 1.0f, 100, "nyxite");

        offerSmelting(exporter, List.of(GemforgedBlocks.BLOODSTONE_ORE, GemforgedBlocks.DEEPSLATE_BLOODSTONE_ORE),
                RecipeCategory.MISC, GemforgedItems.BLOODSTONE, 1.0f, 200, "bloodstone");
        offerBlasting(exporter, List.of(GemforgedBlocks.BLOODSTONE_ORE, GemforgedBlocks.DEEPSLATE_BLOODSTONE_ORE),
                RecipeCategory.MISC, GemforgedItems.BLOODSTONE, 1.0f, 100, "bloodstone");

        offerSmelting(exporter, List.of(GemforgedBlocks.SOLARIUM_ORE, GemforgedBlocks.DEEPSLATE_SOLARIUM_ORE),
                RecipeCategory.MISC, GemforgedItems.SOLARIUM, 1.0f, 200, "solarium");
        offerBlasting(exporter, List.of(GemforgedBlocks.SOLARIUM_ORE, GemforgedBlocks.DEEPSLATE_SOLARIUM_ORE),
                RecipeCategory.MISC, GemforgedItems.SOLARIUM, 1.0f, 100, "solarium");

        offerSmelting(exporter, List.of(GemforgedBlocks.VENOMYTE_ORE, GemforgedBlocks.DEEPSLATE_VENOMYTE_ORE),
                RecipeCategory.MISC, GemforgedItems.VENOMYTE, 1.0f, 200, "venomyte");
        offerBlasting(exporter, List.of(GemforgedBlocks.VENOMYTE_ORE, GemforgedBlocks.DEEPSLATE_VENOMYTE_ORE),
                RecipeCategory.MISC, GemforgedItems.VENOMYTE, 1.0f, 100, "venomyte");

        offerSmelting(exporter, List.of(GemforgedBlocks.PHOENIXTONE_ORE, GemforgedBlocks.DEEPSLATE_PHOENIXTONE_ORE),
                RecipeCategory.MISC, GemforgedItems.PHOENIXTONE, 1.0f, 200, "phoenixtone");
        offerBlasting(exporter, List.of(GemforgedBlocks.PHOENIXTONE_ORE, GemforgedBlocks.DEEPSLATE_PHOENIXTONE_ORE),
                RecipeCategory.MISC, GemforgedItems.PHOENIXTONE, 1.0f, 100, "phoenixtone");

        offerSmelting(exporter, List.of(GemforgedBlocks.PRISMYTE_ORE, GemforgedBlocks.DEEPSLATE_PRISMYTE_ORE),
                RecipeCategory.MISC, GemforgedItems.PRISMYTE, 1.0f, 200, "prismyte");
        offerBlasting(exporter, List.of(GemforgedBlocks.PRISMYTE_ORE, GemforgedBlocks.DEEPSLATE_PRISMYTE_ORE),
                RecipeCategory.MISC, GemforgedItems.PRISMYTE, 1.0f, 100, "prismyte");

        offerSmelting(exporter, List.of(GemforgedBlocks.GRAVITIUM_ORE, GemforgedBlocks.DEEPSLATE_GRAVITIUM_ORE),
                RecipeCategory.MISC, GemforgedItems.GRAVITIUM, 1.0f, 200, "gravitium");
        offerBlasting(exporter, List.of(GemforgedBlocks.GRAVITIUM_ORE, GemforgedBlocks.DEEPSLATE_GRAVITIUM_ORE),
                RecipeCategory.MISC, GemforgedItems.GRAVITIUM, 1.0f, 100, "gravitium");

        // --- STORAGE (BLOCK <-> GEM) RECIPES ---
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.NYXITE, RecipeCategory.DECORATIONS, GemforgedBlocks.NYXITE_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.BLOODSTONE, RecipeCategory.DECORATIONS, GemforgedBlocks.BLOODSTONE_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.SOLARIUM, RecipeCategory.DECORATIONS, GemforgedBlocks.SOLARIUM_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.VENOMYTE, RecipeCategory.DECORATIONS, GemforgedBlocks.VENOMYTE_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.PHOENIXTONE, RecipeCategory.DECORATIONS, GemforgedBlocks.PHOENIXTONE_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.PRISMYTE, RecipeCategory.DECORATIONS, GemforgedBlocks.PRISMYTE_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.GRAVITIUM, RecipeCategory.DECORATIONS, GemforgedBlocks.GRAVITIUM_BLOCK);

        // --- CUSTOM SHAPELESS RECIPES ---
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, GemforgedItems.SHADOWSTEP_DAGGER)
                .input(GemforgedItems.DAGGER_TEMPLATE)
                .input(GemforgedItems.NYXITE)
                .criterion(hasItem(GemforgedItems.DAGGER_TEMPLATE), conditionsFromItem(GemforgedItems.DAGGER_TEMPLATE))
                .offerTo(exporter, new Identifier("gemforged", "shadowstep_dagger_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, GemforgedItems.BATTLE_CHARM)
                .input(GemforgedItems.CHARM_TEMPLATE)
                .input(GemforgedItems.BLOODSTONE)
                .criterion(hasItem(GemforgedItems.CHARM_TEMPLATE), conditionsFromItem(GemforgedItems.CHARM_TEMPLATE))
                .offerTo(exporter, new Identifier("gemforged", "battle_charm_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, GemforgedItems.SANDBURST_STAFF)
                .input(GemforgedItems.STAFF_TEMPLATE)
                .input(GemforgedItems.SOLARIUM)
                .criterion(hasItem(GemforgedItems.STAFF_TEMPLATE), conditionsFromItem(GemforgedItems.STAFF_TEMPLATE))
                .offerTo(exporter, new Identifier("gemforged", "sandburst_staff_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, GemforgedItems.VENOMFANG_BLADE)
                .input(GemforgedItems.BLADE_TEMPLATE)
                .input(GemforgedItems.VENOMYTE)
                .criterion(hasItem(GemforgedItems.BLADE_TEMPLATE), conditionsFromItem(GemforgedItems.BLADE_TEMPLATE))
                .offerTo(exporter, new Identifier("gemforged", "venomfang_blade_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, GemforgedItems.PHOENIX_CHARM)
                .input(GemforgedItems.CHARM_TEMPLATE)
                .input(GemforgedItems.PHOENIXTONE)
                .criterion(hasItem(GemforgedItems.CHARM_TEMPLATE), conditionsFromItem(GemforgedItems.CHARM_TEMPLATE))
                .offerTo(exporter, new Identifier("gemforged", "phoenix_charm_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, GemforgedItems.THUNDER_PRISM)
                .input(GemforgedItems.PRISM_TEMPLATE)
                .input(GemforgedItems.PRISMYTE)
                .criterion(hasItem(GemforgedItems.PRISM_TEMPLATE), conditionsFromItem(GemforgedItems.PRISM_TEMPLATE))
                .offerTo(exporter, new Identifier("gemforged", "thunder_prism_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, GemforgedItems.GRAVITY_HORN)
                .input(GemforgedItems.HORN_TEMPLATE)
                .input(GemforgedItems.GRAVITIUM)
                .criterion(hasItem(GemforgedItems.HORN_TEMPLATE), conditionsFromItem(GemforgedItems.HORN_TEMPLATE))
                .offerTo(exporter, new Identifier("gemforged", "gravity_horn_crafting"));
    }
}
