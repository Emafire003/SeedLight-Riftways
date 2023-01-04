package me.emafire003.dev.seedlight_riftways.client;

import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftwayBlockEntityRenderer;
import me.emafire003.dev.seedlight_riftways.mixin.RenderLayersOfInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.EndGatewayBlockEntityRenderer;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class SLRRenderLayers extends RenderLayer {

    public SLRRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
    private static final RenderLayer RIFTWAY;
    private static final RenderLayer RIFTWAY_INACTIVE;

    public static RenderLayer getRiftway() {
        return RIFTWAY;
    }

    public static RenderLayer getRiftwayInactive() {
        return RIFTWAY_INACTIVE;
    }

    static {
        RIFTWAY = RenderLayersOfInvoker.of("RIFTWAY", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, MultiPhaseParameters.builder().program(END_GATEWAY_PROGRAM).texture(Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE, true, false).add(RiftwayBlockEntityRenderer.PORTAL_TEXTURE, true, false).build()).build(false));
        RIFTWAY_INACTIVE = RenderLayersOfInvoker.of("RIFTWAY", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, MultiPhaseParameters.builder().program(END_GATEWAY_PROGRAM).texture(Textures.create().add(RiftwayBlockEntityRenderer.SKY_TEXTURE, false, false).add(RiftwayBlockEntityRenderer.PORTAL_TEXTURE_INACTIVE, false, false).build()).build(false));

    }
}
