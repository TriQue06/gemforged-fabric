package net.trique.gemforged.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class RageEffect extends StatusEffect {
    private static final UUID MOVE_UUID = UUID.fromString("6f5b32c9-0a5e-4f7b-94a3-6c814a0cc731");
    private static final UUID ATKSPD_UUID = UUID.fromString("f8b7a83e-41e2-4a5c-b5e3-ef16c8a7c2f9");
    private static final UUID ATKDAM_UUID = UUID.fromString("e36d9b91-b2a1-4d82-a48d-b89e5bb16a12");

    private static final double BASE_MOVE_MULT = 0.50D;
    private static final double BASE_ATKDAM_MULT = 0.75D;
    private static final double BASE_ATKSPD_MULT = 1.00D;

    public RageEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xB80E2A);
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, MOVE_UUID.toString(),
                BASE_MOVE_MULT, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, ATKSPD_UUID.toString(),
                BASE_ATKSPD_MULT, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, ATKDAM_UUID.toString(),
                BASE_ATKDAM_MULT, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
