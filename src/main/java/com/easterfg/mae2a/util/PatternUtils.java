package com.easterfg.mae2a.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.core.definitions.AEItems;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.helpers.patternprovider.PatternContainer;
import appeng.helpers.patternprovider.PatternProviderLogic;

import com.easterfg.mae2a.common.settings.PatternModifySetting;

/**
 * @author EasterFG on 2024/10/1
 */
public final class PatternUtils {

    private PatternUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
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
    public static @Nullable ItemStack processingPattern(Level level, ItemStack itemStack,
            PatternModifySetting setting) {
        if (!isProcessingPattern(itemStack))
            return null;
        ProcessingPatternItem item = (ProcessingPatternItem) itemStack.getItem();
        AEProcessingPattern patter = item.decode(itemStack, level, false);
        if (patter == null)
            return null;
        GenericStack output = patter.getPrimaryOutput();
        boolean flag = setting.getMode() == PatternModifySetting.ModifyMode.MULTIPLY;
        if (!setting.isLimitMode()) {
            return apply(patter, setting.getRate(), setting.isSaveByProducts(), flag, patter.getPrimaryOutput());
        }
        if (output.what() instanceof AEFluidKey) {
            return processLimitMode(patter, flag ? setting.getMaxFluidLimit() : setting.getMinFluidLimit(),
                    setting.isSaveByProducts(), flag);
        } else if (output.what() instanceof AEItemKey) {
            return processLimitMode(patter, flag ? setting.getMaxItemLimit() : setting.getMinItemLimit(),
                    setting.isSaveByProducts(), flag);
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
        if (grid == null)
            return new ArrayList<>();
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
                if (pattern == null)
                    continue;
                inventory.setItemDirect(slot, pattern);
                count++;
            }
        }
        return count;
    }

    public static List<ItemStack> getProcessingPatterns(Level level, PatternProviderLogic logic,
            PatternModifySetting setting) {
        InternalInventory inventory = logic.getPatternInv();
        List<ItemStack> result = new ArrayList<>(inventory.size());
        for (int slot = 0; slot < inventory.size(); slot++) {
            var stack = inventory.getStackInSlot(slot);
            if (stack != null) {
                if (stack.isEmpty()) {
                    result.add(ItemStack.EMPTY);
                    continue;
                }
                var pattern = PatternUtils.processingPattern(level, stack, setting);
                if (pattern != null) {
                    result.add(pattern);
                } else {
                    result.add(AEItems.BLANK_PATTERN.stack());
                }
                continue;
            }
            result.add(ItemStack.EMPTY);
        }
        return result;
    }

    /**
     * 应用修改后的处理样板
     *
     * @param pattern       解码后的样板
     * @param limit         数量限制
     * @param hasByProducts 是否保留副产物
     * @param multiplyMode  是否为乘法模式（true=倍增，false=倍减）
     * @return 处理后的新样板，或null（当无法调整时）
     */
    public static @Nullable ItemStack processLimitMode(AEProcessingPattern pattern, long limit, boolean hasByProducts,
            boolean multiplyMode) {
        var primary = pattern.getPrimaryOutput();
        if (!isScalingApplicable(primary.amount(), limit, multiplyMode)) {
            return null;
        }

        int times = calculateScalingFactor(primary.amount(), limit, multiplyMode);
        return apply(pattern, times, hasByProducts, multiplyMode, primary);
    }

    @Nullable
    private static ItemStack apply(AEProcessingPattern pattern, int times, boolean hasByProducts, boolean multiplyMode,
            GenericStack primary) {
        if (times <= 1)
            return null;
        GenericStack[] outputs = pattern.getSparseOutputs();
        GenericStack[] inputs = pattern.getSparseInputs();
        GenericStack[] newInputs = scaleStacks(inputs, times, multiplyMode);
        GenericStack[] newOutput = scaleOutputs(outputs, primary, times, multiplyMode, hasByProducts);
        if (newInputs == null || newOutput == null) {
            return null;
        }
        return PatternDetailsHelper.encodeProcessingPattern(newInputs, newOutput);
    }

    public static GenericStack[] scaleStacks(GenericStack[] stacks, int times, boolean multiplyMode) {
        GenericStack[] scaled = new GenericStack[stacks.length];
        int count = 0, finalCount = 0;
        for (GenericStack stack : stacks) {
            if (stack == null) {
                continue;
            }
            finalCount++;
            long newAmount;
            if (multiplyMode) {
                newAmount = stack.amount() * times;
            } else {
                if (stack.amount() % times != 0) {
                    continue;
                }
                newAmount = stack.amount() / times;
            }
            scaled[count++] = new GenericStack(stack.what(), newAmount);
        }
        if (count != finalCount) {
            return null;
        }
        return scaled;
    }

    private static GenericStack[] scaleOutputs(GenericStack[] outputs, GenericStack primary,
            int times, boolean multiplyMode, boolean hasByProducts) {
        GenericStack[] scaled = new GenericStack[outputs.length];

        // primary
        long newAmount = multiplyMode
                ? primary.amount() * times
                : primary.amount() / times;
        if (newAmount <= 0) return null;
        scaled[0] = new GenericStack(primary.what(), newAmount);

        // other
        if (hasByProducts && outputs.length > 1) {
            System.arraycopy(outputs, 1, scaled, 1, outputs.length - 1);
        }

        return scaled;
    }

    private static int calculateScalingFactor(long currentAmount, long limit, boolean multiplyMode) {
        return (int) (multiplyMode
                ? Math.floor(limit / (double) currentAmount)
                : Math.floor((double) currentAmount / limit));
    }

    private static boolean isScalingApplicable(long currentAmount, long limit, boolean multiplyMode) {
        return multiplyMode ? currentAmount < limit : currentAmount > limit;
    }

    public static CompoundTag writeVec3(Vec3 vec3) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x", vec3.x);
        tag.putDouble("y", vec3.y);
        tag.putDouble("z", vec3.z);
        return tag;
    }

    public static Vec3 readVec3(CompoundTag tag) {
        var x = tag.getDouble("x");
        var y = tag.getDouble("y");
        var z = tag.getDouble("z");
        return new Vec3(x, y, z);
    }

}
