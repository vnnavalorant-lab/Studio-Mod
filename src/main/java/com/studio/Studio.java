package com.studio;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class Studio implements ModInitializer {
    public static final String MOD_ID = "studio";

    /** Pacote cliente -> servidor: "alternar dimensao". */
    public static final Identifier TOGGLE_PACKET = new Identifier(MOD_ID, "toggle_realm");

    /** Chave da dimensao (data/studio/dimension/studio.json). */
    public static final RegistryKey<World> REALM_KEY =
            RegistryKey.of(RegistryKeys.WORLD, new Identifier(MOD_ID, "studio"));

    @Override
    public void onInitialize() {
        ModBlocks.register();

        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_PACKET,
                (server, player, handler, buf, responseSender) ->
                        server.execute(() -> RealmTeleporter.toggle(player)));
    }
}
