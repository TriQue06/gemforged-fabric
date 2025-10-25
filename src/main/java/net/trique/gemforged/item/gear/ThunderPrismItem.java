package net.trique.gemforged.item.gear;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.trique.gemforged.entity.ThunderPrismEntity;
import net.trique.gemforged.item.GemforgedItems;

public class ThunderPrismItem extends Item {

    public ThunderPrismItem(Settings settings) {
        super(settings.maxDamage(250));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            if (user.getItemCooldownManager().isCoolingDown(this)) {
                return TypedActionResult.fail(stack);
            }

            boolean creative = user.getAbilities().creativeMode;
            ItemStack chargeResource = findChargeResource(user);

            if (creative || !chargeResource.isEmpty()) {
                world.playSound(null, user.getBlockPos(),
                        SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS,
                        0.9F, 0.6F + world.getRandom().nextFloat() * 0.2F);

                ItemStack prismStack = stack.copy();
                prismStack.setCount(1);

                ThunderPrismEntity prism = new ThunderPrismEntity(world,
                        user.getX(), user.getY() + 1.0, user.getZ(), user);
                prism.setItem(prismStack);
                world.spawnEntity(prism);

                user.getItemCooldownManager().set(this, 20 * 45);

                if (!creative) {
                    chargeResource.decrement(1);
                    stack.damage(1, user, p -> p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    private ItemStack findChargeResource(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack s = player.getInventory().getStack(i);
            if (s.isOf(GemforgedItems.PRISMYTE)) return s;
        }
        return ItemStack.EMPTY;
    }
}