package net.trique.gemforged.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.block.GemforgedBlocks;

public class GemforgedItemGroups {
    public static final ItemGroup GEMFORGED_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Gemforged.MOD_ID, "gemforged"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.gemforged"))
                    .icon(() -> new ItemStack(GemforgedItems.BATTLE_CHARM))
                    .entries((displayContext, entries) -> {

                        entries.add(GemforgedItems.NYXITE);
                        entries.add(GemforgedItems.BLOODSTONE);
                        entries.add(GemforgedItems.SOLARIUM);
                        entries.add(GemforgedItems.VENOMYTE);
                        entries.add(GemforgedItems.PHOENIXTONE);
                        entries.add(GemforgedItems.PRISMYTE);
                        entries.add(GemforgedItems.GRAVITIUM);
                        entries.add(GemforgedBlocks.NYXITE_BLOCK);
                        entries.add(GemforgedBlocks.BLOODSTONE_BLOCK);
                        entries.add(GemforgedBlocks.SOLARIUM_BLOCK);
                        entries.add(GemforgedBlocks.VENOMYTE_BLOCK);
                        entries.add(GemforgedBlocks.PHOENIXTONE_BLOCK);
                        entries.add(GemforgedBlocks.PRISMYTE_BLOCK);
                        entries.add(GemforgedBlocks.GRAVITIUM_BLOCK);

                        entries.add(GemforgedItems.SHADOWSTEP_DAGGER);
                        entries.add(GemforgedItems.BATTLE_CHARM);
                        entries.add(GemforgedItems.SANDBURST_STAFF);
                        entries.add(GemforgedItems.VENOMFANG_BLADE);
                        entries.add(GemforgedItems.PHOENIX_CHARM);
                        entries.add(GemforgedItems.THUNDER_PRISM);
                        entries.add(GemforgedItems.GRAVITY_HORN);

                        entries.add(GemforgedItems.DAGGER_TEMPLATE);
                        entries.add(GemforgedItems.CHARM_TEMPLATE);
                        entries.add(GemforgedItems.STAFF_TEMPLATE);
                        entries.add(GemforgedItems.PRISM_TEMPLATE);
                        entries.add(GemforgedItems.BLADE_TEMPLATE);
                        entries.add(GemforgedItems.HORN_TEMPLATE);

                    }).build());

    public static void registerItemGroups() {
        Gemforged.LOGGER.info("Registering Item Groups for " + Gemforged.MOD_ID);
    }
}