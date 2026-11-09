package com.studio;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Guarda por jogador: de onde ele veio e onde parou dentro da dimensao. Persiste no save. */
public class RealmState extends PersistentState {
    public static final String ID = "studio_data";

    public record Loc(String dim, double x, double y, double z, float yaw, float pitch) {
        NbtCompound toNbt() {
            NbtCompound n = new NbtCompound();
            n.putString("dim", dim);
            n.putDouble("x", x);
            n.putDouble("y", y);
            n.putDouble("z", z);
            n.putFloat("yaw", yaw);
            n.putFloat("pitch", pitch);
            return n;
        }

        static Loc fromNbt(NbtCompound n) {
            return new Loc(n.getString("dim"), n.getDouble("x"), n.getDouble("y"), n.getDouble("z"),
                    n.getFloat("yaw"), n.getFloat("pitch"));
        }
    }

    private final Map<UUID, Loc> returns = new HashMap<>();
    private final Map<UUID, Loc> realm = new HashMap<>();

    public static RealmState get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager()
                .getOrCreate(RealmState::fromNbt, RealmState::new, ID);
    }

    public Loc getReturn(UUID id) { return returns.get(id); }
    public Loc getRealm(UUID id) { return realm.get(id); }

    public void setReturn(UUID id, Loc loc) { returns.put(id, loc); markDirty(); }
    public void setRealm(UUID id, Loc loc) { realm.put(id, loc); markDirty(); }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put("returns", write(returns));
        nbt.put("realm", write(realm));
        return nbt;
    }

    public static RealmState fromNbt(NbtCompound nbt) {
        RealmState s = new RealmState();
        read(nbt.getCompound("returns"), s.returns);
        read(nbt.getCompound("realm"), s.realm);
        return s;
    }

    private static NbtCompound write(Map<UUID, Loc> map) {
        NbtCompound out = new NbtCompound();
        map.forEach((id, loc) -> out.put(id.toString(), loc.toNbt()));
        return out;
    }

    private static void read(NbtCompound in, Map<UUID, Loc> map) {
        for (String key : in.getKeys()) {
            try {
                map.put(UUID.fromString(key), Loc.fromNbt(in.getCompound(key)));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
