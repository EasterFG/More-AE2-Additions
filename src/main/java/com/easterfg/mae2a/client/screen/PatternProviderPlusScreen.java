package com.easterfg.mae2a.client.screen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.api.upgrades.Upgrades;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.UpgradesPanel;
import appeng.core.localization.GuiText;
import appeng.menu.SlotSemantics;

import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;

/**
 * @author EasterFG on 2025/4/3
 */
public class PatternProviderPlusScreen extends PatternProviderScreen<PatternProviderPlusMenu> {

    public PatternProviderPlusScreen(PatternProviderPlusMenu menu, Inventory playerInventory, Component title,
            ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.widgets.add("upgrades", new UpgradesPanel(
                menu.getSlots(SlotSemantics.UPGRADE),
                this::getCompatibleUpgrades));
    }

    private List<Component> getCompatibleUpgrades() {
        var list = new ArrayList<Component>();
        list.add(GuiText.CompatibleUpgrades.text());
        list.addAll(Upgrades.getTooltipLinesForMachine(menu.getUpgrades().getUpgradableItem()));
        return list;
    }
}
