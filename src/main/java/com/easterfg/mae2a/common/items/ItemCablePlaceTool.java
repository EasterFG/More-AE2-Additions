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
import com.easterfg.mae2a.util.CableType;
import com.easterfg.mae2a.util.NBTHelper;
import com.easterfg.mae2a.util.VectorHelper;

/**
 * @author EasterFG on 2025/4/80.
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
            NBTHelper.removeLastBlock(player.getItemInHand(hand), NBTHelper.POS_LIST_ID);
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
            NBTHelper.saveGlobalPos(stack, wireless.getGlobalPos(), NBTHelper.BIND_POS_ID);
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
        var bindPos = NBTHelper.getGlobalPos(stack, NBTHelper.BIND_POS_ID);
        var toolSetting = NBTHelper.readSetting(stack);

        if (bindPos != null) {
            var pos = bindPos.pos();
            components.add(
                    Component.translatable("tooltip.mae2a.cable_place_tool.bind_ae", pos.getX(), pos.getY(),
                            pos.getZ()));
        } else {
            components.add(Component.translatable("tooltip.mae2a.cable_place_tool.not_bind_ae"));
        }

        components.add(Component.translatable("tooltip.mae2a.fast_place_tool.select_tip"));
        components.add(Component.translatable("tooltip.mae2a.fast_place_tool.undo_select_tip"));
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.cable",
                CableType.values()[toolSetting.getCable()].getTranslation()));
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.color",
                Component.translatable(toolSetting.getColor().toString())));
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.open_ui",
                Component.literal(KeyBindings.OPEN_CABLE_UI.getTranslatedKeyMessage().getString())
                        .withStyle(ChatFormatting.AQUA)));
        components.add(Component.translatable("tooltip.mae2a.cable_place_tool.place_cable",
                Component.literal(KeyBindings.PLACE_CABLE.getTranslatedKeyMessage().getString())
                        .withStyle(ChatFormatting.AQUA)));
    }

    private BlockPos getTargetBlock(Player player, Level level, int rayDistance) {
        BlockHitResult lookAt = VectorHelper.getLookAt(player, rayDistance);
        BlockPos target = lookAt.getBlockPos();
        return level.getBlockState(target).isAir() ? target : target.relative(lookAt.getDirection(), 1);
    }

    private void handlerClick(Player player, Level level, ItemStack stack) {
        var toolSetting = NBTHelper.readSetting(stack);
        BlockPos targetBlock = getTargetBlock(player, level, toolSetting.getPicker());
        NBTHelper.addBlock(stack, NBTHelper.POS_LIST_ID, targetBlock);
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
