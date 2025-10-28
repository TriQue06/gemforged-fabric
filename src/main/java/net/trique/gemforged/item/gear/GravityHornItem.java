package net.trique.gemforged.item.gear;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
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

import java.util.List;

public class GravityHornItem extends Item {
    private static final float RADIUS = 16f;
    private static final int COOLDOWN_TICKS = 20 * 30;
    private static final int USE_DURATION_TICKS = 20;
    private static final int LEVITATION_DURATION = 20 * 2;
    private static final int LEVITATION_AMPLIFIER = 4;

    private static final DustParticleEffect PURPLE =
            new DustParticleEffect(new Vector3f(0.3843f, 0.0784f, 0.4078f), 3.0f);
    private static final DustParticleEffect LILAC =
            new DustParticleEffect(new Vector3f(0.8196f, 0.1647f, 0.8588f), 3.0f);

    public GravityHornItem(FabricItemSettings settings) {
        super(settings.maxDamage(250));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        if (!world.isClient) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 2.0f, 0.6f);
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 2.5f, 0.7f);
        }
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) { return UseAction.TOOT_HORN; }

    @Override
    public int getMaxUseTime(ItemStack stack) { return USE_DURATION_TICKS; }

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
            if (s.isOf(GemforgedItems.GRAVITIUM)) return s;
        }
        return ItemStack.EMPTY;
    }

    private void triggerBurst(ServerWorld world, PlayerEntity player) {
        Vec3d c = player.getPos();

        world.playSound(null, c.x, c.y, c.z,
                SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 3.0f, 0.5f);
        world.playSound(null, c.x, c.y, c.z,
                SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 3.0f, 0.8f);
        world.playSound(null, c.x, c.y, c.z,
                SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 2.5f, 0.9f);

        spawnRing(world, c.add(0, 0.3, 0), RADIUS, 1200, PURPLE, 1.0f);
        spawnRing(world, c.add(0, 0.35, 0), RADIUS, 1200, LILAC, 1.0f);
        spawnStar(world, c.add(0, 0.5, 0), 12.0, 5, PURPLE);
        spawnStar(world, c.add(0, 0.5, 0), 12.0, 5, LILAC);

        Box box = new Box(
                c.x - RADIUS, c.y - RADIUS, c.z - RADIUS,
                c.x + RADIUS, c.y + RADIUS, c.z + RADIUS
        );

        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, box,
                e -> e.isAlive() && e != player);

        for (LivingEntity e : targets) {
            e.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, LEVITATION_DURATION, LEVITATION_AMPLIFIER, true, true));
        }
    }

    private void spawnRing(ServerWorld world, Vec3d center, float radius, int points, DustParticleEffect dust, float heightSpread) {
        double cx = center.x, cy = center.y, cz = center.z;
        for (int i = 0; i < points; i++) {
            double a = (Math.PI * 2 * i) / points;
            double px = cx + radius * Math.cos(a);
            double pz = cz + Math.sin(a) * radius;
            double py = cy + (world.random.nextDouble() - 0.5) * heightSpread * 2;
            world.spawnParticles(dust, px, py, pz, 2, 0.04, 0.04, 0.04, 0.01);
        }
    }

    private void spawnStar(ServerWorld world, Vec3d center, double radius, int points, DustParticleEffect dust) {
        double cx = center.x, cy = center.y, cz = center.z;
        double[] xs = new double[points];
        double[] zs = new double[points];

        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points - Math.PI / 2;
            xs[i] = cx + Math.cos(angle) * radius;
            zs[i] = cz + Math.sin(angle) * radius;
        }

        for (int i = 0; i < points; i++) {
            int j = (i + 2) % points;
            spawnLine(world, cy, xs[i], zs[i], xs[j], zs[j], dust);
        }
    }

    private void spawnLine(ServerWorld world, double cy, double x1, double z1, double x2, double z2, DustParticleEffect dust) {
        int steps = 100;
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            double px = x1 + (x2 - x1) * t;
            double pz = z1 + (z2 - z1) * t;
            world.spawnParticles(dust, px, cy, pz, 2, 0.05, 0.05, 0.05, 0.01);
        }
    }
}