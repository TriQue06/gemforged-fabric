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
    public static final RegistryKey<ConfiguredFeature<?, ?>> RANDOM_GEM_VEIN_KEY = registerKey("random_gem_vein");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> randomGemTargets = List.of(
                OreFeatureConfig.createTarget(stoneReplaceables, GemforgedBlocks.RANDOM_GEM_VEIN.getDefaultState()),
                OreFeatureConfig.createTarget(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_RANDOM_GEM_VEIN.getDefaultState()));
        register(context, RANDOM_GEM_VEIN_KEY, Feature.ORE, new OreFeatureConfig(randomGemTargets, 5));
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(Gemforged.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}