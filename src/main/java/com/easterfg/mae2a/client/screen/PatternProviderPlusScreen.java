package com.easterfg.mae2a.client.screen;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import appeng.api.upgrades.Upgrades;
import appeng.client.gui.Icon;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.Scrollbar;
import appeng.client.gui.widgets.UpgradesPanel;
import appeng.core.localization.GuiText;
import appeng.menu.SlotSemantics;
import appeng.menu.slot.RestrictedInputSlot;
import appeng.util.inv.AppEngInternalInventory;

import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;

/**
 * @author EasterFG on 2025/4/3
 */
public class PatternProviderPlusScreen extends PatternProviderScreen<PatternProviderPlusMenu> {

    private final Scrollbar scrollbar;

    private final NonNullList<Slot> patternSlot = NonNullList.create();

    public PatternProviderPlusScreen(PatternProviderPlusMenu menu, Inventory playerInventory, Component title,
            ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.widgets.add("upgrades", new UpgradesPanel(
                menu.getSlots(SlotSemantics.UPGRADE),
                this::getCompatibleUpgrades));

        AppEngInternalInventory inventory = new AppEngInternalInventory(36);
        for (int i = 0; i < 36; i++) {
            RestrictedInputSlot slot = new RestrictedInputSlot(RestrictedInputSlot.PlacableItemType.ENCODED_PATTERN,
                    inventory, i);
            slot.x = 8 + (18 * (i % 9));
            slot.y = 42 + (18 * (i / 9));
            patternSlot.add(i, slot);
        }

        scrollbar = this.widgets.addScrollBar("scroll_bar");
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
    public void renderSlot(GuiGraphics guiGraphics, Slot s) {
        if (s instanceof RestrictedInputSlot appEngSlots && appEngSlots.getIcon() == Icon.BACKGROUND_ENCODED_PATTERN) {
            var scroll = scrollbar.getCurrentScroll();
            int current = appEngSlots.getSlotIndex() - scroll * 9;
            if (current < 0 || current >= patternSlot.size()) {
                return;
            }
            Slot slot = patternSlot.get(current);
            Slot realSlot = this.menu.getSlot(36 + current + scroll * 9);
            slot.set(realSlot.getItem());
            super.renderSlot(guiGraphics, slot);
            return;
        }
        super.renderSlot(guiGraphics, s);
    }

    @Override
    protected boolean isHovering(Slot slot, double x, double y) {
        if (slot.getSlotIndex() >= 36 && y > 96)
            return false;
        return super.isHovering(slot, x, y);
    }

    @Override
    protected void slotClicked(@Nullable Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        if (slot instanceof RestrictedInputSlot appEngSlots
                && appEngSlots.getIcon() == Icon.BACKGROUND_ENCODED_PATTERN) {
            var index = slotIdx + scrollbar.getCurrentScroll() * 9;
            Slot realMenu = menu.getSlot(index);
            super.slotClicked(realMenu, index, mouseButton, clickType);
            return;
        }
        super.slotClicked(slot, slotIdx, mouseButton, clickType);
    }
}
