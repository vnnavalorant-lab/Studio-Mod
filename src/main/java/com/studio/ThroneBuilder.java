package com.studio;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

/** Constroi (uma unica vez) o trono escuro da referencia. Sao apenas blocos, nenhuma entidade. */
public final class ThroneBuilder {
    private ThroneBuilder() {}

    /** Primeira camada acima do piso (piso: y=1 e y=2). */
    public static final int GROUND = 3;

    public static void ensureBuilt(ServerWorld world) {
        BlockState probe = world.getBlockState(new BlockPos(0, GROUND, -6));
        if (probe.isOf(ModBlocks.THRONE_BLOCK)) return;

        BlockState s = ModBlocks.THRONE_BLOCK.getDefaultState();

        // Encosto escalonado (centro mais alto)
        for (int x = -4; x <= 4; x++) {
            int ax = Math.abs(x);
            int h = ax <= 1 ? 16 : ax == 2 ? 11 : ax == 3 ? 8 : 6;
            for (int z = -7; z <= -6; z++) {
                for (int y = 0; y < h; y++) {
                    set(world, x, GROUND + y, z, s);
                }
            }
        }
        // Assento
        for (int x = -2; x <= 2; x++) {
            for (int z = -5; z <= -3; z++) {
                for (int y = 0; y < 2; y++) {
                    set(world, x, GROUND + y, z, s);
                }
            }
        }
        // Bracos do trono
        for (int ax = 3; ax <= 4; ax++) {
            for (int z = -5; z <= -3; z++) {
                for (int y = 0; y < 4; y++) {
                    set(world, ax, GROUND + y, z, s);
                    set(world, -ax, GROUND + y, z, s);
                }
            }
        }
    }

    private static void set(ServerWorld w, int x, int y, int z, BlockState s) {
        w.setBlockState(new BlockPos(x, y, z), s, Block.NOTIFY_LISTENERS);
    }
}
