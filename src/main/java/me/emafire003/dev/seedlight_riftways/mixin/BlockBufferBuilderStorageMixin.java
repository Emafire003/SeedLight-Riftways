package me.emafire003.dev.seedlight_riftways.mixin;

import me.emafire003.dev.seedlight_riftways.client.SLRRenderLayers;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BlockBufferBuilderStorage.class)
public abstract class BlockBufferBuilderStorageMixin {

    @Shadow
    @Final
    private Map<RenderLayer, BufferBuilder> builders;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo callbackInfo) {
        this.builders.put(SLRRenderLayers.getRiftway(), new BufferBuilder(SLRRenderLayers.getRiftway().getExpectedBufferSize()));
    }

}
