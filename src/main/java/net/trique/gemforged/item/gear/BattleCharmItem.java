package net.trique.gemforged.item.gear;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.trique.gemforged.effect.GemforgedEffects;
import net.trique.gemforged.item.GemforgedItems;
import org.joml.Vector3f;

import java.util.List;

public class BattleCharmItem extends Item {
    private static final float PARTICLE_DENSITY_SCALE = 1.0f;
    private static final float DUST_SIZE_SCALE = 2.0f;
    private static final float RADIUS = 8f;
    private static final int COOLDOWN_TICKS = 20 * 30 * 3;
    private static final int USE_DURATION_TICKS = 20;
    private static final int DURATION_RAGE = 20 * 30;
    private static final int AMP_RAGE = 0;
    private static final int FRAME_STEP = 1;
    private static final int FRAMES = 8;
    private static final float WAVE_START = RADIUS * 0.8f;
    private static final float WAVE_END = RADIUS * 3.0f;
    private static final int VERTICAL_COLUMNS = 12;
    private static final float COLUMN_HEIGHT = 3.5f;

    private static final DustParticleEffect MAIN =
            new DustParticleEffect(new Vector3f(0.8431f, 0.1961f, 0.3686f), 2.0f * DUST_SIZE_SCALE);
    private static final DustParticleEffect GLOW =
            new DustParticleEffect(new Vector3f(0.9725f, 0.4471f, 0.6823f), 2.4f * DUST_SIZE_SCALE);
    private static final DustParticleEffect DARK =
            new DustParticleEffect(new Vector3f(0.4667f, 0.1020f, 0.1608f), 1.6f * DUST_SIZE_SCALE);

    public BattleCharmItem(FabricItemSettings settings) {
        super(settings.maxDamage(250));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        if (!world.isClient) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 0.75f, 1.25f);
        }
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient) return;
        int elapsed = getMaxUseTime(stack) - remainingUseTicks;
        if (elapsed == 1) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.7F, 1.05F);
        }
        if (elapsed % 5 == 0) {
            float pitch = 0.9f + (elapsed / (float) USE_DURATION_TICKS) * 0.5f;
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 0.35F, pitch);
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return USE_DURATION_TICKS;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            boolean creative = player.getAbilities().creativeMode;
            ItemStack chargeResource = findChargeResource(player);
            if (creative || !chargeResource.isEmpty()) {
                triggerBurst((ServerWorld) world, player);
                if (!creative) {
                    chargeResource.decrement(1);
                    player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
                    stack.damage(1, player, p -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }
            }
        }
        return stack;
    }

    private ItemStack findChargeResource(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack s = player.getInventory().getStack(i);
            if (s.isOf(GemforgedItems.BLOODSTONE)) return s;
        }
        return ItemStack.EMPTY;
    }

    private void triggerBurst(ServerWorld world, PlayerEntity player) {
        Vec3d c = player.getPos();
        world.playSound(null, c.x, c.y, c.z, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 2.0f, 0.1f);
        world.playSound(null, c.x, c.y, c.z, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 2.0f, 0.1f);

        spawnRing(world, c.add(0, 0.2, 0), RADIUS, scaledCount(105), MAIN);
        spawnRing(world, c.add(0, 0.5, 0), RADIUS * 0.75f, scaledCount(85), GLOW);
        spawnLingeringCloud(world, c, 3.0f, RADIUS * 1.1f);
        scheduleWave(world, c);

        Box box = new Box(c.x - 10, c.y - 4, c.z - 10, c.x + 10, c.y + 4, c.z + 10);
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, box,
                e -> e.isAlive() && (e == player || e.isTeammate(player)));

        for (LivingEntity e : targets) {
            e.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                    GemforgedEffects.RAGE, DURATION_RAGE, AMP_RAGE, true, true));
        }
    }

    private void scheduleWave(ServerWorld world, Vec3d center) {
        MinecraftServer server = world.getServer();
        final int start = server.getTicks();
        for (int f = 0; f <= FRAMES; f++) {
            final float t = f / (float) FRAMES;
            final float tt = easeOutCubic(t);
            final float radius = lerp(WAVE_START, WAVE_END, tt);
            final double yLift = 0.05 * f;
            server.execute(() -> {
                spawnRing(world, center.add(0, yLift, 0), radius, scaledCount(95), MAIN);
                spawnRing(world, center.add(0, yLift + 0.12, 0), radius * 0.92f, scaledCount(80), GLOW);
                spawnVerticalColumnsFrame(world, center, radius, COLUMN_HEIGHT, VERTICAL_COLUMNS);
            });
        }
    }

    private void spawnVerticalColumnsFrame(ServerWorld world, Vec3d center, float radius, float height, int columns) {
        double cx = center.x, cy = center.y, cz = center.z;
        double stepAngle = (Math.PI * 2.0) / columns;
        for (int i = 0; i < columns; i++) {
            double a = i * stepAngle;
            double x = cx + radius * Math.cos(a);
            double z = cz + radius * Math.sin(a);
            int samples = 8;
            for (int j = 0; j < samples; j++) {
                double u = j / (double) (samples - 1);
                double y = cy + 0.2 + u * height;
                if ((j & 1) == 0) {
                    world.spawnParticles(DARK, x, y, z, 1, 0.02, 0.06, 0.02, 0.0);
                } else {
                    world.spawnParticles(GLOW, x, y, z, 1, 0.02, 0.06, 0.02, 0.0);
                }
                if (j == samples - 1) {
                    world.spawnParticles(ParticleTypes.ENCHANT, x, y + 0.02, z, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    private void spawnRing(ServerWorld world, Vec3d center, float radius, int points, DustParticleEffect dust) {
        double cx = center.x, cy = center.y, cz = center.z;
        for (int i = 0; i < points; i++) {
            double a = (Math.PI * 2 * i) / points;
            double px = cx + radius * Math.cos(a);
            double pz = cz + radius * Math.sin(a);
            world.spawnParticles(dust, px, cy, pz, 1, 0.04, 0.04, 0.04, 0.0);
        }
    }

    private void spawnLingeringCloud(ServerWorld world, Vec3d center, float seconds, float startRadius) {
        int duration = (int) (seconds * 20f);
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, center.x, center.y + 0.1, center.z);
        cloud.setParticleType(DARK);
        cloud.setDuration(duration);
        cloud.setRadius(startRadius);
        cloud.setRadiusGrowth(-(startRadius * 0.6f) / duration);
        cloud.setWaitTime(0);
        cloud.setNoGravity(true);
        world.spawnEntity(cloud);
    }

    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }

    private static float easeOutCubic(float x) {
        float inv = 1.0f - x;
        return 1.0f - inv * inv * inv;
    }

    private static int scaledCount(int base) {
        return Math.max(1, Math.round(base * PARTICLE_DENSITY_SCALE));
    }
}
