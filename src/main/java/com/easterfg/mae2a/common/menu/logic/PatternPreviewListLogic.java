package com.easterfg.mae2a.common.menu.logic;

import appeng.api.inventories.InternalInventory;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.InternalInventoryHost;

import lombok.Getter;

/**
 * @author EasterFG on 2025/4/6
 */
@Getter
public class PatternPreviewListLogic implements InternalInventoryHost {

    protected final AppEngInternalInventory patternInventory;

    public PatternPreviewListLogic(int patternInventorySize) {
        this.patternInventory = new AppEngInternalInventory(this, patternInventorySize);

    }

    @Override
    public void saveChanges() {

    }

    @Override
    public void onChangeInventory(InternalInventory inv, int slot) {

    }

    @Override
    public boolean isClientSide() {
        return false;
    }
}
