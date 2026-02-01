package net.trique.gemforged.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.trique.gemforged.item.GemforgedItems;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class VerdantTotemEntity extends Entity implements FlyingItemEntity {
    private int life;
    private ItemStack storedItem = ItemStack.EMPTY;
    private PlayerEntity owner;
    private UUID ownerId;
    private double originX, originZ, targetY;
    private double angle;

    public VerdantTotemEntity(EntityType<? extends VerdantTotemEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public VerdantTotemEntity(World world, double x, double y, double z, PlayerEntity owner) {
        this(GemforgedEntities.VERDANT_TOTEM, world);
        this.setPos(x, y, z);
        this.owner = owner;
        this.ownerId = owner.getUuid();
    }

    @Override
    public void tick() {
        super.tick();
        life++;

        if (life == 1) {
            originX = getX();
            originZ = getZ();
            targetY = getY() + 4.0;
            if (!getWorld().isClient) {
                getWorld().playSound(null, getX(), getY(), getZ(),
                        SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, 0.7F);
            }
        }

        if (owner == null && ownerId != null && getWorld() instanceof ServerWorld serverWorld) {
            Entity e = serverWorld.getEntity(ownerId);
            if (e instanceof PlayerEntity p) owner = p;
        }

        double dy = getY() < targetY ? 0.05 : 0.0;
        double speed = 0.35;
        angle += speed;
        double radius = 0.8;
        double desiredX = originX + Math.cos(angle) * radius;
        double desiredZ = originZ + Math.sin(angle) * radius;
        double dx = (desiredX - getX()) * 0.4;
        double dz = (desiredZ - getZ()) * 0.4;

        setVelocity(dx, dy, dz);
        move(MovementType.SELF, getVelocity());
        setYaw(getYaw() + 8f);

        if (getWorld() instanceof ServerWorld server) {
            if (life % 4 == 0) {
                server.spawnParticles(new DustParticleEffect(new Vector3f(0.2F, 0.9F, 0.2F), 1.0F),
                        getX(), getY() + 0.5, getZ(),
                        4, 0.35, 0.35, 0.35, 0.01);
            }
            for (int i = 0; i < 6; i++) {
                double a = (Math.PI * 2 * i) / 6.0;
                double px = getX() + Math.cos(a) * 1.5;
                double pz = getZ() + Math.sin(a) * 1.5;
                double py = getY() + 0.1 * Math.sin((life + i * 5) * 0.2);
                server.spawnParticles(ParticleTypes.COMPOSTER, px, py, pz, 1, 0, 0, 0, 0);
            }

            if (life % 20 == 0) applyRegen();
        }

        if (life == 200 && !getWorld().isClient) explode();
        if (life > 220 && !getWorld().isClient) discard();
    }

    private void applyRegen() {
        Box area = getBoundingBox().expand(12);
        List<LivingEntity> allies = getWorld().getEntitiesByClass(LivingEntity.class, area, this::isAlly);
        for (LivingEntity e : allies) {
            e.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 1, false, true, true));
        }
    }

    private void explode() {
        if (!(getWorld() instanceof ServerWorld server)) return;

        for (int i = 0; i < 4; i++) {
            float pitch = 0.7F + (i * 0.1F);
            server.playSound(null, getX(), getY(), getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 4.0F, pitch);
            server.playSound(null, getX(), getY(), getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.PLAYERS, 4.0F, pitch);
            server.playSound(null, getX(), getY(), getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 4.0F, pitch);
        }
        server.playSound(null, getX(), getY(), getZ(), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 4.0F, 1.0F);

        for (int i = 0; i < 120; i++) {
            server.spawnParticles(new DustParticleEffect(new Vector3f(0.4F, 1.0F, 0.4F), 1.5F),
                    getX(), getY(), getZ(), 1,
                    random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), 0.04);
        }

        int[] colors = {0x2e8109, 0x4ab60a, 0x73d20a, 0x9de50c, 0xd7ef05};
        int rings = 5, pointsPerRing = 64;
        double maxRadius = 8.0;

        for (int ring = 0; ring < rings; ring++) {
            double r = (maxRadius / rings) * (ring + 1);
            Color c = new Color(colors[ring]);
            Vector3f colorVec = new Vector3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);

            for (int p = 0; p < pointsPerRing; p++) {
                double a = (2 * Math.PI * p) / pointsPerRing;

                double px = getX() + Math.cos(a) * r;
                double pz = getZ() + Math.sin(a) * r;
                double py = getY();
                spawnColoredSet(server, px, py, pz, colorVec);

                double vy = getY() + Math.sin(a) * r;
                double vz = getZ() + Math.cos(a) * r;
                double vx = getX();
                spawnColoredSet(server, vx, vy, vz, colorVec);

                double xx = getX() + Math.cos(a) * r;
                double yy = getY() + Math.sin(a) * r;
                double zz = getZ();
                spawnColoredSet(server, xx, yy, zz, colorVec);
            }
        }

        Box area = getBoundingBox().expand(12);
        List<LivingEntity> allies = getWorld().getEntitiesByClass(LivingEntity.class, area, this::isAlly);
        for (LivingEntity e : allies) {
            e.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 4, false, true, true));
            e.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1, false, true, true));
        }
    }

    private void spawnColoredSet(ServerWorld server, double x, double y, double z, Vector3f color) {
        server.spawnParticles(new DustParticleEffect(color, 1.3F),
                x, y, z, 10, 0, 0, 0, 0.015);
        server.spawnParticles(ParticleTypes.COMPOSTER, x, y, z, 8, 0, 0, 0, 0.015);
    }

    private boolean isAlly(LivingEntity entity) {
        if (entity == owner) return true;
        if (owner != null && owner.isTeammate(entity)) return true;
        if (entity instanceof TameableEntity t && owner != null && t.getOwner() == owner) return true;
        if (entity instanceof AbstractHorseEntity h && owner != null && h.isTame() && owner.getUuid().equals(h.getOwnerUuid())) return true;
        return (entity instanceof IronGolemEntity || entity instanceof SnowGolemEntity);
    }

    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Item")) storedItem = ItemStack.fromNbt(nbt.getCompound("Item"));
        if (nbt.containsUuid("Owner")) ownerId = nbt.getUuid("Owner");
        originX = nbt.getDouble("OX");
        originZ = nbt.getDouble("OZ");
        targetY = nbt.getDouble("TY");
        angle = nbt.getDouble("Ang");
        life = nbt.getInt("Life");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (!storedItem.isEmpty()) nbt.put("Item", storedItem.writeNbt(new NbtCompound()));
        if (ownerId != null) nbt.putUuid("Owner", ownerId);
        nbt.putDouble("OX", originX);
        nbt.putDouble("OZ", originZ);
        nbt.putDouble("TY", targetY);
        nbt.putDouble("Ang", angle);
        nbt.putInt("Life", life);
    }

    @Override
    public ItemStack getStack() {
        return storedItem.isEmpty() ? new ItemStack(GemforgedItems.VERDANT_TOTEM) : storedItem;
    }

    public void setItem(ItemStack stack) {
        this.storedItem = stack;
    }

    public void setOwner(PlayerEntity player) {
        this.owner = player;
        this.ownerId = player == null ? null : player.getUuid();
    }
}