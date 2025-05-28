package com.easterfg.mae2a.client.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import appeng.api.inventories.InternalInventory;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.Scrollbar;
import appeng.menu.slot.AppEngSlot;
import appeng.util.inv.AppEngInternalInventory;

import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;
import com.easterfg.mae2a.util.GuiUtil;

public abstract class AbstractPatternPlusScrollerScreen<T extends PatternProviderPlusMenu, S extends Slot>
        extends PatternProviderScreen<T> {
    public static final int VIEW_SIZE = 36;

    protected final Scrollbar scrollbar;
    protected final NonNullList<Slot> views = NonNullList.create();

    public AbstractPatternPlusScrollerScreen(T menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.scrollbar = this.widgets.addScrollBar("scroll_bar");

        InternalInventory inventory = new AppEngInternalInventory(VIEW_SIZE);
        for (int i = 0; i < VIEW_SIZE; i++) {
            S slot = createViewSlot(inventory, i);
            views.add(i, slot);
        }
    }

    protected abstract S createViewSlot(InternalInventory inventory, int index);

    protected abstract boolean validSlot(Slot slot);

    @NotNull
    public final Slot getMenuSlot(int index) {
        return this.menu.getSlot(36 + index + scrollbar.getCurrentScroll() * 9);
    }

    @Override
    public void renderSlot(GuiGraphics guiGraphics, Slot slot) {
        if (validSlot(slot)) {
            var scroll = scrollbar.getCurrentScroll();
            int current = slot.getSlotIndex() - scroll * 9;
            if (current < 0 || current >= VIEW_SIZE) {
                return;
            }
            renderValidSlot(guiGraphics, current);
            return;
        }
        super.renderSlot(guiGraphics, slot);
    }

    protected void renderValidSlot(GuiGraphics guiGraphics, int current) {
        Slot now = getMenuSlot(current);
        Slot view = views.get(current);
        ItemStack item = now.getItem();
        view.set(item);

        GuiUtil.renderPatternSlot(guiGraphics, (AppEngSlot) view, this.imageWidth);
    }

    @Override
    protected boolean isHovering(Slot slot, double x, double y) {
        var result = super.isHovering(slot, x, y);
        if (result && slot.getSlotIndex() >= 36) {
            return false;
        }
        return result;
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int pX, int pY) {
        if (hoveredSlot != null) {
            if (validSlot(hoveredSlot)) {
                Slot view = views.get(hoveredSlot.getSlotIndex());
                if (view.hasItem()) {
                    ItemStack itemstack = view.getItem();
                    guiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack),
                            itemstack.getTooltipImage(), itemstack, pX, pY);
                }
                return;
            }
        }
        super.renderTooltip(guiGraphics, pX, pY);
    }

    @Override
    protected void slotClicked(@Nullable Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        if (validSlot(slot)) {
            var index = slotIdx + scrollbar.getCurrentScroll() * 9;
            Slot now = menu.getSlot(index);
            super.slotClicked(now, index, mouseButton, clickType);
        } else {
            super.slotClicked(slot, slotIdx, mouseButton, clickType);
        }
    }
}
