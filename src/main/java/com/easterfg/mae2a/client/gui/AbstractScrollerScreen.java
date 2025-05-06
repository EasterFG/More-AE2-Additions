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
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.Scrollbar;
import appeng.util.inv.AppEngInternalInventory;

import com.easterfg.mae2a.api.slot.PreviewSlot;
import com.easterfg.mae2a.common.menu.PatternPreviewListMenu;

public abstract class AbstractScrollerScreen<S extends Slot> extends AEBaseScreen<PatternPreviewListMenu> {

    public static final int VIEW_SIZE = 36;

    protected final Scrollbar scrollbar;
    protected final NonNullList<PreviewSlot> views = NonNullList.create();

    public AbstractScrollerScreen(PatternPreviewListMenu menu, Inventory playerInventory, Component title,
            ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.scrollbar = this.widgets.addScrollBar("scroll_bar");

        InternalInventory inventory = new AppEngInternalInventory(36);
        for (int i = 0; i < 36; i++) {
            S slot = createViewSlot(inventory, i);
            views.add(i, (PreviewSlot) slot);
        }
    }

    protected abstract S createViewSlot(InternalInventory inventory, int index);

    protected abstract boolean validSlot(Slot slot);

    protected boolean handlerSlotClick(Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        return false;
    }

    @NotNull
    public final PreviewSlot getMenuSlot(int index) {
        return (PreviewSlot) this.menu.getSlot(index + scrollbar.getCurrentScroll() * 9);
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
            Slot view = views.get(hoveredSlot.getSlotIndex());
            if (view.hasItem()) {
                ItemStack itemstack = view.getItem();
                guiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack),
                        itemstack.getTooltipImage(), itemstack, pX, pY);
            }
        }
    }

    @Override
    protected void slotClicked(@Nullable Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        if (validSlot(slot)) {
            var index = slotIdx + scrollbar.getCurrentScroll() * 9;
            Slot now = menu.getSlot(index);
            if (handlerSlotClick(now, index, mouseButton, clickType)) {
                return;
            }
            super.slotClicked(now, index, mouseButton, clickType);
        } else {
            super.slotClicked(slot, slotIdx, mouseButton, clickType);
        }
    }
}
