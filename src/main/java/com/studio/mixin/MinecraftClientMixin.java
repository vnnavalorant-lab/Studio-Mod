package com.studio.mixin;

import com.studio.client.StudioClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    /** Remove a tela de carregamento ao trocar de dimensao pelo atalho: a troca parece instantanea. */
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void valkyrie$skipLoadingScreens(Screen screen, CallbackInfo ci) {
        if (screen != null
                && StudioClient.shouldSuppressLoadingScreen()
                && (screen instanceof DownloadingTerrainScreen || screen instanceof ProgressScreen)) {
            ci.cancel();
        }
    }
}
