package com.easterfg.mae2a.util;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.helpers.patternprovider.PatternContainer;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author EasterFG on 2024/10/1
 */
public class PatternUtils {

    private PatternUtils() {
        throw new IllegalAccessError();
    }

    /**
     * Check if the item is a process pattern.
     *
     * @param itemStack The item to check.
     * @return True if the item is a process pattern, false otherwise.
     */
    public static boolean isProcessingPattern(ItemStack itemStack) {
        return itemStack.getItem() instanceof ProcessingPatternItem;
    }

    /**
     * 处理样板
     *
     * @param level     维度
     * @param itemStack 要处理的样板
     * @param setting   设置
     * @return 处理后的样板, null表示无需处理
     */
    public static @Nullable ItemStack processingPattern(Level level, ItemStack itemStack, PatternModifySetting setting) {
        if (!isProcessingPattern(itemStack)) return null;
        ProcessingPatternItem item = (ProcessingPatternItem) itemStack.getItem();
        AEProcessingPattern patter = item.decode(itemStack, level, false);
        if (patter == null) return null;
        GenericStack output = patter.getPrimaryOutput();
        boolean flag = setting.getMode() == 0;
        if (output.what() instanceof AEFluidKey) {
            return apply(patter, flag ? setting.getMaxFluidLimit() : setting.getMinFluidLimit(), setting.isSaveByProducts(), flag);
        } else if (output.what() instanceof AEItemKey) {
            return apply(patter, flag ? setting.getMaxItemLimit() : setting.getMinItemLimit(), setting.isSaveByProducts(), flag);
        }
        return itemStack;
    }

    /**
     * 处理给定网络中的全部样板
     *
     * @param grid    网络节点
     * @param level   维度
     * @param setting 设置
     * @return 处理结果
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> processingPatterns(IGrid grid, Level level, PatternModifySetting setting) {
        if (grid == null) return new ArrayList<>();
        int machineCount = 0;
        int patternCount = 0;
        for (var machine : grid.getMachineClasses()) {
            if (!(PatternContainer.class.isAssignableFrom(machine))) {
                continue;
            }
            Set<? extends PatternContainer> activeMachines = grid
                    .getActiveMachines((Class<PatternContainer>) machine);
            for (PatternContainer container : activeMachines) {
                int count = processingPattern(level, setting, container);
                if (count > 0) {
                    machineCount++;
                    patternCount += count;
                }
            }
        }
        return List.of((machineCount + patternCount), machineCount, patternCount);
    }

    /**
     * 处理给定容器中的全部样板
     *
     * @param level     维度
     * @param setting   设置
     * @param container 容器
     * @return 处理结果
     */
    public static int processingPattern(Level level, PatternModifySetting setting, PatternContainer container) {
        int count = 0;
        InternalInventory inventory = container.getTerminalPatternInventory();
        for (int slot = 0; slot < inventory.size(); ++slot) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null) {
                ItemStack pattern = PatternUtils.processingPattern(level, stack, setting);
                if (pattern == null) continue;
                inventory.setItemDirect(slot, pattern);
                count++;
            }
        }
        return count;
    }

    /**
     * 应用修改
     *
     * @param pattern       解码后的样板
     * @param limit         限制
     * @param hasByProducts 保留副产物
     * @param flag          乘除标记
     * @return 处理结果
     */
    public static @Nullable ItemStack apply(AEProcessingPattern pattern, long limit, boolean hasByProducts, boolean flag) {
        var primary = pattern.getPrimaryOutput();
        if (primary.amount() >= limit && flag) return null;
        if (primary.amount() <= limit && !flag) return null;
        GenericStack[] outputs = pattern.getSparseOutputs();
        GenericStack[] inputs = pattern.getSparseInputs();
        var newOutput = new GenericStack[outputs.length];
        var newInputs = new GenericStack[inputs.length];

        if (hasByProducts) {
            System.arraycopy(outputs, 1, newOutput, 1, outputs.length - 1);
        }
        int times;
        if (flag) {
            times = (int) Math.floor(limit / (double) primary.amount());
            if (times <= 1) return null;
            newInputs = Arrays.stream(inputs)
                    .filter(Objects::nonNull)
                    .map(input -> new GenericStack(input.what(), input.amount() * times))
                    .toArray(GenericStack[]::new);
            newOutput[0] = new GenericStack(primary.what(), primary.amount() * times);
        } else {
            times = (int) Math.floor((double) primary.amount() / limit);
            if (times <= 1) return null;
            newInputs = Arrays.stream(inputs)
                    .filter(Objects::nonNull)
                    .map(input -> {
                        if (input.amount() % times != 0) {
                            return null;
                        }
                        return new GenericStack(input.what(), input.amount() / times);
                    })
                    .filter(Objects::nonNull)
                    .toArray(GenericStack[]::new);
            newOutput[0] = new GenericStack(primary.what(), primary.amount() / times);
            if (newInputs.length != pattern.getInputs().length) return null;
        }
        return PatternDetailsHelper.encodeProcessingPattern(newInputs, newOutput);
    }
}
