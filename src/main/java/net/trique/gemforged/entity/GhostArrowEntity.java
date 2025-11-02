package net.trique.gemforged.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.entity.projectile.ProjectileUtil;

public class GhostArrowEntity extends PersistentProjectileEntity {

    private static final int MAX_LIFETIME = 60; // 3 saniye
    private int lifetime = 0;

    public GhostArrowEntity(EntityType<? extends GhostArrowEntity> type, World world) {
        super(type, world);
        this.pickupType = PickupPermission.DISALLOWED;
        this.setNoClip(true);
    }

    public GhostArrowEntity(EntityType<? extends GhostArrowEntity> type, LivingEntity owner, World world) {
        super(type, owner, world);
        this.pickupType = PickupPermission.DISALLOWED;
        this.setNoClip(true);
    }

    @Override
    public void tick() {
        super.tick();

        lifetime++;
        if (!getWorld().isClient && lifetime >= MAX_LIFETIME) {
            this.discard();
            return;
        }

        Vec3d velocity = getVelocity();
        Vec3d start = getPos();
        Vec3d end = start.add(velocity);

        // ✅ SADECE ENTITY COLLISION ALGILAR
        EntityHitResult hit = ProjectileUtil.getEntityCollision(
                this.getWorld(), this,
                start, end,
                this.getBoundingBox().stretch(velocity).expand(1.0),
                entity -> entity instanceof LivingEntity && entity != this.getOwner()
        );

        if (hit != null) {
            this.onEntityHit(hit);
            return;
        }

        // ✅ Parçacık izi
        for (int i = 0; i < 2; i++) {
            this.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX() - velocity.x * i * 0.2,
                    this.getY() - velocity.y * i * 0.2,
                    this.getZ() - velocity.z * i * 0.2,
                    0, 0, 0);
        }

        // ✅ VANILLA ARROW MOVEMENT → DOĞRU ÇARPIŞMA
        this.move(MovementType.SELF, velocity);

        // ✅ Gravity & drag (vanilla kullandığı için sadece gravity şart)
        if (!this.hasNoGravity()) {
            this.setVelocity(velocity.add(0, -0.05, 0));
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        super.onEntityHit(hitResult);

        Entity target = hitResult.getEntity();
        Entity owner = this.getOwner();

        if (!getWorld().isClient && target instanceof LivingEntity living) {

            // ✅ VANILLA DAMAGE HESABI → SÜPER!
            float damage = (float) this.getDamage();
            if (this.isCritical()) {
                damage += this.random.nextFloat() * damage * 0.25f + 0.5f;
            }

            // ✅ Hasar gönder
            living.damage(this.getDamageSources().arrow(this, owner), damage);

            // ✅ Sadece damage, knockback vanilla kalır
            this.discard();
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    protected boolean shouldHit(Entity entity) {
        return entity != this.getOwner();
    }
}