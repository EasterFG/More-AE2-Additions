package com.easterfg.mae2a.common.items;

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
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.parts.encoding.PatternEncodingTerminalPart;
import appeng.util.Platform;
import com.easterfg.mae2a.common.menu.PatternModifyMenu;
import com.easterfg.mae2a.common.menu.host.PatternModifyHost;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import com.easterfg.mae2a.util.PatternUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author EasterFG on 2024/9/30
 * <p>
 * 批量翻倍网络中全部样板的工具
 */
public class PatternModifyToolItem extends Item implements IMenuItem {

    public PatternModifyToolItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.mae2a.pattern_modify_tool.tooltip.1"));
        tooltip.add(Component.translatable("item.mae2a.pattern_modify_tool.tooltip.2"));
        tooltip.add(Component.translatable("item.mae2a.pattern_modify_tool.tooltip.3"));
        tooltip.add(Component.translatable("item.mae2a.pattern_modify_tool.tooltip.4"));
        tooltip.add(Component.translatable("item.mae2a.pattern_modify_tool.tooltip.5"));
    }

    @Override
    public @Nullable ItemMenuHost getMenuHost(Player player, int inventorySlot, ItemStack stack, @Nullable BlockPos pos) {
        return new PatternModifyHost(player, inventorySlot, stack);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide()) {
            MenuOpener.open(PatternModifyMenu.TYPE, player, MenuLocators.forHand(player, hand));
        }
        return new InteractionResultHolder<>(InteractionResult.sidedSuccess(level.isClientSide()),
                player.getItemInHand(hand));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;

        Level level = context.getLevel();

        if (!level.isClientSide()) {
            if (!showToolGui(context)) {
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    public boolean showToolGui(@NotNull UseOnContext useContext) {
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
            if (p.isShiftKeyDown()) {
                PatternModifySetting setting = new PatternModifySetting();
                setting.readFromNBT(p.getItemInHand(useContext.getHand()).getOrCreateTag());
                level.getBlockState(pos).liquid()
                BlockEntity te = level.getBlockEntity(pos);
                PatternContainer container;
                boolean network = false;
                if (te instanceof CableBusBlockEntity cable) {
                    Vec3 hit = useContext.getClickLocation();
                    Vec3 target = new Vec3(hit.x - pos.getX(), hit.y - pos.getY(), hit.z - pos.getZ());
                    IPart part = cable.getCableBus().selectPartLocal(target).part;
                    container = part instanceof PatternContainer ? (PatternContainer) part : null;
                    network = part instanceof PatternEncodingTerminalPart;
                } else {
                    container = te instanceof PatternContainer ? (PatternContainer) te : null;
                }

                if (container != null) {
                    int count = PatternUtils.processingPattern(level, setting, container);
                    if (count > 0) {
                        p.displayClientMessage(Component.translatable("tools.mae2a.one_patter_result", count), true);
                    }
                } else if (network) {
                    applyInAll(p, level, nodeHost, setting);
                }
            }
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
            p.displayClientMessage(Component.translatable("tools.mae2a.many_patter_result", result.get(1), result.get(2)),
                    true);
        }
    }
}
