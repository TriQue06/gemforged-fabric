package net.trique.gemforged.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.trique.gemforged.effect.PhoenixEffect;

public class PhoenixRelicEvents {
    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (entity instanceof PlayerEntity player) {
                if (player.getHealth() <= 0.0F) {
                    boolean revived = PhoenixEffect.tryRevive(player);
                    if (revived) {
                        player.setHealth(player.getMaxHealth());
                        player.timeUntilRegen = 0;
                    }
                }
            }
        });
    }

    public static void onDamage(PlayerEntity player, DamageSource source, float amount) {
        float health = player.getHealth();
        if (amount >= health) {
            boolean revived = PhoenixEffect.tryRevive(player);
            if (revived) {
                player.setHealth(player.getMaxHealth());
                player.timeUntilRegen = 0;
            }
        }
    }
}
