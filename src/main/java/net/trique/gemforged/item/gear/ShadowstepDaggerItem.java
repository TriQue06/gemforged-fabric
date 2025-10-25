package net.trique.gemforged.item.gear;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.trique.gemforged.item.GemforgedItems;
import org.joml.Vector3f;

import java.util.UUID;

public class ShadowstepDaggerItem extends SwordItem {
    private static final String TAG_COMBO = "onyx_combo";
    private static final String TAG_LASTHIT = "onyx_last_hit";
    private static final int MAX_COMBO = 6;
    private static final int COMBO_TIMEOUT_TICKS = 60;
    private static final int COOLDOWN_TICKS = 20 * 30;
    private static final double TP_MIN = 2.0, TP_MAX = 3.0;

    private static final UUID MOD_DAMAGE_ID = UUID.nameUUIDFromBytes("gemforged_onyx_combo_damage".getBytes());
    private static final UUID MOD_SPEED_ID = UUID.nameUUIDFromBytes("gemforged_onyx_combo_speed".getBytes());

    private static final Vector3f SHADOW_PURPLE = new Vector3f(0.2627f, 0.1569f, 0.3843f);
    private static final Vector3f SHADOW_LIGHT = new Vector3f(0.4745f, 0.3294f, 0.6118f);

    public ShadowstepDaggerItem(Settings settings) {
        super(ToolMaterials.IRON, 2, -2.0f, settings.maxDamage(250));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof PlayerEntity player)) return true;
        World world = player.getWorld();
        if (world.isClient) return true;

        NbtCompound tag = stack.getOrCreateNbt();
        long now = world.getTime();
        int combo = tag.getInt(TAG_COMBO);
        long last = tag.getLong(TAG_LASTHIT);

        if (combo > 0 && (now - last) > COMBO_TIMEOUT_TICKS) {
            combo = 0;
            removeComboModifiers(player);
        }

        if (player.getItemCooldownManager().isCoolingDown(this)) {
            removeComboModifiers(player);
            return true;
        }

        if (combo == 0) {
            ItemStack nyxite = findChargeResource(player);
            boolean creative = player.getAbilities().creativeMode;
            if (!creative && nyxite.isEmpty()) {
                return true;
            }
        }

        if (player instanceof ServerPlayerEntity sp && target.isAlive()) {
            tryTeleportAround((ServerWorld) world, sp, target);
        }

        combo++;
        tag.putInt(TAG_COMBO, combo);
        tag.putLong(TAG_LASTHIT, now);

        if (combo == 1) {
            addComboModifiers(player);
        }

        if (combo >= MAX_COMBO) {
            boolean creative = player.getAbilities().creativeMode;
            ItemStack nyxite = findChargeResource(player);

            if (creative || !nyxite.isEmpty()) {
                if (!creative) nyxite.decrement(1);
                player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            }

            tag.putInt(TAG_COMBO, 0);
            removeComboModifiers(player);
        }

        stack.setNbt(tag);
        return true;
    }

    private ItemStack findChargeResource(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack s = player.getInventory().getStack(i);
            if (s.isOf(GemforgedItems.NYXITE)) return s;
        }
        return ItemStack.EMPTY;
    }

    private void addComboModifiers(PlayerEntity player) {
        EntityAttributeInstance dmg = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        EntityAttributeInstance spd = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);
        if (dmg != null && dmg.getModifier(MOD_DAMAGE_ID) == null) {
            dmg.addTemporaryModifier(new EntityAttributeModifier(MOD_DAMAGE_ID, "onyx_damage_bonus", 2.0, EntityAttributeModifier.Operation.ADDITION));
        }
        if (spd != null && spd.getModifier(MOD_SPEED_ID) == null) {
            spd.addTemporaryModifier(new EntityAttributeModifier(MOD_SPEED_ID, "onyx_speed_bonus", 10.0, EntityAttributeModifier.Operation.ADDITION));
        }
    }

    private void removeComboModifiers(PlayerEntity player) {
        EntityAttributeInstance dmg = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        EntityAttributeInstance spd = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);
        if (dmg != null) dmg.removeModifier(MOD_DAMAGE_ID);
        if (spd != null) spd.removeModifier(MOD_SPEED_ID);
    }

    private void tryTeleportAround(ServerWorld world, ServerPlayerEntity player, LivingEntity target) {
        Vec3d c = target.getPos();
        Random random = world.getRandom();

        for (int i = 0; i < 10; i++) {
            double d = TP_MIN + random.nextDouble() * (TP_MAX - TP_MIN);
            double a = random.nextDouble() * Math.PI * 2;

            BlockPos guess = BlockPos.ofFloored(
                    c.x + d * Math.cos(a),
                    c.y,
                    c.z + d * Math.sin(a)
            );
            BlockPos safe = findStandable(world, guess, 6);
            if (safe != null) {
                Vec3d from = player.getPos();
                spawnShadowSmoke(world, from.x, from.y + 1.0, from.z);
                playShadowTeleportSound(world, from.x, from.y, from.z);

                player.teleport(safe.getX() + 0.5, safe.getY(), safe.getZ() + 0.5, true);

                Vec3d to = player.getPos();
                spawnShadowSmoke(world, to.x, to.y + 1.0, to.z);
                playShadowTeleportSound(world, to.x, to.y, to.z);
                player.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES, target.getEyePos());
                return;
            }
        }
    }

    private void spawnShadowSmoke(ServerWorld world, double x, double y, double z) {
        world.spawnParticles(new DustParticleEffect(SHADOW_PURPLE, 1.5f),
                x, y, z, 20, 0.6, 0.25, 0.6, 0.02);
        world.spawnParticles(new DustParticleEffect(SHADOW_LIGHT, 1.5f),
                x, y, z, 20, 0.6, 0.25, 0.6, 0.02);
        world.spawnParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 20, 0.6, 0.25, 0.6, 0.02);
        world.spawnParticles(ParticleTypes.ASH, x, y, z, 10, 0.5, 0.2, 0.5, 0.01);
    }

    private void playShadowTeleportSound(ServerWorld world, double x, double y, double z) {
        world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 0.6f + world.random.nextFloat() * 0.2f);
        world.playSound(null, x, y, z, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.8f, 0.9f + world.random.nextFloat() * 0.1f);
    }

    private BlockPos findStandable(ServerWorld world, BlockPos pos, int vRange) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        for (int dy = 0; dy <= vRange; dy++) {
            if (isStandable(world, mutable.set(pos.getX(), pos.getY() + dy, pos.getZ()))) return mutable.toImmutable();
            if (isStandable(world, mutable.set(pos.getX(), pos.getY() - dy, pos.getZ()))) return mutable.toImmutable();
        }
        return null;
    }

    private boolean isStandable(ServerWorld world, BlockPos pos) {
        BlockPos below = pos.down();
        BlockState belowState = world.getBlockState(below);
        boolean solidBelow = !belowState.getCollisionShape(world, below).isEmpty();
        boolean airFeet = world.isAir(pos);
        boolean airHead = world.isAir(pos.up());
        return solidBelow && airFeet && airHead;
    }
}