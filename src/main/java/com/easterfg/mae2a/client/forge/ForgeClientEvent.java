package com.easterfg.mae2a.client.forge;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import appeng.blockentity.networking.CableBusBlockEntity;

import com.easterfg.mae2a.client.render.CableRender;
import com.easterfg.mae2a.common.definition.MAE2AItems;
import com.easterfg.mae2a.util.NBTHelper;
import com.easterfg.mae2a.util.VectorHelper;

/**
 * @author EasterFG on 2025/4/9
 */
@OnlyIn(Dist.CLIENT)
public final class ForgeClientEvent {
    private static List<Vec3> CACHE;

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
        if (MAE2AItems.CABLE_PLACE_TOOL.asItem() != heldItem.getItem()) {
            return;
        }

        var setting = NBTHelper.readSetting(heldItem);

        BlockHitResult lookingAt = VectorHelper.getLookAt(player, setting.getPicker());

        BlockPos endPos = lookingAt.getBlockPos();
        var te = level.getBlockEntity(endPos);
        if (!(te instanceof CableBusBlockEntity)) {
            endPos = endPos.relative(lookingAt.getDirection(), 1);
        }

        var blocks = NBTHelper.getBlockList(heldItem.getOrCreateTag(), NBTHelper.POS_LIST_ID);
        if (CACHE == null || CACHE.size() != blocks.size()) {
            CACHE = blocks.stream().map(BlockPos::getCenter).toList();
        }

        CableRender.render(event, CACHE, endPos.getCenter());
    }
}
