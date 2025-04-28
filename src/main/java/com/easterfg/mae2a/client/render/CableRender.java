package com.easterfg.mae2a.client.render;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import appeng.hooks.RenderBlockOutlineHook;

import com.easterfg.mae2a.config.MAE2AConfig;
import com.easterfg.mae2a.util.VectorHelper;

/**
 * @author EasterFG on 2025/4/9
 */
public class CableRender {

    private static final double THICKNESS = 0.2;

    private static boolean dirty = true;

    private static VoxelShape CACHE;

    public static void render(RenderLevelStageEvent event, @NotNull List<Vec3> blocks, Vec3 endPos) {
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        List<Pair<Vec3, Vec3>> segments;
        if (blocks.isEmpty()) {
            segments = List.of(Pair.of(endPos, endPos));
        } else {
            segments = VectorHelper.generateMultiSegments(blocks);
        }

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        if (dirty && !segments.isEmpty()) {
            dirty = false;
            CACHE = merge(segments);
        }

        VoxelShape shape = blocks.isEmpty() ? merge(segments) : merge(blocks.get(blocks.size() - 1), endPos);

        renderVoxelShape(RenderType.LINES, poseStack, bufferSource, shape, MAE2AConfig.boxColor);
        renderVoxelShape(RenderBlockOutlineHook.LINES_BEHIND_BLOCK, poseStack, bufferSource, shape,
                MAE2AConfig.boxColor);

        poseStack.popPose();
        bufferSource.endBatch();
    }

    public static void markDirty() {
        dirty = true;
    }

    private static VoxelShape merge(List<Pair<Vec3, Vec3>> segments) {
        if (segments.isEmpty())
            return Shapes.empty();
        VoxelShape shape = Shapes.empty();
        return segments.stream()
                .map(pair -> new AABB(pair.getFirst(), pair.getSecond()).inflate(THICKNESS))
                .map(Shapes::create)
                .reduce(shape, Shapes::or);
    }

    private static VoxelShape merge(Vec3 last, Vec3 end) {
        if (last.equals(end)) {
            return Shapes.create(new AABB(last, end).inflate(THICKNESS));
        }
        return VectorHelper.generateAdjustedSegments(last, end)
                .stream()
                .map(pair -> new AABB(pair.getFirst(), pair.getSecond()).inflate(THICKNESS))
                .map(Shapes::create)
                .reduce(CACHE, Shapes::or);
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
}
