package com.easterfg.mae2a.client.screen;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.core.AppEng;
import appeng.core.definitions.AEItems;

import com.easterfg.mae2a.common.menu.PatternPreviewListMenu;
import com.easterfg.mae2a.config.MAE2AConfig;

/**
 * @author EasterFG on 2025/4/6
 */
public class PatternPreviewListScreen extends AEBaseScreen<PatternPreviewListMenu> {

    private int rows;

    private final Button confirm;
    private final Button cancel;

    private static final int GUI_HEADER_HEIGHT = 54;
    private static final int GUI_FOOTER_OFFSET = 72;
    private static final int GUI_FOOTER_HEIGHT = 32;
    private static final int GUI_ROW_HEIGHT = 18;
    private static final int GUI_WIDTH = 176;

    public PatternPreviewListScreen(PatternPreviewListMenu menu, Inventory playerInventory, Component title,
            ScreenStyle style) {
        super(menu, playerInventory, title, style);
        for (int i = 0; i < 4; i++) {
            final int times = MAE2AConfig.buttonTimes[i];
            var mb = widgets.addButton("multiply_" + i, Component.literal("x" + times), () -> menu.multiply(times));
            var db = widgets.addButton("divide_" + i, Component.literal("÷" + times), () -> menu.divide(times));
            mb.setTooltip(Tooltip.create(Component.translatable("gui.mae2a.preview_config_tooltip", times)));
            db.setTooltip(Tooltip.create(Component.translatable("gui.mae2a.preview_config_tooltip", times)));
        }

        this.confirm = widgets.addButton("confirm", Component.translatable("gui.mae2a.confirm"), () -> {
            menu.confirm();
            onClose();
        });
        this.cancel = widgets.addButton("cancel", Component.translatable("gui.mae2a.cancel"), this::onClose);
    }

    @Override
    protected void init() {
        super.init();
        List<ItemStack> patterns = this.menu.getPatterns();
        if (patterns != null) {
            int size = patterns.size();
            rows = size / 9;
            resize();
        }
    }

    protected void resize() {
        confirm.setY(confirm.getY() + rows * GUI_ROW_HEIGHT);
        cancel.setY(cancel.getY() + rows * GUI_ROW_HEIGHT);
    }

    @Override
    protected void slotClicked(@Nullable Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        if (getMenu().isClientSideSlot(slot) || slot == null) {
            return;
        }

        var stack = slot.getItem();
        if (stack.isEmpty() || stack.is(AEItems.BLANK_PATTERN.asItem()))
            return;
        menu.switchEnable(slotIdx);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
        if (hoveredSlot != null) {
            ItemStack item = hoveredSlot.getItem();
            if (item.is(Items.BARRIER)) {
                guiGraphics.renderTooltip(font, List.of(
                        Component.translatable("gui.mae2a.pattern_list.disable"),
                        Component.translatable("gui.mae2a.pattern_list.info"),
                        Component.translatable("gui.mae2a.pattern_list.enable_helper")), Optional.empty(), x, y);
                return;
            } else if (item.is(AEItems.BLANK_PATTERN.asItem())) {
                guiGraphics.renderTooltip(font, List.of(
                        Component.translatable("gui.mae2a.pattern_list.cant"),
                        Component.translatable("gui.mae2a.pattern_list.cant_info")), Optional.empty(), x, y);
                return;
            }
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected @NotNull List<Component> getTooltipFromContainerItem(@NotNull ItemStack stack) {
        var tooltip = super.getTooltipFromContainerItem(stack);
        tooltip.add(Component.translatable("gui.mae2a.pattern_list.disable_helper"));
        return tooltip;
    }

    @Override
    public void drawBG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY, float partialTicks) {
        blit(guiGraphics, offsetX, offsetY, new Rect2i(0, 0, GUI_WIDTH, GUI_HEADER_HEIGHT));

        int currentY = GUI_HEADER_HEIGHT + offsetY;

        blit(guiGraphics, offsetX, currentY + rows * GUI_ROW_HEIGHT,
                new Rect2i(0, GUI_FOOTER_OFFSET, GUI_WIDTH, GUI_FOOTER_HEIGHT));

        for (int i = 0; i < rows; ++i) {
            Rect2i box = new Rect2i(0, GUI_HEADER_HEIGHT, GUI_WIDTH, GUI_ROW_HEIGHT);
            blit(guiGraphics, offsetX, currentY, box);
            currentY += 18;
        }

    }

    private void blit(GuiGraphics guiGraphics, int offsetX, int offsetY, Rect2i srcRect) {
        var texture = AppEng.makeId("textures/guis/pattern_list_preview.png");
        guiGraphics.blit(texture, offsetX, offsetY, srcRect.getX(), srcRect.getY(), srcRect.getWidth(),
                srcRect.getHeight());
    }

}
