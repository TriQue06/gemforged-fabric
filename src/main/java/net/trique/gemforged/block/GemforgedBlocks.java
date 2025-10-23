package net.trique.gemforged.block;

import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.trique.gemforged.Gemforged;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class GemforgedBlocks {

    public static final Block NYXITE_ORE = registerBlock("nyxite_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(3.0F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE),
                    UniformIntProvider.create(3, 7)));

    public static final Block DEEPSLATE_NYXITE_ORE = registerBlock("deepslate_nyxite_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(4.5F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.DEEPSLATE),
                    UniformIntProvider.create(3, 7)));

    public static final Block NYXITE_BLOCK = registerBlock("nyxite_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(5.0F, 6.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block BLOODSTONE_ORE = registerBlock("bloodstone_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(3.0F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE),
                    UniformIntProvider.create(3, 7)));

    public static final Block DEEPSLATE_BLOODSTONE_ORE = registerBlock("deepslate_bloodstone_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(4.5F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.DEEPSLATE),
                    UniformIntProvider.create(3, 7)));

    public static final Block BLOODSTONE_BLOCK = registerBlock("bloodstone_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(5.0F, 6.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block SOLARIUM_ORE = registerBlock("solarium_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(3.0F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE),
                    UniformIntProvider.create(3, 7)));

    public static final Block DEEPSLATE_SOLARIUM_ORE = registerBlock("deepslate_solarium_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(4.5F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.DEEPSLATE),
                    UniformIntProvider.create(3, 7)));

    public static final Block SOLARIUM_BLOCK = registerBlock("solarium_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(5.0F, 6.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block VENOMYTE_ORE = registerBlock("venomyte_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(3.0F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE),
                    UniformIntProvider.create(3, 7)));

    public static final Block DEEPSLATE_VENOMYTE_ORE = registerBlock("deepslate_venomyte_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(4.5F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.DEEPSLATE),
                    UniformIntProvider.create(3, 7)));

    public static final Block VENOMYTE_BLOCK = registerBlock("venomyte_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(5.0F, 6.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block PHOENIXTONE_ORE = registerBlock("phoenixtone_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(3.0F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE),
                    UniformIntProvider.create(3, 7)));

    public static final Block DEEPSLATE_PHOENIXTONE_ORE = registerBlock("deepslate_phoenixtone_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(4.5F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.DEEPSLATE),
                    UniformIntProvider.create(3, 7)));

    public static final Block PHOENIXTONE_BLOCK = registerBlock("phoenixtone_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(5.0F, 6.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block PRISMYTE_ORE = registerBlock("prismyte_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(3.0F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE),
                    UniformIntProvider.create(3, 7)));

    public static final Block DEEPSLATE_PRISMYTE_ORE = registerBlock("deepslate_prismyte_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(4.5F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.DEEPSLATE),
                    UniformIntProvider.create(3, 7)));

    public static final Block PRISMYTE_BLOCK = registerBlock("prismyte_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(5.0F, 6.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block GRAVITIUM_ORE = registerBlock("gravitium_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(3.0F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE),
                    UniformIntProvider.create(3, 7)));

    public static final Block DEEPSLATE_GRAVITIUM_ORE = registerBlock("deepslate_gravitium_ore",
            new ExperienceDroppingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(4.5F, 3.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.DEEPSLATE),
                    UniformIntProvider.create(3, 7)));

    public static final Block GRAVITIUM_BLOCK = registerBlock("gravitium_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(5.0F, 6.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Gemforged.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Gemforged.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        Gemforged.LOGGER.info("Registering ModBlocks for " + Gemforged.MOD_ID);
    }
}
