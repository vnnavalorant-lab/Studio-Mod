package com.studio.client;

import com.studio.Studio;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class StudioClient implements ClientModInitializer {
    private static KeyBinding toggleKey;
    private static long suppressUntil = 0L;

    /** Enquanto verdadeiro, as telas "Carregando terreno" / "Entrando" sao ignoradas (troca instantanea). */
    public static boolean shouldSuppressLoadingScreen() {
        return System.currentTimeMillis() < suppressUntil;
    }

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.studio.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "key.categories.studio"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                if (client.player == null) continue;
                long window = client.getWindow().getHandle();
                boolean ctrl = InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_LEFT_CONTROL)
                        || InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_RIGHT_CONTROL);
                if (!ctrl) continue;

                suppressUntil = System.currentTimeMillis() + 4000L;
                ClientPlayNetworking.send(Studio.TOGGLE_PACKET, PacketByteBufs.empty());
            }
        });
    }
}
