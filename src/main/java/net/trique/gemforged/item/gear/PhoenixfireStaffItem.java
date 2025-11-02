package net.trique.gemforged.item.gear;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.trique.gemforged.item.GemforgedItems;
import net.trique.gemforged.particle.GemforgedParticles;

import java.util.HashSet;
import java.util.Set;

public class PhoenixfireStaffItem extends Item {

    private static final float MAGIC_DAMAGE = 3.0F;
    private static final int FIRE_SECONDS = 3;
    private static final float EXPLOSION_POWER = 1.0F;
    private static final int COOLDOWN_TICKS = 20 * 20;
    private static final int RANGE = 20;
    private static final double ENTITY_HIT_RADIUS = 1.2D;
    private static final float PARTICLE_HEIGHT_OFFSET = 1.6F;
    private static final float VERTICAL_KNOCKBACK_MULTIPLIER = 0.5F;
    private static final float HORIZONTAL_KNOCKBACK_MULTIPLIER = 2.0F;

    public PhoenixfireStaffItem(Settings settings) {
        super(settings.maxDamage(350));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int elapsed = getMaxUseTime(stack) - remainingUseTicks;
        if (elapsed == 1) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 2.0F, 1.2F);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {

            boolean creative = player.getAbilities().creativeMode;
            ItemStack fuel = findFuel(player);

            if (creative || !fuel.isEmpty()) {
                fireBeam((ServerWorld) world, user);

                if (!creative) {
                    fuel.decrement(1);
                    player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
                    stack.damage(1, player, p -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }
            }
        }
        return stack;
    }

    private ItemStack findFuel(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack s = player.getInventory().getStack(i);
            if (s.isOf(GemforgedItems.PHOENIXTONE)) return s;
        }
        return ItemStack.EMPTY;
    }

    private void fireBeam(ServerWorld world, LivingEntity user) {

        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 4.0F, 1.0F);
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 4.0F, 1.0F);

        Vec3d source = user.getPos().add(0, PARTICLE_HEIGHT_OFFSET, 0);
        Vec3d target = source.add(user.getRotationVec(1.0F).multiply(RANGE));
        Vec3d toTarget = target.subtract(source);
        Vec3d dir = toTarget.normalize();

        Set<LivingEntity> hit = new HashSet<>();
        int steps = MathHelper.floor(toTarget.length()) + 10;

        for (int i = 1; i < steps; i++) {
            Vec3d p = source.add(dir.multiply(i));

            world.spawnParticles(
                    GemforgedParticles.PHOENIX_BEAM,
                    p.x, p.y, p.z,
                    1,
                    0.0, 0.0, 0.0,
                    0.0
            );

            BlockPos bp = BlockPos.ofFloored(p);
            for (Entity e : world.getOtherEntities(user,
                    new Box(bp).expand(ENTITY_HIT_RADIUS))) {
                if (e instanceof LivingEntity living && living != user)
                    hit.add(living);
            }
        }

        for (LivingEntity living : hit) {

            living.damage(world.getDamageSources().magic(), MAGIC_DAMAGE);
            living.setOnFireFor(FIRE_SECONDS);

            world.createExplosion(user,
                    living.getX(), living.getY(), living.getZ(),
                    EXPLOSION_POWER,
                    World.ExplosionSourceType.NONE
            );

            double resist = living.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
            double v = VERTICAL_KNOCKBACK_MULTIPLIER * (1.0 - resist);
            double h = HORIZONTAL_KNOCKBACK_MULTIPLIER * (1.0 - resist);
            living.addVelocity(dir.x * h, dir.y * v, dir.z * h);
        }
    }
}