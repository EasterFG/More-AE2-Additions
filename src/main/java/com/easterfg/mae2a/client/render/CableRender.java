package com.easterfg.mae2a.client.render;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import appeng.hooks.RenderBlockOutlineHook;

/**
 * @author EasterFG on 2025/4/9
 */
public class CableRender {

    private static final double THICKNESS = 0.2;

    public static void render(RenderLevelStageEvent event, @NotNull BlockPos startPos, @NotNull BlockPos endPos) {
        List<Pair<Vec3, Vec3>> segments = generateAdjustedSegments(startPos.getCenter(), endPos.getCenter());

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        VoxelShape finalShape = buildCompleteShape(segments);

        renderVoxelShape(RenderType.LINES, poseStack, bufferSource, finalShape, 0xFF_E6194B);
        renderVoxelShape(RenderBlockOutlineHook.LINES_BEHIND_BLOCK, poseStack, bufferSource, finalShape, 0x80_E6194B);

        poseStack.popPose();
        bufferSource.endBatch();
    }

    private static VoxelShape buildCompleteShape(List<Pair<Vec3, Vec3>> segments) {
        List<AABB> allBoxes = new ArrayList<>();

        for (Pair<Vec3, Vec3> seg : segments) {
            Vec3 start = seg.getFirst();
            Vec3 end = seg.getSecond();
            allBoxes.add(new AABB(start, end).inflate(THICKNESS));
        }

        return mergeBoxes(allBoxes);
    }

    public static VoxelShape mergeBoxes(List<AABB> boxes) {
        VoxelShape shape = Shapes.empty();
        for (AABB box : boxes) {
            VoxelShape boxShape = Shapes.create(box);
            shape = Shapes.or(shape, boxShape);
        }
        return shape;
    }

    private static void renderVoxelShape(RenderType type, PoseStack poseStack, MultiBufferSource buffer,
            VoxelShape shape, int color) {
        VertexConsumer consumer = buffer.getBuffer(type);
        shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            float dx = (float) (x2 - x1);
            float dy = (float) (y2 - y1);
            float dz = (float) (z2 - z1);
            float vex = Mth.sqrt(dx * dx + dy * dy + dz * dz);

            dx /= vex;
            dy /= vex;
            dz /= vex;

            consumer.vertex(poseStack.last().pose(), (float) x1, (float) y1, (float) z1)
                    .color(color)
                    .normal(poseStack.last().normal(), dx, dy, dz)
                    .endVertex();
            consumer.vertex(poseStack.last().pose(), (float) x2, (float) y2, (float) z2)
                    .color(color)
                    .normal(poseStack.last().normal(), dx, dy, dz)
                    .endVertex();
        });
    }

    public static List<Pair<Vec3, Vec3>> generateAdjustedSegments(Vec3 start, Vec3 end) {
        List<Pair<Vec3, Vec3>> rawSegments = new ArrayList<>();
        Vec3 current = start;

        if (start.equals(end)) {
            return List.of(Pair.of(start, end));
        }

        double x = end.x - current.x;
        if (x != 0) {
            Vec3 next = new Vec3(end.x, current.y, current.z);
            rawSegments.add(Pair.of(current, next));
            current = next;
        }

        double z = end.z - current.z;
        if (z != 0) {
            Vec3 next = new Vec3(current.x, current.y, end.z);
            rawSegments.add(Pair.of(current, next));
            current = next;
        }

        double y = end.y - current.y;
        if (y != 0) {
            Vec3 next = new Vec3(current.x, end.y, current.z);
            rawSegments.add(Pair.of(current, next));
            current = next;
        }

        if (!current.equals(end)) {
            rawSegments.add(Pair.of(current, end));
        }

        return rawSegments;
    }
}
