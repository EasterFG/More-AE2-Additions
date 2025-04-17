package com.easterfg.mae2a.common.items;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.implementations.items.IAEItemPowerStorage;
import appeng.api.implementations.menuobjects.IMenuItem;
import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.blockentity.networking.WirelessAccessPointBlockEntity;

import com.easterfg.mae2a.client.KeyBindings;
import com.easterfg.mae2a.common.menu.host.CablePlaceToolHost;
import com.easterfg.mae2a.util.CableToolHelper;
import com.easterfg.mae2a.util.CableType;
import com.easterfg.mae2a.util.NBTHelper;
import com.easterfg.mae2a.util.VectorHelper;

/**
 * @author EasterFG on 2025/4/8
 */
public class ItemCablePlaceTool extends Item implements IMenuItem, IAEItemPowerStorage {

    public ItemCablePlaceTool(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable ItemMenuHost getMenuHost(Player player, int inventorySlot, ItemStack stack,
            @Nullable BlockPos pos) {
        return new CablePlaceToolHost(player, inventorySlot, stack);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
            @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide)
            return InteractionResultHolder.success(stack);

        if (player.isShiftKeyDown()) {
            player.getItemInHand(hand).removeTagKey(NBTHelper.START_POS_ID);
        } else {
            handlerClick(player, level, stack);
        }

        return new InteractionResultHolder<>(InteractionResult.sidedSuccess(level.isClientSide()),
                player.getItemInHand(hand));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var player = context.getPlayer();
        if (player == null)
            return InteractionResult.PASS;

        var level = context.getLevel();
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        if (bindME(stack, level, context.getClickedPos())) {
            player.displayClientMessage(Component.translatable("tools.mae2a.bind_ae"), false);
            return InteractionResult.SUCCESS;
        } else {
            handlerClick(player, level, stack);
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components,
            @NotNull TooltipFlag flag) {
        var data = NBTHelper.getBlocks(stack, NBTHelper.START_POS_ID, NBTHelper.BIND_POS_ID);
        var startPos = data.get(NBTHelper.START_POS_ID);
        var bindPos = data.get(NBTHelper.BIND_POS_ID);
        if (startPos != null) {
            components.add(Component.translatable("tooltip.mae2a.cable_place_tool.start_pos", startPos.getX(),
                    startPos.getY(), startPos.getZ()));
        } else {
            components.add(Component.translatable("tooltip.mae2a.cable_place_tool.not_start_pos"));
        }

        if (bindPos != null) {
            components.add(
                    Component.translatable("tooltip.mae2a.cable_place_tool.bind_ae", bindPos.getX(), bindPos.getY(),
                            bindPos.getZ()));
        } else {
            components.add(Component.translatable("tooltip.mae2a.cable_place_tool.not_bind_ae"));
        }

        var toolSetting = NBTHelper.readSetting(stack);
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.cable",
                CableType.values()[toolSetting.getCable()].getTranslation()));
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.color",
                Component.translatable(toolSetting.getColor().toString()).withStyle(ChatFormatting.AQUA)));
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.open_ui",
                Component.literal(KeyBindings.OPEN_CABLE_UI.getTranslatedKeyMessage().getString())
                        .withStyle(ChatFormatting.AQUA)));
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.1"));
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.2"));
    }

    private BlockPos getTargetBlock(Player player, Level level, int rayDistance) {
        BlockHitResult lookAt = VectorHelper.getLookAt(player, rayDistance);
        BlockPos target = lookAt.getBlockPos();
        return level.getBlockState(target).isAir() ? target : target.relative(lookAt.getDirection(), 1);
    }

    private void handlerClick(Player player, Level level, ItemStack stack) {
        var pos = NBTHelper.getBlockPos(stack, NBTHelper.START_POS_ID);
        var toolSetting = NBTHelper.readSetting(stack);
        if (pos == null) {
            BlockPos targetBlock = getTargetBlock(player, level, toolSetting.getPicker());
            NBTHelper.saveBlockPos(stack, targetBlock, NBTHelper.START_POS_ID);
        } else {
            place(player, level, stack);
        }
    }

    private boolean bindME(ItemStack stack, Level level, BlockPos click) {
        BlockEntity tile = level.getBlockEntity(click);
        if (tile instanceof WirelessAccessPointBlockEntity wireless && wireless.getGridNode() != null) {
            NBTHelper.saveBlockPos(stack, click, NBTHelper.BIND_POS_ID);
            return true;
        }
        return false;
    }

    private void place(Player player, Level level, ItemStack stack) {
        var start = NBTHelper.getBlockPos(stack, NBTHelper.START_POS_ID);
        if (start == null) {
            return;
        }
        var toolSetting = NBTHelper.readSetting(stack);
        BlockPos end = getTargetBlock(player, level, toolSetting.getPicker());
        var bind = NBTHelper.getBlockPos(stack, NBTHelper.BIND_POS_ID);

        CableToolHelper.place(player, level, bind, start, end, toolSetting);
        stack.removeTagKey(NBTHelper.START_POS_ID);
    }

    @Override
    public double injectAEPower(ItemStack stack, double amount, Actionable mode) {
        return 0;
    }

    @Override
    public double extractAEPower(ItemStack stack, double amount, Actionable mode) {
        return 0;
    }

    @Override
    public double getAEMaxPower(ItemStack stack) {
        return 0;
    }

    @Override
    public double getAECurrentPower(ItemStack stack) {
        return 0;
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack stack) {
        return null;
    }

    @Override
    public double getChargeRate(ItemStack stack) {
        return 0;
    }
}
