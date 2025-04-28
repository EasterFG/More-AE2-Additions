package com.easterfg.mae2a.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

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

    public static Set<BlockPos> generatePath(List<BlockPos> nodes) {
        if (nodes == null || nodes.size() < 2) {
            return new LinkedHashSet<>();
        }

        return IntStream.range(1, nodes.size())
                .parallel()
                .mapToObj(i -> generatePath(nodes.get(i - 1), nodes.get(i)))
                .flatMap(Set::stream)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public static Set<BlockPos> generatePath(BlockPos start, BlockPos end) {
        if (start.equals(end)) {
            return Collections.singleton(start);
        }

        Set<BlockPos> path = new LinkedHashSet<>();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(start.getX(), start.getY(), start.getZ());

        path.add(start);
        processAxis(path, mutablePos, end, Axis.X);
        processAxis(path, mutablePos, end, Axis.Z);
        processAxis(path, mutablePos, end, Axis.Y);

        return path;
    }

    private static void processAxis(Set<BlockPos> path,
            BlockPos.MutableBlockPos mutablePos,
            BlockPos end,
            Axis axis) {
        int current = getCoordinate(mutablePos, axis);
        int target = getCoordinate(end, axis);
        int step = Integer.compare(target, current);

        if (step == 0)
            return;
        while (current != target) {
            current += step;
            setCoordinate(mutablePos, axis, current);
            path.add(mutablePos.immutable());
        }
    }

    private static int getCoordinate(BlockPos pos, Axis axis) {
        return switch (axis) {
            case X -> pos.getX();
            case Y -> pos.getY();
            case Z -> pos.getZ();
        };
    }

    private static void setCoordinate(BlockPos.MutableBlockPos mutablePos,
            Axis axis,
            int value) {
        switch (axis) {
            case X -> mutablePos.setX(value);
            case Y -> mutablePos.setY(value);
            case Z -> mutablePos.setZ(value);
        }
    }

    public static List<Pair<Vec3, Vec3>> generateMultiSegments(@NotNull List<Vec3> nodes) {
        return IntStream.range(1, nodes.size())
                .mapToObj(i -> Pair.of(nodes.get(i - 1), nodes.get(i)))
                .flatMap(pair -> generateAdjustedSegments(pair.getFirst(), pair.getSecond()).stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Pair<Vec3, Vec3>> generateAdjustedSegments(Vec3 start, Vec3 end) {
        List<Pair<Vec3, Vec3>> segments = new ArrayList<>();

        if (start.equals(end)) {
            segments.add(Pair.of(start, end));
            return segments;
        }

        Vec3 current = start;

        double xDiff = end.x - current.x;
        if (xDiff != 0) {
            Vec3 next = new Vec3(end.x, current.y, current.z);
            segments.add(Pair.of(current, next));
            current = next;
        }

        double zDiff = end.z - current.z;
        if (zDiff != 0) {
            Vec3 next = new Vec3(current.x, current.y, end.z);
            segments.add(Pair.of(current, next));
            current = next;
        }

        double yDiff = end.y - current.y;
        if (yDiff != 0) {
            Vec3 next = new Vec3(current.x, end.y, current.z);
            segments.add(Pair.of(current, next));
            current = next;
        }

        if (!current.equals(end)) {
            segments.add(Pair.of(current, end));
        }

        return segments;
    }
}
