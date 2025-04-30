package com.easterfg.mae2a.client.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.IPatternDetails;
import appeng.api.inventories.InternalInventory;
import appeng.api.stacks.AmountFormat;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.common.StackSizeRenderer;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.Scrollbar;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.menu.AEBaseMenu;
import appeng.menu.slot.AppEngSlot;
import appeng.util.inv.AppEngInternalInventory;

public abstract class AbstractScrollerScreen<T extends AEBaseMenu, S extends Slot> extends AEBaseScreen<T> {

    public static final int VIEW_SIZE = 36;

    protected Scrollbar scrollbar;
    protected NonNullList<Slot> views = NonNullList.create();

    public AbstractScrollerScreen(T menu, Inventory playerInventory, Component title, ScreenStyle style) {
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

    protected boolean handlerSlotClick(Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        return false;
    }

    protected int getInvOffset() {
        return 0;
    }

    @Nullable
    private Slot getMenuSlot(int index) {
        if (index < 0 || index >= views.size()) {
            return null;
        }
        return this.menu.getSlot(getInvOffset() + index + scrollbar.getCurrentScroll() * 9);
    }

    @Override
    public void renderSlot(GuiGraphics guiGraphics, Slot slot) {
        if (validSlot(slot)) {
            var scroll = scrollbar.getCurrentScroll();
            int current = slot.getSlotIndex() - scroll * 9;
            Slot now = getMenuSlot(current);
            if (now == null) {
                return;
            }
            Slot view = views.get(current);
            ItemStack item = now.getItem();
            view.set(item);

            renderNeedCountSlot(guiGraphics, (AppEngSlot) view);
            return;
        }
        super.renderSlot(guiGraphics, slot);
    }

    protected void renderNeedCountSlot(GuiGraphics guiGraphics, AppEngSlot s) {
        var is = s.getItem();

        if ((s.renderIconWithItem() || is.isEmpty()) && s.isSlotEnabled() && s.getIcon() != null) {
            s.getIcon().getBlitter()
                    .dest(s.x, s.y)
                    .opacity(s.getOpacityOfIcon())
                    .blit(guiGraphics);
        }

        if (!s.isValid()) {
            guiGraphics.fill(s.x, s.y, 16 + s.x, 16 + s.y, 0x66ff6666);
        }

        var display = s.getDisplayStack();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

        guiGraphics.renderItem(display, s.x, s.y, s.x + s.y * this.imageWidth);

        if (!is.isEmpty() && is.getItem() instanceof EncodedPatternItem patternItem) {
            IPatternDetails decode = patternItem.decode(is, Minecraft.getInstance().level, false);
            if (decode != null) {
                GenericStack output = decode.getOutputs()[0];
                String amount = output.what().formatAmount(output.amount(), AmountFormat.SLOT);
                StackSizeRenderer.renderSizeLabel(guiGraphics, this.font, s.x, s.y, amount, false);
            }
        }
        guiGraphics.pose().popPose();
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
