package net.trique.gemforged.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class GlacialFistEffect extends StatusEffect {

    private static final UUID ATK_KB_UUID =
            UUID.fromString("d91fb35a-0924-4e7d-98b7-4cb05e09a2c7");

    private static final double EXTRA_KNOCKBACK = 16.00D;

    public GlacialFistEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x7ED6FF);

        this.addAttributeModifier(
                EntityAttributes.GENERIC_ATTACK_KNOCKBACK,
                ATK_KB_UUID.toString(),
                EXTRA_KNOCKBACK,
                EntityAttributeModifier.Operation.ADDITION
        );
    }
}