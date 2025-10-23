package net.trique.gemforged.item.gear;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
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
import net.trique.gemforged.item.GemforgedItems;
import org.joml.Vector3f;

public class SandburstStaffItem extends Item {
    private static final float MAX_RADIUS = 10.0f;
    private static final float BASE_KNOCKBACK = 16.0f;
    private static final double VERTICAL_BOOST = 1.0;
    private static final int COOLDOWN_TICKS = 20 * 20;
    private static final float MAGIC_DAMAGE = 5.5f;
    private static final int USE_DURATION_TICKS = 20;
    private static final Vector3f YELLOW = new Vector3f(0.9176f, 0.7765f, 0.1569f);
    private static final Vector3f SAND = new Vector3f(0.7922f, 0.5843f, 0.1020f);
    private static final float YELLOW_SCALE = 1.6f;
    private static final float SAND_SCALE = 2.0f;
    private static final int WAVE_COUNT = 3;
    private static final int WAVE_FRAMES = 16;
    private static final int WAVE_FRAME_STEP = 2;
    private static final int WAVE_GAP_TICKS = 4;
    private static final float MIN_RENDER_RADIUS = 0.8f;
    private static final float RINGS_HEIGHT = 2.0f;

    public SandburstStaffItem(FabricItemSettings settings) {
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
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return USE_DURATION_TICKS;
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
            if (s.isOf(GemforgedItems.SOLARIUM)) return s;
        }
        return ItemStack.EMPTY;
    }

    private void triggerBurst(ServerWorld world, PlayerEntity player) {
        Vec3d origin = player.getPos().add(0, 0.2, 0);
        world.playSound(null, origin.x, origin.y, origin.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.6f, 1.55f);
        world.playSound(null, origin.x, origin.y, origin.z, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.PLAYERS, 1.2f, 0.85f);
        scheduleWaves(world, origin);
        affectEntities(world, player);
    }

    private void scheduleWaves(ServerWorld world, Vec3d center) {
        MinecraftServer server = world.getServer();
        final int start = server.getTicks();
        for (int w = 0; w < WAVE_COUNT; w++) {
            final int waveStart = start + w * WAVE_GAP_TICKS;
            for (int f = 0; f <= WAVE_FRAMES; f += WAVE_FRAME_STEP) {
                final int when = waveStart + f;
                final float t = f / (float) WAVE_FRAMES;
                final float eased = (float) Math.pow(t, 0.6);
                final float rad = MIN_RENDER_RADIUS + eased * (MAX_RADIUS - MIN_RENDER_RADIUS);
                final float fade = 1.0f - t;
                final Vec3d cNow = center;

                server.execute(() -> {
                    spawnRingWithSpikesColored(world, cNow, rad, RINGS_HEIGHT, fade, YELLOW, YELLOW_SCALE);
                    spawnRingWithSpikesColored(world, cNow, rad, RINGS_HEIGHT, fade, SAND, SAND_SCALE);
                });
            }
        }
    }

    private void spawnRingWithSpikesColored(ServerWorld world, Vec3d center, float radius, double height, float fade,
                                            Vector3f color, float scale) {
        if (radius <= MIN_RENDER_RADIUS) return;

        final double cx = center.x, cy = center.y, cz = center.z;
        float r01 = Math.min(1f, radius / MAX_RADIUS);
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

        if (radius >= MAX_RADIUS * 0.35f) {
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

    private void affectEntities(ServerWorld world, PlayerEntity source) {
        Vec3d center = source.getPos();
        Box box = new Box(
                center.x - MAX_RADIUS, center.y - 1.0, center.z - MAX_RADIUS,
                center.x + MAX_RADIUS, center.y + 2.5, center.z + MAX_RADIUS
        );
        for (Entity e : world.getOtherEntities(source, box)) {
            if (e == source || !e.isPushable()) continue;
            Vec3d diff = e.getPos().subtract(center);
            Vec3d horizontal = new Vec3d(diff.x, 0.0, diff.z);
            double dist = horizontal.length();
            if (dist <= 0.0001 || dist > MAX_RADIUS) continue;
            double falloff = Math.max(0.0, 1.0 - (dist / MAX_RADIUS));
            double strength = BASE_KNOCKBACK * falloff;
            Vec3d push = horizontal.normalize().multiply(strength);
            e.addVelocity(push.x, VERTICAL_BOOST * falloff, push.z);
            if (e instanceof LivingEntity le) {
                le.damage(world.getDamageSources().magic(), MAGIC_DAMAGE);
            }
        }
    }
}
