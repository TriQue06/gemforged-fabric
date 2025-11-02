package net.trique.gemforged.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.trique.gemforged.item.GemforgedItems;

import java.util.List;
import java.util.UUID;

public class ThunderPrismEntity extends Entity implements FlyingItemEntity {
    private int life;
    private ItemStack storedItem = ItemStack.EMPTY;
    private PlayerEntity owner;
    private UUID ownerId;
    private double originX, originZ, targetY;
    private double angle;
    private boolean spinning = false;

    public ThunderPrismEntity(EntityType<? extends ThunderPrismEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public ThunderPrismEntity(World world, double x, double y, double z, PlayerEntity owner) {
        this(GemforgedEntities.THUNDER_PRISM, world);
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
            targetY = getY() + 4.5;
            if (!getWorld().isClient) {
                getWorld().playSound(null, getX(), getY(), getZ(),
                        SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.9F, 0.7F);
            }
        }

        if (owner == null && ownerId != null && getWorld() instanceof ServerWorld serverWorld) {
            Entity e = serverWorld.getEntity(ownerId);
            if (e instanceof PlayerEntity p) owner = p;
        }

        double dy = getY() < targetY ? 0.08 : 0.0;
        double speed = getY() < targetY ? 0.35 : 1.0;
        angle += speed;
        double radius = getY() < targetY ? 0.8 : 1.0;
        double desiredX = originX + Math.cos(angle) * radius;
        double desiredZ = originZ + Math.sin(angle) * radius;
        double dx = (desiredX - getX()) * 0.45;
        double dz = (desiredZ - getZ()) * 0.45;

        setVelocity(dx, dy, dz);
        move(MovementType.SELF, getVelocity());

        if (life >= 40 && life < 100) {
            spinning = true;
            setYaw(getYaw() + 25);
        } else {
            spinning = false;
        }

        if (getWorld() instanceof ServerWorld server) {
            int count = spinning ? 36 : 16;
            server.spawnParticles(ParticleTypes.ENCHANT,
                    getX(), getY(), getZ(),
                    count, 0.6, 0.6, 0.6, 0.05);
            server.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    getX(), getY(), getZ(),
                    count / 2, 0.5, 0.5, 0.5, 0.01);
        }

        if (!getWorld().isClient && life < 100 && life % 18 == 0) {
            getWorld().playSound(null, getX(), getY(), getZ(),
                    SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 0.6F, 0.9F + getWorld().getRandom().nextFloat() * 0.2F);
        }

        if (life == 100 && !getWorld().isClient) {
            triggerEffect();
        }

        if (life > 120 && !getWorld().isClient) {
            discard();
        }
    }

    private void triggerEffect() {
        Box area = getBoundingBox().expand(16);
        List<LivingEntity> targets = getWorld().getEntitiesByClass(LivingEntity.class, area, e -> e.isAlive() && !isAlly(e));

        for (LivingEntity target : targets) {
            for (int i = 0; i < 3; i++) {
                LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(getWorld());
                if (bolt != null) {
                    bolt.refreshPositionAndAngles(target.getX(), target.getY(), target.getZ(), 0.0F, 0.0F);
                    getWorld().spawnEntity(bolt);
                }
            }
        }

        getWorld().playSound(null, getX(), getY(), getZ(),
                SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 2.0F, 1.0F);
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
        if (nbt.contains("Item")) {
            storedItem = ItemStack.fromNbt(nbt.getCompound("Item"));
        }
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

    public ItemStack getItem() {
        return storedItem.isEmpty() ? new ItemStack(GemforgedItems.THUNDER_PRISM) : storedItem;
    }

    public void setItem(ItemStack stack) {
        this.storedItem = stack;
    }

    public void setOwner(PlayerEntity player) {
        this.owner = player;
        this.ownerId = player == null ? null : player.getUuid();
    }

    @Override
    public ItemStack getStack() {
        return null;
    }
}