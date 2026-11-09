package com.studio.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.studio.Studio;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    /** Neblina densa e escura na dimensao Studio (comeca colado no jogador e engole tudo em ~26 blocos). */
    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void studio$denseFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance,
                                        boolean thickFog, float tickDelta, CallbackInfo ci) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null || !world.getRegistryKey().equals(Studio.REALM_KEY)) return;
        if (camera.getSubmersionType() != CameraSubmersionType.NONE) return;

        if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {
            RenderSystem.setShaderFogStart(0.0F);
            RenderSystem.setShaderFogEnd(26.0F);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        }
    }
}
