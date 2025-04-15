package com.easterfg.mae2a.common.cells;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.GenericStack;
import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import appeng.items.storage.StorageCellTooltipComponent;

import com.easterfg.mae2a.common.items.InfiniteDyesCellItem;

/**
 * @author EasterFG on 2024/10/12
 */
public class InfiniteDyesCellHandler implements ICellHandler {

    public static final InfiniteDyesCellHandler INSTANCE = new InfiniteDyesCellHandler();
    public final TooltipComponent DEFAULT_IMAGE_TOOLTIP;

    private InfiniteDyesCellHandler() {
        List<GenericStack> content = new ArrayList<>();
        InfiniteDyesCellInventory.getConfigured().forEach(key -> content.add(new GenericStack(key, 1)));
        DEFAULT_IMAGE_TOOLTIP = new StorageCellTooltipComponent(
                List.of(),
                content.subList(0, 6),
                true,
                false);
    }

    @Override
    public boolean isCell(ItemStack is) {
        return !is.isEmpty() && is.getItem() instanceof InfiniteDyesCellItem;
    }

    @Override
    public @Nullable StorageCell getCellInventory(ItemStack is, @Nullable ISaveProvider container) {
        if (!is.isEmpty() && is.getItem() instanceof InfiniteDyesCellItem) {
            return new InfiniteDyesCellInventory(is);
        }
        return null;
    }
}
