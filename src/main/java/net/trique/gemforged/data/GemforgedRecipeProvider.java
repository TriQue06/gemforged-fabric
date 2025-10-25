package net.trique.gemforged.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.item.GemforgedItems;

import java.util.concurrent.CompletableFuture;

public class GemforgedRecipeProvider extends FabricRecipeProvider {
    public GemforgedRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.NYXITE, RecipeCategory.DECORATIONS, GemforgedBlocks.NYXITE_BLOCK);
        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.BLOODSTONE, RecipeCategory.DECORATIONS, GemforgedBlocks.BLOODSTONE_BLOCK);
        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.SOLARIUM, RecipeCategory.DECORATIONS, GemforgedBlocks.SOLARIUM_BLOCK);
        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.VENOMYTE, RecipeCategory.DECORATIONS, GemforgedBlocks.VENOMYTE_BLOCK);
        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.PHOENIXTONE, RecipeCategory.DECORATIONS, GemforgedBlocks.PHOENIXTONE_BLOCK);
        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.PRISMYTE, RecipeCategory.DECORATIONS, GemforgedBlocks.PRISMYTE_BLOCK);
        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, GemforgedItems.GRAVITIUM, RecipeCategory.DECORATIONS, GemforgedBlocks.GRAVITIUM_BLOCK);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, GemforgedItems.SHADOWSTEP_DAGGER)
                .input(GemforgedItems.DAGGER_TEMPLATE)
                .input(GemforgedItems.NYXITE)
                .criterion(hasItem(GemforgedItems.DAGGER_TEMPLATE), conditionsFromItem(GemforgedItems.DAGGER_TEMPLATE))
                .offerTo(recipeExporter, Identifier.of(Gemforged.MOD_ID, "shadowstep_dagger_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, GemforgedItems.BATTLE_CHARM)
                .input(GemforgedItems.CHARM_TEMPLATE)
                .input(GemforgedItems.BLOODSTONE)
                .criterion(hasItem(GemforgedItems.CHARM_TEMPLATE), conditionsFromItem(GemforgedItems.CHARM_TEMPLATE))
                .offerTo(recipeExporter, Identifier.of(Gemforged.MOD_ID, "battle_charm_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, GemforgedItems.SANDBURST_STAFF)
                .input(GemforgedItems.STAFF_TEMPLATE)
                .input(GemforgedItems.SOLARIUM)
                .criterion(hasItem(GemforgedItems.STAFF_TEMPLATE), conditionsFromItem(GemforgedItems.STAFF_TEMPLATE))
                .offerTo(recipeExporter, Identifier.of(Gemforged.MOD_ID, "sandburst_staff_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, GemforgedItems.VENOMFANG_BLADE)
                .input(GemforgedItems.BLADE_TEMPLATE)
                .input(GemforgedItems.VENOMYTE)
                .criterion(hasItem(GemforgedItems.BLADE_TEMPLATE), conditionsFromItem(GemforgedItems.BLADE_TEMPLATE))
                .offerTo(recipeExporter, Identifier.of(Gemforged.MOD_ID, "venomfang_blade_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, GemforgedItems.PHOENIX_CHARM)
                .input(GemforgedItems.CHARM_TEMPLATE)
                .input(GemforgedItems.PHOENIXTONE)
                .criterion(hasItem(GemforgedItems.CHARM_TEMPLATE), conditionsFromItem(GemforgedItems.CHARM_TEMPLATE))
                .offerTo(recipeExporter, Identifier.of(Gemforged.MOD_ID, "phoenix_charm_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, GemforgedItems.THUNDER_PRISM)
                .input(GemforgedItems.PRISM_TEMPLATE)
                .input(GemforgedItems.PRISMYTE)
                .criterion(hasItem(GemforgedItems.PRISM_TEMPLATE), conditionsFromItem(GemforgedItems.PRISM_TEMPLATE))
                .offerTo(recipeExporter, Identifier.of(Gemforged.MOD_ID, "thunder_prism_crafting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, GemforgedItems.GRAVITY_HORN)
                .input(GemforgedItems.HORN_TEMPLATE)
                .input(GemforgedItems.GRAVITIUM)
                .criterion(hasItem(GemforgedItems.HORN_TEMPLATE), conditionsFromItem(GemforgedItems.HORN_TEMPLATE))
                .offerTo(recipeExporter, Identifier.of(Gemforged.MOD_ID, "gravity_horn_crafting"));
    }
}