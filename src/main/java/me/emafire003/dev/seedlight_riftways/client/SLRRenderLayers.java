package me.emafire003.dev.seedlight_riftways.client;

import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftwayBlockEntityRenderer;
import me.emafire003.dev.seedlight_riftways.mixin.RenderLayersOfInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class SLRRenderLayers extends RenderLayer {

    public SLRRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
    private static final RenderLayer SQUARE_PORTAL;
    private static final RenderLayer SQUARE_PORTAL_PINK;

    public static RenderLayer getSquarePortal() {
        return SQUARE_PORTAL;
    }
    public static RenderLayer getSquarePortalPink() {
        return SQUARE_PORTAL_PINK;
    }

    static {
        SQUARE_PORTAL = RenderLayersOfInvoker.of("square_portal", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, MultiPhaseParameters.builder().shader(END_GATEWAY_SHADER).texture(Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE, true, false).add(RiftwayBlockEntityRenderer.PORTAL_TEXTURE, true, false).build()).build(false));
        SQUARE_PORTAL_PINK = RenderLayersOfInvoker.of("square_portal", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, MultiPhaseParameters.builder().shader(END_GATEWAY_SHADER).texture(Textures.create().add(RiftwayBlockEntityRenderer.SKY_TEXTURE, false, false).add(RiftwayBlockEntityRenderer.PORTAL_TEXTURE, false, false).build()).build(false));

    }
}
