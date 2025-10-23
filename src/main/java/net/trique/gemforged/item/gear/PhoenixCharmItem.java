package net.trique.gemforged.item.gear;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
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

public class PhoenixCharmItem extends Item {
    private static final int USE_DURATION_TICKS = 20;
    private static final int EFFECT_DURATION = 20 * 30;
    private static final int COOLDOWN_TICKS = 20 * 60 * 4;

    private static final DustParticleEffect ORANGE =
            new DustParticleEffect(new Vector3f(0.7725f, 0.2353f, 0.0627f), 2.0f);
    private static final DustParticleEffect YELLOW =
            new DustParticleEffect(new Vector3f(0.9725f, 0.7294f, 0.3843f), 2.0f);

    public PhoenixCharmItem(FabricItemSettings settings) {
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
                applyPhoenixEffect((ServerWorld) world, player);

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
            if (s.isOf(GemforgedItems.PHOENIXTONE)) return s;
        }
        return ItemStack.EMPTY;
    }

    private void applyPhoenixEffect(ServerWorld world, PlayerEntity user) {
        Vec3d c = user.getPos();
        world.playSound(null, c.x, c.y, c.z,
                SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.2f, 1.0f);
        world.playSound(null, c.x, c.y, c.z,
                SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 0.8f, 1.2f);

        Box area = new Box(c.x - 4, c.y - 4, c.z - 4, c.x + 4, c.y + 4, c.z + 4);
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, area,
                e -> e.isAlive() && (e == user || e.isTeammate(user)));

        for (LivingEntity e : targets) {
            e.addStatusEffect(new StatusEffectInstance(GemforgedEffects.PHOENIX, EFFECT_DURATION, 0, true, true));

            Vec3d base = e.getPos();

            for (int wing = -1; wing <= 1; wing += 2) {
                for (int i = 0; i < 50; i++) {
                    double progress = i / 50.0;
                    double angle = progress * Math.PI / 1.2;
                    double radius = 1.5 * Math.sin(angle);
                    double px = base.x + wing * radius;
                    double py = base.y + 0.5 + progress * 2.5;
                    double pz = base.z + (progress - 0.5) * 2.0;

                    DustParticleEffect dust = (i % 2 == 0) ? ORANGE : YELLOW;
                    world.spawnParticles(dust, px, py, pz, 1, 0, 0, 0, 0);
                }
            }

            for (int i = 0; i < 40; i++) {
                double angle = (Math.PI * 2 * i) / 40.0;
                double radius = 0.6;
                double px = base.x + radius * Math.cos(angle);
                double pz = base.z + radius * Math.sin(angle);
                double py = base.y + 0.3 + (i % 10) * 0.1;

                DustParticleEffect dust = (i % 2 == 0) ? ORANGE : YELLOW;
                world.spawnParticles(dust, px, py, pz, 1, 0, 0, 0, 0);
            }
        }
    }
}
