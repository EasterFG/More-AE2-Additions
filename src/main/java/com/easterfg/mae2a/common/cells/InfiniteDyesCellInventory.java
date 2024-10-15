package com.easterfg.mae2a.common.cells;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * @author EasterFG on 2024/10/12
 */
public class InfiniteDyesCellInventory implements StorageCell {
    private static final Set<AEKey> configured = new HashSet<>();
    private final ItemStack stack;

    static {
        for (DyeColor color : DyeColor.values()) {
            configured.add(AEItemKey.of(DyeItem.byColor(color)));
        }
    }

    public InfiniteDyesCellInventory(ItemStack is) {
        this.stack = is;
    }

    static Set<AEKey> getConfigured() {
        return configured;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        return configured.contains(what) ? amount : 0;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        return configured.contains(what) ? amount : 0;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        for (AEKey key : configured) {
            out.add(key, Integer.MAX_VALUE);
        }
    }

    @Override
    public boolean isPreferredStorageFor(AEKey input, IActionSource source) {
        return configured.contains(input);
    }

    @Override
    public CellState getStatus() {
        return CellState.TYPES_FULL;
    }

    @Override
    public double getIdleDrain() {
        return 0;
    }

    @Override
    public void persist() {
    }

    @Override
    public Component getDescription() {
        return stack.getHoverName();
    }
}
