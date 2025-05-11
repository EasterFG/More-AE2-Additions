package com.easterfg.mae2a.common.items;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.phys.Vec3;

import appeng.api.implementations.menuobjects.IMenuItem;
import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.parts.IPart;
import appeng.api.util.DimensionalBlockPos;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.helpers.patternprovider.PatternContainer;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.parts.encoding.PatternEncodingTerminalPart;
import appeng.util.Platform;

import com.easterfg.mae2a.common.menu.PatternModifyMenu;
import com.easterfg.mae2a.common.menu.PatternPreviewListMenu;
import com.easterfg.mae2a.common.menu.host.PatternModifyHost;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import com.easterfg.mae2a.util.PatternUtils;

/**
 * @author EasterFG on 2024/9/30
 */
public class PatternModifyToolItem extends Item implements IMenuItem {

    public PatternModifyToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> tooltip,
            @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.mae2a.pattern_modify_tool.1"));
        tooltip.add(Component.translatable("tooltip.mae2a.pattern_modify_tool.limit"));
        tooltip.add(Component.translatable("tooltip.mae2a.pattern_modify_tool.rate"));
        tooltip.add(Component.translatable("tooltip.mae2a.pattern_modify_tool.preview"));
        tooltip.add(Component.translatable("tooltip.mae2a.pattern_modify_tool.block"));
        tooltip.add(Component.translatable("tooltip.mae2a.pattern_modify_tool.network"));
        tooltip.add(Component.translatable("tooltip.mae2a.pattern_modify_tool.warring"));
    }

    @Override
    public @Nullable ItemMenuHost getMenuHost(Player player, int inventorySlot, ItemStack stack,
            @Nullable BlockPos pos) {
        return new PatternModifyHost(player, inventorySlot, stack, pos);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player,
            @NotNull InteractionHand hand) {
        if (!level.isClientSide()) {
            MenuOpener.open(PatternModifyMenu.TYPE, player, MenuLocators.forHand(player, hand));
        }
        return new InteractionResultHolder<>(InteractionResult.sidedSuccess(level.isClientSide()),
                player.getItemInHand(hand));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null)
            return InteractionResult.FAIL;

        Level level = context.getLevel();

        CompoundTag tag = stack.getOrCreateTag();
        tag.put("hitPos", PatternUtils.writeVec3(context.getClickLocation()));

        if (!level.isClientSide()) {
            if (!showToolGui(stack, context)) {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    public static PatternProviderLogic findPatternProvide(Level level, BlockPos pos, Vec3 hit) {
        var te = level.getBlockEntity(pos);
        if (te instanceof PatternProviderLogicHost host) {
            return host.getLogic();
        } else {
            IPart cable = findPartInCable(level, pos, hit);
            if (cable instanceof PatternProviderLogicHost host) {
                return host.getLogic();
            }
        }
        return null;
    }

    public static IPart findPartInCable(Level level, BlockPos pos, Vec3 hit) {
        var te = level.getBlockEntity(pos);
        if (te instanceof CableBusBlockEntity cable) {
            Vec3 target = new Vec3(hit.x - pos.getX(), hit.y - pos.getY(), hit.z - pos.getZ());
            return cable.getCableBus().selectPartLocal(target).part;
        }
        return null;
    }

    public boolean showToolGui(@NotNull ItemStack stack, @NotNull UseOnContext useContext) {
        if (useContext.getPlayer() == null) {
            return false;
        }

        BlockPos pos = useContext.getClickedPos();
        Player p = useContext.getPlayer();
        Level level = useContext.getLevel();

        if (!Platform.hasPermissions(new DimensionalBlockPos(level, pos), p)) {
            return false;
        }

        var nodeHost = GridHelper.getNodeHost(level, pos);
        if (nodeHost == null) {
            MenuOpener.open(PatternModifyMenu.TYPE, p, MenuLocators.forHand(p, useContext.getHand()));
        } else {
            boolean network = false;
            PatternContainer container = null;
            BlockEntity te = level.getBlockEntity(pos);
            IPart cable = findPartInCable(level, pos, useContext.getClickLocation());
            if (cable == null && te instanceof PatternContainer) {
                container = (PatternContainer) te;
            } else {
                if (cable instanceof PatternContainer) {
                    container = (PatternContainer) cable;
                } else if (cable instanceof PatternEncodingTerminalPart) {
                    network = true;
                }
            }

            if (container != null) {
                if (container.getTerminalPatternInventory().isEmpty()) {
                    p.displayClientMessage(Component.translatable("tools.mae2a.no_pattern"), true);
                    return false;
                }
                MenuOpener.open(PatternPreviewListMenu.TYPE, p, MenuLocators.forItemUseContext(useContext));
                return true;
            } else if (!network) {
                return false;
            }

            PatternModifySetting setting = new PatternModifySetting();
            setting.readFromNBT(stack.getOrCreateTag());
            applyInAll(p, level, nodeHost, setting);
        }
        return true;
    }

    private void applyInAll(Player p, Level level, IInWorldGridNodeHost nodeHost, PatternModifySetting setting) {
        IGrid grid = null;
        for (Direction direction : Direction.values()) {
            IGridNode node = nodeHost.getGridNode(direction);
            if (node != null) {
                grid = node.getGrid();
            }
        }
        List<Integer> result = PatternUtils.processingPatterns(grid, level, setting);
        if (!result.isEmpty() && result.get(0) > 0) {
            p.displayClientMessage(
                    Component.translatable("tools.mae2a.many_patter_result", result.get(1), result.get(2)),
                    true);
        }
    }
}
