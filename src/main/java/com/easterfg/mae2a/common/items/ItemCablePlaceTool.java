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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.implementations.menuobjects.IMenuItem;
import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.blockentity.misc.ChargerBlockEntity;
import appeng.blockentity.networking.WirelessAccessPointBlockEntity;
import appeng.items.tools.powered.powersink.AEBasePoweredItem;

import com.easterfg.mae2a.client.KeyBindings;
import com.easterfg.mae2a.common.menu.host.CablePlaceToolHost;
import com.easterfg.mae2a.util.CableToolHelper;
import com.easterfg.mae2a.util.CableType;
import com.easterfg.mae2a.util.NBTHelper;
import com.easterfg.mae2a.util.VectorHelper;

/**
 * @author EasterFG on 2025/4/8
 */
public class ItemCablePlaceTool extends AEBasePoweredItem implements IMenuItem {

    public ItemCablePlaceTool(Properties properties) {
        super(() -> 640000, properties);
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

        var click = context.getClickedPos();
        var tile = level.getBlockEntity(click);
        if (tile instanceof WirelessAccessPointBlockEntity wireless && wireless.getGridNode() != null) {
            NBTHelper.saveBlockPos(stack, click, NBTHelper.BIND_POS_ID);
            player.displayClientMessage(Component.translatable("tools.mae2a.bind_ae"), false);
        } else if (tile instanceof ChargerBlockEntity) {
            return InteractionResult.PASS;
        } else {
            handlerClick(player, level, stack);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components,
            @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        var nbt = stack.getTag();
        var bindPos = NBTHelper.getBlockPos(nbt, NBTHelper.BIND_POS_ID);
        var toolSetting = NBTHelper.readSetting(stack);

        if (bindPos != null) {
            components.add(
                    Component.translatable("tooltip.mae2a.cable_place_tool.bind_ae", bindPos.getX(), bindPos.getY(),
                            bindPos.getZ()));
        } else {
            components.add(Component.translatable("tooltip.mae2a.cable_place_tool.not_bind_ae"));
        }

        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.cable",
                CableType.values()[toolSetting.getCable()].getTranslation()));
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.color",
                Component.translatable(toolSetting.getColor().toString())));
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

    private void place(Player player, Level level, ItemStack stack) {
        var start = NBTHelper.getBlockPos(stack, NBTHelper.START_POS_ID);
        if (start == null) {
            return;
        }
        var toolSetting = NBTHelper.readSetting(stack);
        BlockPos end = getTargetBlock(player, level, toolSetting.getPicker());
        var bind = NBTHelper.getBlockPos(stack, NBTHelper.BIND_POS_ID);

        CableToolHelper.place(stack, player, level, bind, start, end, toolSetting);
        stack.removeTagKey(NBTHelper.START_POS_ID);
    }

    @Override
    public double getAEMaxPower(ItemStack stack) {
        return 640000;
    }

    @Override
    public double getChargeRate(ItemStack stack) {
        return 200D;
    }
}
