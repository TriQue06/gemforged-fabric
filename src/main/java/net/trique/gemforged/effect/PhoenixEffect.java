package net.trique.gemforged.effect;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhoenixEffect extends StatusEffect {
    private static final double BASE_MOVE_MULT = 0.25D;
    private static final UUID MOVE_UUID = UUID.fromString("cc6d29f1-8c8a-4b12-9b1e-8fb45026a781");
    private static final Map<UUID, SavedState> STATES = new HashMap<>();

    private static final DustParticleEffect RED =
            new DustParticleEffect(new Vector3f(1.00f, 0.05f, 0.02f), 2.0f);
    private static final DustParticleEffect ORANGE =
            new DustParticleEffect(new Vector3f(0.7725f, 0.2353f, 0.0627f), 2.0f);
    private static final DustParticleEffect YELLOW =
            new DustParticleEffect(new Vector3f(0.9725f, 0.7294f, 0.3843f), 2.0f);

    public PhoenixEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xFF7A00);
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, MOVE_UUID.toString(),
                BASE_MOVE_MULT, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            if (entity instanceof PlayerEntity player && player.hasStatusEffect(GemforgedEffects.PHOENIX)) {
                if (PhoenixEffect.tryRevive(player)) return false;
            }
            return true;
        });
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            Vec3d pos = player.getPos();
            STATES.put(player.getUuid(),
                    new SavedState(pos, player.getYaw(), player.getPitch(), player.getHealth()));
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            STATES.remove(player.getUuid());
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!(entity.getWorld() instanceof ServerWorld level)) return;

        Vec3d look = entity.getRotationVec(1.0f);
        Vec3d pos = entity.getPos();

        double speed = entity.getVelocity().length();
        int densityBoost = (int) Math.min(10, Math.floor(speed * 25.0));
        int perSegParticles = 6 + densityBoost;
        int segments = 3;
        double segStep = 0.5;

        double backDist = 0.75 + entity.getRandom().nextDouble() * 0.35;
        double baseX = pos.x - look.x * backDist;
        double baseY = pos.y + 0.55;
        double baseZ = pos.z - look.z * backDist;

        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d side = up.crossProduct(look).normalize();
        Vec3d upTilt = look.crossProduct(side).normalize();

        for (int s = 0; s < segments; s++) {
            double tBack = s * segStep;
            double cx = baseX - look.x * tBack;
            double cy = baseY - 0.05 * s;
            double cz = baseZ - look.z * tBack;

            double r = 0.24 + 0.06 * s;

            for (int i = 0; i < perSegParticles; i++) {
                double a = (Math.PI * 2 * i) / perSegParticles + entity.age * 0.28;
                Vec3d offset = side.multiply(r * MathHelper.cos((float) a))
                        .add(upTilt.multiply(r * MathHelper.sin((float) a) * 1.8));

                double px = cx + offset.x;
                double py = cy + offset.y;
                double pz = cz + offset.z;

                int sel = (s + i) % 3;
                DustParticleEffect dust = (sel == 0) ? RED : (sel == 1) ? ORANGE : YELLOW;
                level.spawnParticles(dust, px, py, pz, 1, 0, 0, 0, 0);
            }
        }

        if (entity.age % 50 == 0) {
            level.playSound(null, pos.x, pos.y, pos.z,
                    SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS,
                    0.5f, 0.10f + entity.getRandom().nextFloat() * 0.20f);
        }
    }

    public static boolean tryRevive(PlayerEntity player) {
        SavedState state = STATES.get(player.getUuid());
        if (state == null) return false;

        player.setHealth(state.health);

        if (player instanceof ServerPlayerEntity sp) {
            ServerWorld sw = (ServerWorld) sp.getWorld();
            sp.teleport(sw, state.pos.x, state.pos.y, state.pos.z, state.yaw, state.pitch);
        } else {
            player.setPosition(state.pos.x, state.pos.y, state.pos.z);
            player.setYaw(state.yaw);
            player.setPitch(state.pitch);
        }

        STATES.remove(player.getUuid());
        player.removeStatusEffect(GemforgedEffects.PHOENIX);

        if (player.getWorld() instanceof ServerWorld level) {
            Vec3d pos = player.getPos();

            level.playSound(null, pos.x, pos.y, pos.z,
                    SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 2.0f, 1.0f);
            level.playSound(null, pos.x, pos.y, pos.z,
                    SoundEvents.ENTITY_BLAZE_BURN, SoundCategory.PLAYERS, 1.5f, 0.8f);

            int rings = 5;
            int pointsPerRing = 120;
            double maxRadius = 8.0;

            for (int r = 0; r < rings; r++) {
                double radius = (maxRadius / rings) * (r + 1);
                double yBase = pos.y + 0.3 + r * 0.2;

                for (int i = 0; i < pointsPerRing; i++) {
                    double angle = (Math.PI * 2 * i) / pointsPerRing;
                    double px = pos.x + radius * Math.cos(angle);
                    double pz = pos.z + radius * Math.sin(angle);
                    double py = yBase + Math.sin(i * 0.15 + r) * 0.2;

                    int selector = (i + r) % 3;
                    DustParticleEffect dust = (selector == 0) ? RED : (selector == 1) ? ORANGE : YELLOW;
                    level.spawnParticles(dust, px, py, pz, 1, 0, 0, 0, 0);
                }
            }

            int columns = 16;
            for (int col = 0; col < columns; col++) {
                double angle = (Math.PI * 2 * col) / columns;
                double px = pos.x + Math.cos(angle) * 2.0;
                double pz = pos.z + Math.sin(angle) * 2.0;

                for (int h = 0; h < 20; h++) {
                    double py = pos.y + 0.2 + h * 0.15;
                    DustParticleEffect dust = (h > 13) ? YELLOW : (h > 6) ? ORANGE : RED;
                    level.spawnParticles(dust, px, py, pz, 1, 0, 0, 0, 0);
                }
            }

            level.spawnParticles(ParticleTypes.EXPLOSION, pos.x, pos.y + 1, pos.z,
                    3, 1.0, 1.0, 1.0, 0.0);
        }

        return true;
    }

    private record SavedState(Vec3d pos, float yaw, float pitch, float health) {}
}
