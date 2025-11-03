package net.trique.gemforged.item.gear;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.trique.gemforged.entity.GemforgedEntities;
import net.trique.gemforged.entity.GhostArrowEntity;
import net.trique.gemforged.item.GemforgedItems;

public class GhostBowItem extends BowItem {

    public static final float BASE_DAMAGE = 3.0F;
    public static final float BONUS_MAGIC_DAMAGE = 1.0F;
    public static final int COOLDOWN_TICKS = 100;

    public GhostBowItem(Settings settings) {
        super(settings.maxDamage(240));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack bow = user.getStackInHand(hand);
        if (!hasNyxite(user)) {
            return TypedActionResult.fail(bow);
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(bow);
    }

    private boolean hasNyxite(PlayerEntity player) {
        if (player.getAbilities().creativeMode) return true;
        return player.getInventory().contains(new ItemStack(GemforgedItems.NYXITE));
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;
        if (!hasNyxite(player)) return;

        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        float power = getPullProgress(i);
        if (power < 0.1F) return;

        if (!world.isClient) {

            GhostArrowEntity arrow = new GhostArrowEntity(GemforgedEntities.GHOST_ARROW, world);
            arrow.setOwner(player);

            arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());

            arrow.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, power * 3.0F, 1.0F);

            arrow.setDamage(BASE_DAMAGE + BONUS_MAGIC_DAMAGE);
            if (power >= 1.0F) arrow.setCritical(true);

            world.spawnEntity(arrow);

            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS,
                    1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + (power * 0.5F));

            if (!player.getAbilities().creativeMode) {
                consumeNyxite(player);
                stack.damage(1, player, p -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            }
        }

        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    private void consumeNyxite(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack slot = player.getInventory().getStack(i);
            if (slot.isOf(GemforgedItems.NYXITE)) {
                slot.decrement(1);
                break;
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
}
