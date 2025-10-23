package net.trique.gemforged.world;

import net.trique.gemforged.Gemforged;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

public class GemforgedPlacedFeatures {
    public static final RegistryKey<PlacedFeature> NYXITE_ORE_PLACED_KEY = registerKey("nyxite_ore_placed");
    public static final RegistryKey<PlacedFeature> BLOODSTONE_ORE_PLACED_KEY = registerKey("bloodstone_ore_placed");
    public static final RegistryKey<PlacedFeature> SOLARIUM_ORE_PLACED_KEY = registerKey("solarium_ore_placed");
    public static final RegistryKey<PlacedFeature> VENOMYTE_ORE_PLACED_KEY = registerKey("venomyte_ore_placed");
    public static final RegistryKey<PlacedFeature> PHOENIXTONE_ORE_PLACED_KEY = registerKey("phoenixtone_ore_placed");
    public static final RegistryKey<PlacedFeature> PRISMYTE_ORE_PLACED_KEY = registerKey("prismyte_ore_placed");
    public static final RegistryKey<PlacedFeature> GRAVITIUM_ORE_PLACED_KEY = registerKey("gravitium_ore_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, NYXITE_ORE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(GemforgedConfiguredFeatures.NYXITE_ORE_KEY),
                GemforgedOrePlacement.modifiersWithCount(3,
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-64), YOffset.fixed(16))));

        register(context, BLOODSTONE_ORE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(GemforgedConfiguredFeatures.BLOODSTONE_ORE_KEY),
                GemforgedOrePlacement.modifiersWithCount(3,
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-64), YOffset.fixed(16))));

        register(context, SOLARIUM_ORE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(GemforgedConfiguredFeatures.SOLARIUM_ORE_KEY),
                GemforgedOrePlacement.modifiersWithCount(3,
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-64), YOffset.fixed(16))));

        register(context, VENOMYTE_ORE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(GemforgedConfiguredFeatures.VENOMYTE_ORE_KEY),
                GemforgedOrePlacement.modifiersWithCount(3,
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-64), YOffset.fixed(16))));

        register(context, PHOENIXTONE_ORE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(GemforgedConfiguredFeatures.PHOENIXTONE_ORE_KEY),
                GemforgedOrePlacement.modifiersWithCount(3,
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-64), YOffset.fixed(16))));

        register(context, PRISMYTE_ORE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(GemforgedConfiguredFeatures.PRISMYTE_ORE_KEY),
                GemforgedOrePlacement.modifiersWithCount(3,
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-64), YOffset.fixed(16))));

        register(context, GRAVITIUM_ORE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(GemforgedConfiguredFeatures.GRAVITIUM_ORE_KEY),
                GemforgedOrePlacement.modifiersWithCount(3,
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-64), YOffset.fixed(16))));
    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(Gemforged.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                 RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
