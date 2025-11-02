package net.trique.gemforged.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.entity.GhostArrowEntity;

@Environment(EnvType.CLIENT)
public class GhostArrowEntityRenderer extends ProjectileEntityRenderer<GhostArrowEntity> {

    private static final Identifier TEXTURE =
            new Identifier(Gemforged.MOD_ID, "textures/entity/projectiles/ghost_arrow.png");

    public GhostArrowEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(GhostArrowEntity entity) {
        return TEXTURE;
    }
}