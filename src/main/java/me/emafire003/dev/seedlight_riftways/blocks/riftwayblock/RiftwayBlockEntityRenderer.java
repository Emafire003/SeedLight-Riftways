package me.emafire003.dev.seedlight_riftways.blocks.riftwayblock;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.client.SLRRenderLayers;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RiftwayBlockEntityRenderer extends EndPortalBlockEntityRenderer<RiftWayBlockEntity> {
    public static final Identifier PORTAL_TEXTURE_INACTIVE = new Identifier(SeedLightRiftways.MOD_ID, "textures/entity/riftway_inactive.png");
    public static final Identifier SKY_TEXTURE = new Identifier("textures/environment/portal_sky.png");
    public static final Identifier PORTAL_TEXTURE = new Identifier(SeedLightRiftways.MOD_ID, "textures/entity/riftway.png");

    public RiftwayBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    public void render(RiftWayBlockEntity blockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        /*if (RiftWayBlockEntity.needsCooldownBeforeTeleporting()) {
            float g = RiftWayBlockEntity.isRecentlyGenerated() ? RiftWayBlockEntity.getRecentlyGeneratedBeamHeight(f) : RiftWayBlockEntity.getCooldownBeamHeight(f);
            double d = RiftWayBlockEntity.isRecentlyGenerated() ? (double)RiftWayBlockEntity.getWorld().getTopY() : 50.0;
            g = MathHelper.sin(g * 3.1415927F);
            int k = MathHelper.floor((double)g * d);
            float[] fs = RiftWayBlockEntity.isRecentlyGenerated() ? DyeColor.MAGENTA.getColorComponents() : DyeColor.PURPLE.getColorComponents();
            long l = RiftWayBlockEntity.getWorld().getTime();
            BeaconBlockEntityRenderer.renderBeam(matrixStack, vertexConsumerProvider, BEAM_TEXTURE, f, g, l, -k, k * 2, fs, 0.15F, 0.175F);
        }*/

        super.render(blockEntity, f, matrixStack, vertexConsumerProvider, i, j);
    }
    @Override
    protected float getTopYOffset() {
        return 1.0F;
    }
    @Override
    protected float getBottomYOffset() {
        return 0.0F;
    }
    @Override
    protected RenderLayer getLayer() {
        if(SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE){
            return SLRRenderLayers.getRiftway();
        }
        return SLRRenderLayers.getRiftwayInactive();
    }
    @Override
    public int getRenderDistance() {
        return 256;
    }
}
