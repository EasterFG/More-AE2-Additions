package com.easterfg.mae2a.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.style.ScreenStyle;
import appeng.menu.slot.RestrictedInputSlot;

import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;

public abstract class AbstractPatternPlusScrollerScreen
        extends AbstractScrollerScreen<PatternProviderPlusMenu, RestrictedInputSlot> {
    public AbstractPatternPlusScrollerScreen(PatternProviderPlusMenu menu, Inventory playerInventory, Component title,
            ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Override
    protected int getInvOffset() {
        return 36;
    }
}
