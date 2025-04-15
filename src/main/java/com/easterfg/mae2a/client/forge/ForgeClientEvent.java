package com.easterfg.mae2a.client.forge;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.easterfg.mae2a.client.render.CableRender;
import com.easterfg.mae2a.common.definition.ModItems;
import com.easterfg.mae2a.util.NBTHelper;
import com.easterfg.mae2a.util.VectorHelper;

/**
 * @author EasterFG on 2025/4/9
 */
@OnlyIn(Dist.CLIENT)
public final class ForgeClientEvent {

    private static int frameCount = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void onClientTickEvent(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        var player = Minecraft.getInstance().player;
        var level = Minecraft.getInstance().level;
        if (player == null || level == null)
            return;

        ItemStack heldItem = player.getMainHandItem();
        if (ModItems.CABLE_PLACE_TOOL.asItem() != heldItem.getItem()) {
            return;
        }

        var setting = NBTHelper.readSetting(heldItem);

        BlockHitResult lookingAt = VectorHelper.getLookAt(player, setting.getPicker());

        BlockPos endPos = lookingAt.getBlockPos();
        if (!level.getBlockState(endPos).isAir()) {
            endPos = endPos.relative(lookingAt.getDirection(), 1);
        }

        BlockPos startPos = NBTHelper.getBlockPos(heldItem, "start_pos");
        CableRender.render(event, Objects.requireNonNullElse(startPos, endPos), endPos);
        if (frameCount++ % 5 == 0 && startPos != null) {
            int manhattan = startPos.distManhattan(endPos);
            player.displayClientMessage(Component.literal(String.valueOf(manhattan)), true);
        }
    }
}
