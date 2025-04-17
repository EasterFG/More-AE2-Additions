package com.easterfg.mae2a.util;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

/**
 * @author EasterFG on 2025/4/9
 */
public final class VectorHelper {
    private VectorHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static BlockHitResult getLookAt(Player player, double rayDistance) {
        var result = player.pick(rayDistance, 0f, false);
        return (BlockHitResult) result;
    }

    public static Set<BlockPos> generatePath(BlockPos start, BlockPos end) {
        if (start.equals(end)) {
            return Set.of(start);
        }

        Set<BlockPos> path = new LinkedHashSet<>();
        BlockPos current = start;

        int xStep = Integer.compare(end.getX(), current.getX());
        if (xStep != 0) {
            for (int x = current.getX(); x != end.getX() + xStep; x += xStep) {
                path.add(new BlockPos(x, current.getY(), current.getZ()));
            }
            current = new BlockPos(end.getX(), current.getY(), current.getZ());
        }

        int zStep = Integer.compare(end.getZ(), current.getZ());
        if (zStep != 0) {
            for (int z = current.getZ(); z != end.getZ() + zStep; z += zStep) {
                path.add(new BlockPos(current.getX(), current.getY(), z));
            }
            current = new BlockPos(current.getX(), current.getY(), end.getZ());
        }

        int yStep = Integer.compare(end.getY(), current.getY());
        if (yStep != 0) {
            for (int y = current.getY(); y != end.getY() + yStep; y += yStep) {
                path.add(new BlockPos(current.getX(), y, current.getZ()));
            }

        }

        return path;
    }
}
