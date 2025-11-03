package net.trique.gemforged.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.ActionResult;
import net.trique.gemforged.effect.GemforgedEffects;

public class GlacialCharmEvents {
    public static void register() {
    AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {

        if (!(entity instanceof LivingEntity target)) {
            return ActionResult.PASS;
        }

        if (!player.hasStatusEffect(GemforgedEffects.GLACIAL_FIST)) {
            return ActionResult.PASS;
        }

        target.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS,
                20 * 5,
                2,
                true,
                true
        ));

        return ActionResult.PASS;
    });
    }
}