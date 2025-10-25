package net.trique.gemforged.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.trique.gemforged.effect.GemforgedEffects;

public class RageOverlay implements HudRenderCallback {
    private static final float BASE_ALPHA = 0.32f;
    private static final float PULSE_ALPHA = 0.16f;
    private static final int PERIOD_TICKS = 30;
    private static final int RED_RGB = 0x00FF0000;

    public static void register() {
        HudRenderCallback.EVENT.register(new RageOverlay());
    }

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        if (player == null || mc.options.getPerspective().isFirstPerson() == false) return;

        StatusEffect rageEffect = Registries.STATUS_EFFECT.get(GemforgedEffects.RAGE_ID);
        if (rageEffect == null) return;

        StatusEffectInstance eff = player.getStatusEffect((RegistryEntry<StatusEffect>) rageEffect);
        if (eff == null) return;

        long gt = player.getWorld().getTime();
        float phase = (gt % PERIOD_TICKS) / (float) PERIOD_TICKS;
        float alpha = BASE_ALPHA + PULSE_ALPHA * MathHelper.sin(phase * (float) (Math.PI * 2));
        alpha = MathHelper.clamp(alpha, 0f, 1f);

        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();

        int a = MathHelper.clamp(Math.round(alpha * 255f), 0, 255);
        int color = (a << 24) | RED_RGB;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        drawContext.fill(0, 0, width, height, color);
        RenderSystem.disableBlend();
    }
}