package com.easterfg.mae2a.client.screen;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.inventories.InternalInventory;
import appeng.api.stacks.AmountFormat;
import appeng.client.gui.me.common.StackSizeRenderer;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.style.ScreenStyle;
import appeng.core.AppEng;
import appeng.core.definitions.AEItems;
import appeng.crafting.pattern.EncodedPatternItem;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.api.slot.PreviewSlot;
import com.easterfg.mae2a.client.gui.AbstractScrollerScreen;
import com.easterfg.mae2a.client.gui.widget.CustomIconButton;
import com.easterfg.mae2a.common.menu.PatternPreviewListMenu;
import com.easterfg.mae2a.config.MAE2AConfig;
import com.easterfg.mae2a.util.GuiUtil;

/**
 * @author EasterFG on 2025/4/6
 */
@OnlyIn(Dist.CLIENT)
public class PatternPreviewListScreen extends AbstractScrollerScreen<PreviewSlot> {

    private int rows;
    private boolean mode = true;

    private final Button confirm;
    private final Button cancel;

    private static final int GUI_HEADER_HEIGHT = 54;
    private static final int GUI_FOOTER_OFFSET = 72;
    private static final int GUI_FOOTER_HEIGHT = 32;
    private static final int GUI_ROW_HEIGHT = 18;
    private static final int GUI_WIDTH = 176;

    private static final int GUI_SCROLLBAR_OFFSET = 173;
    private static final int GUI_SCROLLBAR_WIDTH = 21;
    private static final int GUI_SCROLLBAR_HEIGHT = 133;

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

        Blitter texture = Blitter.texture(MoreAE2Additions.id("textures/guis/selection_mode.png"), 32, 16);
        var select = CustomIconButton.Builder.builder(__ -> {
            mode = !mode;
            menu.switchSelect();
        })
                .texture(texture.copy().src(0, 0, 16, 16))
                .disableTexture(texture.copy().src(16, 0, 16, 16))
                .status(() -> mode)
                .tooltip(Component.translatable("gui.mae2a.pattern_list.normal_mode"),
                        Component.translatable("gui.mae2a.pattern_list.invert_mode"))
                .message(Component.translatable("gui.mae2a.pattern_list.select_mode"))
                .build();
        this.addToLeftToolbar(select);

        scrollbar.setHeight(70);
        scrollbar.setVisible(false);
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
        int min = Math.min(rows, 4);
        confirm.setY(confirm.getY() + min * GUI_ROW_HEIGHT);
        cancel.setY(cancel.getY() + min * GUI_ROW_HEIGHT);
        scrollbar.setRange(0, (rows - 4), 1);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
        if (hoveredSlot != null) {
            if (hoveredSlot instanceof PreviewSlot slot) {
                ItemStack item = hoveredSlot.getItem();
                if (item.is(AEItems.BLANK_PATTERN.asItem())) {
                    guiGraphics.renderTooltip(font, List.of(
                            Component.translatable("gui.mae2a.pattern_list.cant"),
                            Component.translatable("gui.mae2a.pattern_list.cant_info")), Optional.empty(), x, y);
                    return;
                }

                if (!item.isEmpty() && mode != slot.isEnable()) {
                    ItemStack displayStack = slot.getDisplayStack();
                    guiGraphics.renderTooltip(font, List.of(
                            displayStack.getHoverName(),
                            Component.translatable("gui.mae2a.pattern_list.disable"),
                            Component.translatable("gui.mae2a.pattern_list.info"),
                            Component.translatable("gui.mae2a.pattern_list.enable_helper")), Optional.empty(), x, y);
                    return;
                }
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
        var texture = AppEng.makeId("textures/guis/pattern_list_preview.png");
        guiGraphics.blit(texture, offsetX, offsetY, 0, 0, GUI_WIDTH,
                GUI_HEADER_HEIGHT);

        int currentY = GUI_HEADER_HEIGHT + offsetY;

        guiGraphics.blit(texture, offsetX, currentY + 4 * GUI_ROW_HEIGHT,
                0, GUI_FOOTER_OFFSET, GUI_WIDTH, GUI_FOOTER_HEIGHT);

        int count = Math.min(4, rows);
        for (int i = 0; i < count; ++i) {
            guiGraphics.blit(texture, offsetX, currentY, 0, GUI_HEADER_HEIGHT, GUI_WIDTH,
                    GUI_ROW_HEIGHT);
            currentY += 18;
        }

        if (rows > 4) {
            drawScrollbar(guiGraphics, offsetX, offsetY);
        }
    }

    private void drawScrollbar(GuiGraphics guiGraphics, int offsetX, int offsetY) {
        var texture = AppEng.makeId("textures/guis/pattern_list_preview.png");
        scrollbar.setVisible(true);
        guiGraphics.blit(texture, offsetX + GUI_SCROLLBAR_OFFSET, offsetY, 0, 105, GUI_SCROLLBAR_WIDTH,
                GUI_SCROLLBAR_HEIGHT);
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
        PreviewSlot now = getMenuSlot(current);
        PreviewSlot view = views.get(current);
        ItemStack item = now.getItem();
        view.set(item);
        view.setStatus(now.getStatus());

        renderCountSlot(guiGraphics, view);
    }

    protected void renderCountSlot(GuiGraphics guiGraphics, PreviewSlot s) {
        var is = s.getItem();

        if ((s.renderIconWithItem() || is.isEmpty()) && s.isSlotEnabled() && s.getIcon() != null) {
            s.getIcon().getBlitter()
                    .dest(s.x, s.y)
                    .opacity(s.getOpacityOfIcon())
                    .blit(guiGraphics);
        }

        ItemStack display;

        if (s.isEnable()) {
            display = s.getDisplayStack();
        } else {
            if (mode) {
                display = Items.BARRIER.getDefaultInstance();
            } else {
                display = s.getDisplayStack();
            }
        }

        if (!is.isEmpty()) {
            int color = s.isEnable() ? 0x88ff6666 : 0x8866ff66;
            if (!mode) {
                guiGraphics.fill(s.x, s.y, 16 + s.x, 16 + s.y, color);
            }
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

        guiGraphics.renderItem(display, s.x, s.y, s.x + s.y * this.imageWidth);

        if (mode == s.isEnable() && is.getItem() instanceof EncodedPatternItem) {
            var output = GuiUtil.getShowAmount(is);
            if (output != null) {
                String amount = output.what().formatAmount(output.amount(), AmountFormat.SLOT);
                StackSizeRenderer.renderSizeLabel(guiGraphics, this.font, s.x, s.y, amount, false);
            }
        }

        guiGraphics.pose().popPose();
    }

    @Override
    protected PreviewSlot createViewSlot(InternalInventory inventory, int i) {
        PreviewSlot slot = new PreviewSlot(inventory, i);
        slot.x = 8 + (i % 9) * 18;
        slot.y = GUI_HEADER_HEIGHT + 1 + (i / 9) * 18;
        return slot;
    }

    @Override
    protected boolean validSlot(Slot slot) {
        return slot instanceof PreviewSlot;
    }

    @Override
    protected boolean handlerSlotClick(Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        if (getMenu().isClientSideSlot(slot)) {
            return false;
        }

        var stack = slot.getItem();
        if (stack.isEmpty() || stack.is(AEItems.BLANK_PATTERN.asItem())) {
            return false;
        }
        menu.switchEnable(slotIdx);
        return true;
    }
}
