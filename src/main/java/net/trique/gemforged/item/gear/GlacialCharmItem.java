package net.trique.gemforged.item.gear;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
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

public class GlacialCharmItem extends Item {

    private static final float RADIUS = 8f;
    private static final int COOLDOWN_TICKS = 20 * 30;
    private static final int USE_DURATION_TICKS = 20;
    private static final int DURATION_GFL = 20 * 15;
    private static final int AMP_GFL = 0;

    private static final DustParticleEffect COL_LIGHT =
            new DustParticleEffect(new Vector3f(0xa4 / 255f, 0xe9 / 255f, 0xfb / 255f), 2.2f);
    private static final DustParticleEffect COL_MID =
            new DustParticleEffect(new Vector3f(0x54 / 255f, 0xc5 / 255f, 0xf1 / 255f), 2.2f);
    private static final DustParticleEffect COL_DARK =
            new DustParticleEffect(new Vector3f(0x22 / 255f, 0x78 / 255f, 0xbb / 255f), 2.2f);

    public GlacialCharmItem(Settings settings) {
        super(settings.maxDamage(250));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);

        if (!world.isClient) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                    SoundCategory.PLAYERS, 0.7f, 0.4f);
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
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            ItemStack resource = findChargeResource(player);
            boolean creative = player.getAbilities().creativeMode;

            if (creative || !resource.isEmpty()) {
                activateGlacial((ServerWorld) world, player);

                player.incrementStat(Stats.USED.getOrCreateStat(this));

                if (!creative) {
                    resource.decrement(1);
                    stack.damage(1, player, p -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                    player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
                }
            }
        }
        return stack;
    }

    private ItemStack findChargeResource(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack s = player.getInventory().getStack(i);
            if (s.isOf(GemforgedItems.PRISMYTE)) return s;
        }
        return ItemStack.EMPTY;
    }

    private void activateGlacial(ServerWorld world, PlayerEntity player) {
        Vec3d center = player.getPos();

        world.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.6f, 0.4f);

        spawnSnowflakeCoreFractal(world, center);
        spawn3DAxisFlakes(world, center);
        spawnSphereRings(world, center);
        spawnCloud(world, center);
        applyGlacialFist(world, player, center);
    }

    private void applyGlacialFist(ServerWorld world, PlayerEntity caster, Vec3d c) {
        Box box = new Box(c.x - RADIUS, c.y - RADIUS, c.z - RADIUS,
                c.x + RADIUS, c.y + RADIUS, c.z + RADIUS);

        List<LivingEntity> allies =
                world.getEntitiesByClass(LivingEntity.class, box,
                        e -> e.isTeammate(caster) || e == caster);

        for (LivingEntity ally : allies) {
            ally.addStatusEffect(new StatusEffectInstance(
                    GemforgedEffects.GLACIAL_FIST,
                    DURATION_GFL,
                    AMP_GFL,
                    true,
                    true
            ));
        }
    }

    private static DustParticleEffect pick(int i) {
        int m = i % 3;
        return m == 0 ? COL_LIGHT : (m == 1 ? COL_MID : COL_DARK);
    }

    private void spawnSnowflakeCoreFractal(ServerWorld world, Vec3d c) {
        int arms = 8;
        int segments = 12;
        double cx = c.x, cy = c.y + 0.3, cz = c.z;

        for (int arm = 0; arm < arms; arm++) {
            double base = arm * (Math.PI * 2 / arms);

            for (int i = 1; i <= segments; i++) {
                double t = (double) i / segments;
                double dist = t * (RADIUS * 0.85);
                DustParticleEffect col = pick(i);

                double sx = cx + Math.cos(base) * dist;
                double sz = cz + Math.sin(base) * dist;

                world.spawnParticles(col, sx, cy, sz, 2, 0.01, 0.01, 0.01, 0.006);

                double branch = base + (i % 2 == 0 ? Math.PI / 8 : -Math.PI / 8);
                double bd = dist * 0.35;
                double bx = sx + Math.cos(branch) * bd;
                double bz = sz + Math.sin(branch) * bd;
                world.spawnParticles(col, bx, cy, bz, 1, 0.008, 0.008, 0.008, 0.004);
            }
        }
    }

    private void spawn3DAxisFlakes(ServerWorld world, Vec3d c) {
        int samples = 16;
        double cx = c.x, cy = c.y + 0.3, cz = c.z;

        for (int i = 1; i <= samples; i++) {
            double t = (double) i / samples;
            double d = t * RADIUS;
            DustParticleEffect col = pick(i);

            world.spawnParticles(col, cx + d, cy, cz, 1, 0.01, 0.01, 0.01, 0.006);
            world.spawnParticles(col, cx - d, cy, cz, 1, 0.01, 0.01, 0.01, 0.006);
            world.spawnParticles(col, cx, cy + d, cz, 1, 0.01, 0.01, 0.01, 0.006);
            world.spawnParticles(col, cx, cy - d, cz, 1, 0.01, 0.01, 0.01, 0.006);
            world.spawnParticles(col, cx, cy, cz + d, 1, 0.01, 0.01, 0.01, 0.006);
            world.spawnParticles(col, cx, cy, cz - d, 1, 0.01, 0.01, 0.01, 0.006);
        }
    }

    private void spawnSphereRings(ServerWorld world, Vec3d c) {
        int rings = 2;
        int perRing = 64;
        double cx = c.x, cy = c.y + 0.3, cz = c.z;

        for (int r = 1; r <= rings; r++) {
            double rr = (RADIUS / rings) * r;

            for (int i = 0; i < perRing; i++) {
                double a = (2 * Math.PI * i) / perRing;
                DustParticleEffect col = pick(i);

                double x1 = cx + Math.cos(a) * rr;
                double z1 = cz + Math.sin(a) * rr;
                world.spawnParticles(col, x1, cy, z1, 2, 0.02, 0.02, 0.02, 0.007);
            }
        }
    }

    private void spawnCloud(ServerWorld world, Vec3d c) {
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, c.x, c.y + 0.1, c.z);
        cloud.setParticleType(COL_MID);
        cloud.setRadius(RADIUS);
        cloud.setDuration(30);
        cloud.setRadiusGrowth(-0.15f);
        cloud.setNoGravity(true);
        world.spawnEntity(cloud);
    }
}
