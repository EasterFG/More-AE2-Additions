package com.easterfg.mae2a.client.screen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.inventories.InternalInventory;
import appeng.api.upgrades.Upgrades;
import appeng.client.gui.Icon;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.UpgradesPanel;
import appeng.core.localization.GuiText;
import appeng.menu.SlotSemantics;
import appeng.menu.slot.RestrictedInputSlot;

import com.easterfg.mae2a.client.gui.AbstractPatternPlusScrollerScreen;
import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;
import com.easterfg.mae2a.util.Platform;

/**
 * @author EasterFG on 2025/4/3
 */
@OnlyIn(Dist.CLIENT)
public class PatternProviderPlusScreen
        extends AbstractPatternPlusScrollerScreen<PatternProviderPlusMenu, RestrictedInputSlot> {

    public PatternProviderPlusScreen(PatternProviderPlusMenu menu, Inventory playerInventory, Component title,
            ScreenStyle style) {
        super(menu, playerInventory, title, style);
        if (!Platform.isModLoaded("appflux")) {
            this.widgets.add("upgrades", new UpgradesPanel(
                    menu.getSlots(SlotSemantics.UPGRADE),
                    this::getCompatibleUpgrades));
        }

        scrollbar.setHeight(70);
        scrollbar.setRange(0, getScrollableRows(), 1);
    }

    public int getScrollableRows() {
        int size = menu.getSlots(SlotSemantics.ENCODED_PATTERN).size();
        return size / 9 - 4;
    }

    private List<Component> getCompatibleUpgrades() {
        var list = new ArrayList<Component>();
        list.add(GuiText.CompatibleUpgrades.text());
        list.addAll(Upgrades.getTooltipLinesForMachine(menu.getUpgrades().getUpgradableItem()));
        return list;
    }

    @Override
    protected RestrictedInputSlot createViewSlot(InternalInventory inventory, int index) {
        RestrictedInputSlot slot = new RestrictedInputSlot(RestrictedInputSlot.PlacableItemType.ENCODED_PATTERN,
                inventory, index);
        slot.x = 8 + (18 * (index % 9));
        slot.y = 42 + (18 * (index / 9));
        return slot;
    }

    @Override
    protected boolean validSlot(Slot slot) {
        return slot instanceof RestrictedInputSlot appEngSlots
                && appEngSlots.getIcon() == Icon.BACKGROUND_ENCODED_PATTERN;
    }
}
