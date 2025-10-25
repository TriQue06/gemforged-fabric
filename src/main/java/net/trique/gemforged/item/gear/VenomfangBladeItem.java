package net.trique.gemforged.item.gear;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.trique.gemforged.item.GemforgedItems;
import org.joml.Vector3f;

public class VenomfangBladeItem extends SwordItem {
    private static final int HIT_THRESHOLD = 3;
    private static final int SUPER_THRESHOLD = 10;
    private static final float MAGIC_DAMAGE = 4.0f;
    private static final float WAVE_MAX_RADIUS = 7.0f;
    private static final int WAVE_FRAMES = 16;
    private static final int WAVE_FRAME_STEP = 2;
    private static final int WAVE_COUNT = 3;
    private static final int WAVE_GAP_TICKS = 6;
    private static final int POISON_DURATION = 10 * 20;
    private static final int WITHER_DURATION = 10 * 20;
    private static final double KNOCKBACK_STRENGTH = 1.5;
    private static final double KNOCKBACK_VERTICAL = 0.25;
    private static final Vector3f GREEN = new Vector3f(0.1961f, 0.7451f, 0.5529f);
    private static final float GREEN_SCALE = 1.8f;
    private static final Vector3f DARK_GREEN = new Vector3f(0.0706f, 0.2549f, 0.1608f);
    private static final Vector3f WITHER = new Vector3f(0.1608f, 0.1608f, 0.1608f);
    private static final float DARK_SCALE = 2.1f;

    public VenomfangBladeItem(Settings settings) {
        super(ToolMaterials.DIAMOND, 2, -2.0f, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean res = super.postHit(stack, target, attacker);
        World world = attacker.getWorld();

        if (attacker instanceof PlayerEntity player && !world.isClient) {
            int count = stack.getOrCreateNbt().getInt("venomfang_hits") + 1;
            int superCount = stack.getOrCreateNbt().getInt("venomfang_super") + 1;

            if (count >= HIT_THRESHOLD) {
                if (consumeVenomyte(player)) {
                    triggerVenomWaves((ServerWorld) world, player, target);
                    count = 0;
                } else count = 0;
            }

            if (superCount >= SUPER_THRESHOLD) {
                if (consumeVenomyte(player)) {
                    triggerWitherBlast((ServerWorld) world, player, target);
                    superCount = 0;
                } else superCount = 0;
            }

            stack.getOrCreateNbt().putInt("venomfang_hits", count);
            stack.getOrCreateNbt().putInt("venomfang_super", superCount);
        }
        return res;
    }

    private boolean consumeVenomyte(PlayerEntity player) {
        if (player.getAbilities().creativeMode) return true;
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack s = player.getInventory().getStack(i);
            if (s.isOf(GemforgedItems.VENOMYTE)) {
                s.decrement(1);
                return true;
            }
        }
        return false;
    }

    private void triggerVenomWaves(ServerWorld world, PlayerEntity attacker, LivingEntity source) {
        Vec3d center = source.getPos().add(0, 0.05, 0);

        world.playSound(null, center.x, center.y, center.z, SoundEvents.ENTITY_WITCH_DRINK, SoundCategory.PLAYERS, 0.9f, 0.95f);
        world.playSound(null, center.x, center.y, center.z, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS, 0.9f, 1.25f);

        // Görsel dalgalar
        for (int w = 0; w < WAVE_COUNT; w++) {
            int baseDelay = w * WAVE_GAP_TICKS;
            world.getServer().execute(() -> {
                world.playSound(null, center.x, center.y, center.z, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.PLAYERS, 0.75f, 0.8f);
                world.playSound(null, center.x, center.y, center.z, SoundEvents.ENTITY_SPIDER_AMBIENT, SoundCategory.PLAYERS, 0.6f, 0.55f);
            });

            for (int f = 0; f <= WAVE_FRAMES; f++) {
                float t = f / (float) WAVE_FRAMES;
                float rad = t * WAVE_MAX_RADIUS;
                float fade = 1.0f - t;
                spawnRing(world, center, rad, 2.0, fade, GREEN, GREEN_SCALE);
            }
        }

        // Alan etkisi
        Box box = new Box(center.x - WAVE_MAX_RADIUS, center.y - 1.0, center.z - WAVE_MAX_RADIUS,
                center.x + WAVE_MAX_RADIUS, center.y + 2.0, center.z + WAVE_MAX_RADIUS);

        for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class, box, le -> le.isAlive() && le != attacker)) {
            e.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, POISON_DURATION));
            e.damage(world.getDamageSources().magic(), MAGIC_DAMAGE);
            Vec3d diff = e.getPos().subtract(center);
            Vec3d push = new Vec3d(diff.x, 0, diff.z).normalize().multiply(KNOCKBACK_STRENGTH);
            e.addVelocity(push.x, KNOCKBACK_VERTICAL, push.z);
            e.velocityModified = true;
        }

        world.playSound(null, center.x, center.y, center.z, SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 0.6f, 1.35f);
    }

    private void triggerWitherBlast(ServerWorld world, PlayerEntity attacker, LivingEntity source) {
        Vec3d center = source.getPos().add(0, 0.05, 0);
        world.playSound(null, center.x, center.y, center.z, SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.PLAYERS, 1.2f, 0.9f);

        for (int f = 0; f <= WAVE_FRAMES; f++) {
            float t = f / (float) WAVE_FRAMES;
            float rad = t * WAVE_MAX_RADIUS;
            float fade = 1.0f - t;
            spawnRing(world, center, rad, 2.0, fade, DARK_GREEN, DARK_SCALE);
            spawnRing(world, center, rad, 2.0, fade, WITHER, DARK_SCALE);
            world.spawnParticles(ParticleTypes.SMOKE, center.x, center.y, center.z, 8, rad * 0.2, 0.2, rad * 0.2, 0.01);
        }

        Box box = new Box(center.x - WAVE_MAX_RADIUS, center.y - 1.0, center.z - WAVE_MAX_RADIUS,
                center.x + WAVE_MAX_RADIUS, center.y + 2.0, center.z + WAVE_MAX_RADIUS);

        for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class, box, le -> le.isAlive() && le != attacker)) {
            e.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, WITHER_DURATION));
            e.damage(world.getDamageSources().magic(), MAGIC_DAMAGE * 2);
        }

        world.playSound(null, center.x, center.y, center.z, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.PLAYERS, 0.6f, 0.8f);
    }

    private void spawnRing(ServerWorld world, Vec3d center, float radius, double height, float fade, Vector3f color, float scale) {
        if (radius <= 0.05f) return;

        double cx = center.x, cy = center.y, cz = center.z;
        float r01 = Math.min(1f, radius / WAVE_MAX_RADIUS);
        float density = 0.25f + (float) Math.pow(r01, 1.6);
        int points = Math.max(12, (int) (radius * 18 * density));
        int layers = 8;

        DustParticleEffect dust = new DustParticleEffect(color, scale * (0.8f + 0.5f * fade));

        for (int i = 0; i < points; i++) {
            double a = (Math.PI * 2 * i) / points;
            double px = cx + radius * Math.cos(a);
            double pz = cz + radius * Math.sin(a);
            double py = cy + height * 0.5;
            world.spawnParticles(dust, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
        }

        if (radius >= WAVE_MAX_RADIUS * 0.35f) {
            for (int k = 0; k < 8; k++) {
                double a = (Math.PI / 4.0) * k;
                double px = cx + radius * Math.cos(a);
                double pz = cz + radius * Math.sin(a);
                for (int h = 0; h <= layers; h++) {
                    double py = cy + (h / (double) layers) * height;
                    world.spawnParticles(dust, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}