package net.trique.gemforged.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GhostArrowEntity extends ArrowEntity {

    private static final int MAX_LIFETIME = 60;
    private int lifetime = 0;

    public GhostArrowEntity(EntityType<? extends GhostArrowEntity> type, World world) {
        super(type, world);
        init();
    }

    public GhostArrowEntity(World world, LivingEntity owner) {
        super(world, owner);
        init();
    }

    private void init() {
        this.setNoClip(true);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    public void tick() {

        // ✅ Super çağırmıyoruz, kendi physics’imiz
        Vec3d vel = this.getVelocity();

        if (!this.hasNoGravity()) {
            vel = vel.add(0.0D, -0.05D, 0.0D);
            this.setVelocity(vel);
        }

        Vec3d pos = this.getPos();
        Vec3d nextPos = pos.add(vel);

        EntityHitResult hit = ProjectileUtil.raycast(
                this,
                pos,
                nextPos,
                this.getBoundingBox().stretch(vel).expand(0.8D),
                e -> e instanceof LivingEntity
                        && e.isAlive()
                        && e != this.getOwner(),
                0
        );

        if (hit != null) {
            this.onEntityHit(hit);
            return;
        }

        // ✅ collision olmadan hareket
        this.move(MovementType.SELF, vel);

        // ✅ Yönü velocity ile güncelle
        this.setYaw((float)(Math.atan2(vel.x, vel.z) * 180.0D / Math.PI));
        this.setPitch((float)(Math.atan2(vel.y, vel.horizontalLength()) * 180.0D / Math.PI));

        if (++lifetime >= MAX_LIFETIME && !getWorld().isClient) {
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        super.onEntityHit(hitResult);
        if (!getWorld().isClient) this.discard();
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(Items.AIR);
    }
}
