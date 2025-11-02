package net.trique.gemforged;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SonicBoomParticle;
import net.trique.gemforged.client.RageOverlay;
import net.trique.gemforged.client.render.GhostArrowEntityRenderer;
import net.trique.gemforged.client.render.VerdantTotemRenderer;
import net.trique.gemforged.entity.GemforgedEntities;
import net.trique.gemforged.client.render.ThunderPrismRenderer;
import net.trique.gemforged.particle.GemforgedParticles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GemforgedClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("GemforgedClient");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Gemforged client...");

        HudRenderCallback.EVENT.register(new RageOverlay());
        EntityRendererRegistry.register(GemforgedEntities.THUNDER_PRISM, ThunderPrismRenderer::new);
        EntityRendererRegistry.register(GemforgedEntities.VERDANT_TOTEM, VerdantTotemRenderer::new);
        EntityRendererRegistry.register(GemforgedEntities.GHOST_ARROW, GhostArrowEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(
                GemforgedParticles.PHOENIX_BEAM,
                SonicBoomParticle.Factory::new
        );
    }
}
