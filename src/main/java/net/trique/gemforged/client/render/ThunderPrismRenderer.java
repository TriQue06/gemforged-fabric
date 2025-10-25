package net.trique.gemforged.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.entity.ThunderPrismEntity;

public class ThunderPrismRenderer extends EntityRenderer<ThunderPrismEntity> {
    private static final Identifier TEXTURE = Identifier.of(Gemforged.MOD_ID, "textures/item/thunder_prism.png");

    public ThunderPrismRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(ThunderPrismEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        matrices.push();

        float interpYaw = entity.prevYaw + (entity.getYaw() - entity.prevYaw) * tickDelta;
        float interpPitch = entity.prevPitch + (entity.getPitch() - entity.prevPitch) * tickDelta;

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-interpYaw + 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(interpPitch));

        matrices.translate(0.0F, -0.1F, 0.0F);

        MinecraftClient.getInstance().getItemRenderer().renderItem(
                entity.getItem(),
                ModelTransformationMode.FIXED,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                0
        );

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(ThunderPrismEntity entity) {
        return TEXTURE;
    }
}