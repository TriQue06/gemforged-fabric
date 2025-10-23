package net.trique.gemforged.world;

import net.trique.gemforged.Gemforged;
import net.trique.gemforged.block.GemforgedBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

public class GemforgedConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> NYXITE_ORE_KEY = registerKey("nyxite_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BLOODSTONE_ORE_KEY = registerKey("bloodstone_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SOLARIUM_ORE_KEY = registerKey("solarium_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> VENOMYTE_ORE_KEY = registerKey("venomyte_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PHOENIXTONE_ORE_KEY = registerKey("phoenixtone_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PRISMYTE_ORE_KEY = registerKey("prismyte_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRAVITIUM_ORE_KEY = registerKey("gravitium_ore");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> nyxiteTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, GemforgedBlocks.NYXITE_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_NYXITE_ORE.getDefaultState()));
        register(context, NYXITE_ORE_KEY, Feature.ORE, new OreFeatureConfig(nyxiteTargets, 5));

        List<OreFeatureConfig.Target> bloodstoneTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, GemforgedBlocks.BLOODSTONE_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_BLOODSTONE_ORE.getDefaultState()));
        register(context, BLOODSTONE_ORE_KEY, Feature.ORE, new OreFeatureConfig(bloodstoneTargets, 5));

        List<OreFeatureConfig.Target> solariumTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, GemforgedBlocks.SOLARIUM_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_SOLARIUM_ORE.getDefaultState()));
        register(context, SOLARIUM_ORE_KEY, Feature.ORE, new OreFeatureConfig(solariumTargets, 5));

        List<OreFeatureConfig.Target> venomyteTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, GemforgedBlocks.VENOMYTE_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_VENOMYTE_ORE.getDefaultState()));
        register(context, VENOMYTE_ORE_KEY, Feature.ORE, new OreFeatureConfig(venomyteTargets, 5));

        List<OreFeatureConfig.Target> phoenixtoneTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, GemforgedBlocks.PHOENIXTONE_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_PHOENIXTONE_ORE.getDefaultState()));
        register(context, PHOENIXTONE_ORE_KEY, Feature.ORE, new OreFeatureConfig(phoenixtoneTargets, 5));

        List<OreFeatureConfig.Target> prismyteTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, GemforgedBlocks.PRISMYTE_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_PRISMYTE_ORE.getDefaultState()));
        register(context, PRISMYTE_ORE_KEY, Feature.ORE, new OreFeatureConfig(prismyteTargets, 5));

        List<OreFeatureConfig.Target> gravitiumTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, GemforgedBlocks.GRAVITIUM_ORE.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_GRAVITIUM_ORE.getDefaultState()));
        register(context, GRAVITIUM_ORE_KEY, Feature.ORE, new OreFeatureConfig(gravitiumTargets, 5));
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(Gemforged.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
