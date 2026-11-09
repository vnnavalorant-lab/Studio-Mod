package com.studio;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RealmTeleporter {
    private RealmTeleporter() {}

    private static final Map<UUID, Integer> LAST_USE = new HashMap<>();

    // Ponto de chegada padrao na dimensao
    private static final double SPAWN_X = 0.5;
    private static final double SPAWN_Y = 3;
    private static final double SPAWN_Z = 0.5;

    public static void toggle(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        // anti-spam (10 ticks)
        int now = server.getTicks();
        Integer last = LAST_USE.get(player.getUuid());
        if (last != null && now - last < 10) return;
        LAST_USE.put(player.getUuid(), now);

        ServerWorld realm = server.getWorld(Studio.REALM_KEY);
        if (realm == null) return;

        if (player.hasVehicle()) player.stopRiding();

        RealmState state = RealmState.get(server);
        ServerWorld current = player.getServerWorld();
        RealmState.Loc here = new RealmState.Loc(
                current.getRegistryKey().getValue().toString(),
                player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());

        if (current.getRegistryKey().equals(Studio.REALM_KEY)) {
            // ---- SAIR: volta para onde estava ----
            state.setRealm(player.getUuid(), here);
            RealmState.Loc ret = state.getReturn(player.getUuid());

            ServerWorld target = null;
            if (ret != null) {
                RegistryKey<World> key = RegistryKey.of(RegistryKeys.WORLD, new Identifier(ret.dim()));
                target = server.getWorld(key);
            }
            if (ret == null || target == null) {
                ServerWorld overworld = server.getOverworld();
                BlockPos sp = overworld.getSpawnPos();
                overworld.getChunk(sp);
                int y = overworld.getTopY(Heightmap.Type.MOTION_BLOCKING, sp.getX(), sp.getZ());
                go(player, overworld, sp.getX() + 0.5, y, sp.getZ() + 0.5, player.getYaw(), 0f);
            } else {
                go(player, target, ret.x(), ret.y(), ret.z(), ret.yaw(), ret.pitch());
            }
        } else {
            // ---- ENTRAR ----
            state.setReturn(player.getUuid(), here);

            RealmState.Loc dest = state.getRealm(player.getUuid());
            if (dest != null && dest.dim().equals(Studio.REALM_KEY.getValue().toString())) {
                go(player, realm, dest.x(), dest.y(), dest.z(), dest.yaw(), dest.pitch());
            } else {
                go(player, realm, SPAWN_X, SPAWN_Y, SPAWN_Z, 0f, 0f);
            }
        }
    }

    private static void go(ServerPlayerEntity p, ServerWorld w, double x, double y, double z, float yaw, float pitch) {
        w.getChunk(BlockPos.ofFloored(x, y, z)); // garante o chunk gerado antes de chegar
        p.teleport(w, x, y, z, yaw, pitch);
        p.fallDistance = 0.0F;
    }
}
