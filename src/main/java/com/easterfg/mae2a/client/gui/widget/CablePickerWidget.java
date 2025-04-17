package com.easterfg.mae2a.client.gui.widget;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import appeng.api.util.AEColor;

import com.easterfg.mae2a.util.CableType;

import lombok.Setter;

/**
 * @author EasterFG on 2025/4/10
 */
public class CablePickerWidget extends AbstractWidget {

    private static final int ITEM_SPACING = 0;
    private static final int LINE_HEIGHT = 16;
    private static final int CHECKBOX_SIZE = 10;
    private static final int ITEM_WIDTH = 16;

    private static final int PADDING = 4;

    public static final Font FONT = Minecraft.getInstance().font;
    private static final CableType[] cableTypes = CableType.values();

    private final Component title = Component.translatable("gui.mae2a.cable_picker");

    private int selectedIndex;
    @Setter
    private Consumer<Integer> onSelectionChanged;

    @Setter
    private Supplier<AEColor> colorSupplier;

    public CablePickerWidget(int selectedIndex, Supplier<AEColor> colorSupplier) {
        super(0, 0, 64, calHeight(cableTypes.length), Component.empty());
        this.selectedIndex = Math.max(selectedIndex, 0);
        this.colorSupplier = colorSupplier;
    }

    private static int calHeight(int count) {
        return 2 * PADDING + FONT.lineHeight + count * (LINE_HEIGHT + ITEM_SPACING);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderTitle(guiGraphics);
        renderItems(guiGraphics, mouseX, mouseY);
        renderTooltips(guiGraphics, mouseX, mouseY);
    }

    protected void renderTitle(GuiGraphics guiGraphics) {
        int titleWidth = FONT.width(title);
        int titleX = getX() + (width - titleWidth) / 2;
        int titleY = getY() + 2;
        guiGraphics.drawString(FONT, title, titleX, titleY, 0xFFFFFF);
    }

    protected void renderItems(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int startY = getY() + FONT.lineHeight + PADDING;

        for (int i = 0; i < cableTypes.length; i++) {
            int itemY = startY + i * (LINE_HEIGHT + ITEM_SPACING);
            renderItem(guiGraphics, i, getX(), itemY, mouseX, mouseY);
        }
    }

    private void renderItem(GuiGraphics guiGraphics, int index, int x, int y, int mouseX, int mouseY) {
        boolean isSelected = index == selectedIndex;
        boolean isHovered = isMouseOverItem(x, y, mouseX, mouseY);
        if (isHovered) {
            guiGraphics.fill(x, y, x + width, y + LINE_HEIGHT, 0x40FFFFFF);
        }

        int checkboxX = x + PADDING;
        int checkboxY = y + (LINE_HEIGHT - CHECKBOX_SIZE) / 2;
        drawCheckbox(guiGraphics, checkboxX, checkboxY, isSelected);

        int iconX = x + width - ITEM_WIDTH - 4;
        renderItemIcon(guiGraphics, index, iconX, y);
    }

    private void renderItemIcon(GuiGraphics guiGraphics, int index, int x, int y) {
        var stack = cableTypes[index].getItems().item(colorSupplier.get()).getDefaultInstance();
        guiGraphics.renderItem(stack, x, y);
    }

    private void drawCheckbox(GuiGraphics guiGraphics, int x, int y, boolean selected) {
        guiGraphics.fill(x, y, x + CHECKBOX_SIZE, y + CHECKBOX_SIZE, 0xFF555555);
        guiGraphics.fill(x + 1, y + 1, x + CHECKBOX_SIZE - 1, y + CHECKBOX_SIZE - 1,
                selected ? 0xFF00FF00 : 0xFFAAAAAA);
        if (selected) {
            guiGraphics.fill(x + 3, y + 3, x + CHECKBOX_SIZE - 3, y + CHECKBOX_SIZE - 3, 0x80000000);
        }
    }

    protected void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int startY = getY() + FONT.lineHeight + PADDING;

        for (int i = 0; i < cableTypes.length; i++) {
            int itemY = startY + i * (LINE_HEIGHT + ITEM_SPACING);
            if (isMouseOverItem(getX(), itemY, mouseX, mouseY)) {
                guiGraphics.renderTooltip(FONT, cableTypes[i].getTranslation(), mouseX, mouseY);
                break;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY))
            return false;

        int clickedIndex = calculateClickedIndex(mouseY);
        if (clickedIndex == selectedIndex)
            return false;
        if (clickedIndex != -1) {
            updateSelection(clickedIndex);
            return true;
        }
        return false;
    }

    private int calculateClickedIndex(double mouseY) {
        int relativeY = (int) (mouseY - getY() - FONT.lineHeight - PADDING);
        if (relativeY < 0)
            return -1;

        int index = 0;
        int currentY = 0;
        while (index < cableTypes.length) {
            if (relativeY >= currentY && relativeY < currentY + LINE_HEIGHT) {
                return index;
            }
            currentY += LINE_HEIGHT + ITEM_SPACING;
            index++;
        }
        return -1;
    }

    private boolean isMouseOverItem(int itemX, int itemY, int mouseX, int mouseY) {
        return mouseX >= itemX &&
                mouseX <= itemX + width &&
                mouseY >= itemY &&
                mouseY <= itemY + LINE_HEIGHT;
    }

    private void updateSelection(int newIndex) {
        selectedIndex = newIndex;
        if (onSelectionChanged != null) {
            onSelectionChanged.accept(selectedIndex);
        }
        Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }
}
