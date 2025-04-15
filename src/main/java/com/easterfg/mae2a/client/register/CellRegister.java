package com.easterfg.mae2a.client.register;

import appeng.api.storage.StorageCells;

import com.easterfg.mae2a.common.cells.InfiniteDyesCellHandler;

/**
 * @author EasterFG on 2024/10/12
 */
public class CellRegister {
    public static void register() {
        StorageCells.addCellHandler(InfiniteDyesCellHandler.INSTANCE);
    }
}
