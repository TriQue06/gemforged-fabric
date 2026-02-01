package net.trique.gemforged.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.trique.gemforged.Gemforged;

public class GemforgedEntities {

    public static final EntityType<ThunderPrismEntity> THUNDER_PRISM =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(Gemforged.MOD_ID, "thunder_prism"),
                    FabricEntityTypeBuilder.<ThunderPrismEntity>create(SpawnGroup.MISC, ThunderPrismEntity::new)
                            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                            .trackRangeBlocks(8)
                            .trackedUpdateRate(1)
                            .build()
            );

    public static final EntityType<VerdantTotemEntity> VERDANT_TOTEM =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(Gemforged.MOD_ID, "verdant_totem"),
                    FabricEntityTypeBuilder.<VerdantTotemEntity>create(SpawnGroup.MISC, VerdantTotemEntity::new)
                            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                            .trackRangeBlocks(8)
                            .trackedUpdateRate(1)
                            .build()
            );

    public static final EntityType<GhostArrowEntity> GHOST_ARROW =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(Gemforged.MOD_ID, "ghost_arrow"),
                    FabricEntityTypeBuilder.<GhostArrowEntity>create(SpawnGroup.MISC, GhostArrowEntity::new)
                            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                            .trackRangeBlocks(64)
                            .trackedUpdateRate(20)
                            .build()
            );

    public static void registerEntities() {
        Gemforged.LOGGER.info("Registering entities for " + Gemforged.MOD_ID);
    }
}
