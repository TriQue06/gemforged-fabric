package net.trique.gemforged.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.item.gear.*;

public class GemforgedItems {
    public static final Item SHADOWSTEP_DAGGER = registerItem("shadowstep_dagger",
            new ShadowstepDaggerItem(new Item.Settings().rarity(Rarity.EPIC).maxDamage(240)));
    public static final Item BATTLE_CHARM = registerItem("battle_charm",
            new BattleCharmItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item SANDBURST_STAFF = registerItem("sandburst_staff",
            new SandburstStaffItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item VENOMFANG_BLADE = registerItem("venomfang_blade",
            new VenomfangBladeItem(new Item.Settings().rarity(Rarity.EPIC).maxDamage(240)));
    public static final Item PHOENIX_CHARM = registerItem("phoenix_charm",
            new PhoenixCharmItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item THUNDER_PRISM = registerItem("thunder_prism",
            new ThunderPrismItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC).fireproof()));
    public static final Item GRAVITY_HORN = registerItem("gravity_horn",
            new GravityHornItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item VERDANT_TOTEM = registerItem("verdant_totem",
            new VerdantTotemItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));

    public static final Item NYXITE = registerItem("nyxite",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item BLOODSTONE = registerItem("bloodstone",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item SOLARIUM = registerItem("solarium",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item VENOMYTE = registerItem("venomyte",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item PHOENIXTONE = registerItem("phoenixtone",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item PRISMYTE = registerItem("prismyte",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item GRAVITIUM = registerItem("gravitium",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item VERDANTITE = registerItem("verdantite",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static final Item DAGGER_TEMPLATE = registerItem("dagger_template",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final Item CHARM_TEMPLATE = registerItem("charm_template",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final Item STAFF_TEMPLATE = registerItem("staff_template",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final Item PRISM_TEMPLATE = registerItem("prism_template",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final Item BLADE_TEMPLATE = registerItem("blade_template",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final Item HORN_TEMPLATE = registerItem("horn_template",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final Item TOTEM_TEMPLATE = registerItem("totem_template",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Gemforged.MOD_ID, name), item);
    }

    public static void registerItems() {
        Gemforged.LOGGER.info("Registering Gemforged Items");
    }
}
