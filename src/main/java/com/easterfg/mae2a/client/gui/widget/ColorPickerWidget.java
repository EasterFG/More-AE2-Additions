package com.easterfg.mae2a.client.gui.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import appeng.api.util.AEColor;

/**
 * @author EasterFG on 2025/4/10
 */
public class ColorPickerWidget extends AbstractWidget {

    private static final int COLOR_BOX = 10;
    private static final int COLUMNS = 4;
    private static final int SPACING = 4;

    private AEColor activeColor;
    private final List<AEColor> colors;
    /**
     * 色块位置缓存
     */
    private final List<ColorBoxPosition> cachedPositions = new ArrayList<>();
    /**
     * 颜色缓存
     */
    private static final Map<AEColor, Integer> COLOR_CACHE = new ConcurrentHashMap<>();
    private boolean dirty = true;
    private final Component title;

    public static final Component DEFAULT_TITLE = Component.translatable("gui.mae2a.color_picker");

    @FunctionalInterface
    public interface OnColorChanged {
        void onColorChange(AEColor newColor);
    }

    private record ColorBoxPosition(int relX, int relY, AEColor color) {
    }

    private OnColorChanged callback;

    public ColorPickerWidget(@Nullable AEColor activeColor, AEColor... colors) {
        this(0, 0, activeColor, DEFAULT_TITLE, colors);
    }

    public ColorPickerWidget(int x, int y, @Nullable AEColor activeColor, Component title, AEColor... colors) {
        super(x, y, calculateWidth(), calculateHeight(colors.length == 0 ? AEColor.values().length : colors.length),
                Component.empty());
        this.activeColor = activeColor == null ? AEColor.TRANSPARENT : activeColor;
        this.colors = Arrays.asList(colors.length > 0 ? colors : AEColor.values());
        this.title = title;
    }

    private static int calculateWidth() {
        return COLUMNS * COLOR_BOX + (COLUMNS + 1) * SPACING;
    }

    private static int calculateHeight(int colorCount) {
        int rows = (int) Math.ceil((double) colorCount / COLUMNS);
        return rows * (COLOR_BOX + SPACING) + (getFont().lineHeight + 4);
    }

    private static Font getFont() {
        return Minecraft.getInstance().font;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (dirty)
            updateLayoutCache();

        renderTitle(guiGraphics);
        renderColors(guiGraphics, mouseX, mouseY);

    }

    protected void renderTitle(GuiGraphics guiGraphics) {
        if (title != null) {
            Font font = getFont();
            int titleWidth = font.width(title);
            int titleX = this.getX() + (this.width - titleWidth) / 2;
            int titleY = this.getY() + 2;
            guiGraphics.drawString(font, title, titleX, titleY, 0xFFFFFFFF);
        }
    }

    protected void renderColors(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        final int widgetX = this.getX();
        final int widgetY = this.getY();
        boolean tooltipRendered = false;

        final int relMouseX = mouseX - widgetX;
        final int relMouseY = mouseY - widgetY;

        for (ColorBoxPosition pos : cachedPositions) {
            final int x = widgetX + pos.relX();
            final int y = widgetY + pos.relY();

            drawColorBox(guiGraphics, x, y, pos.color());

            if (!tooltipRendered && isHoveredColor(relMouseX, relMouseY, pos.relX, pos.relY)) {
                guiGraphics.fill(x, y, x + COLOR_BOX, y + COLOR_BOX, 0x40FFFFFF);
                guiGraphics.renderTooltip(
                        Minecraft.getInstance().font,
                        Component.translatable(pos.color.toString()),
                        mouseX, mouseY);
                tooltipRendered = true;
            }

            if (pos.color() == activeColor) {
                drawSelectionIndicator(guiGraphics, x, y);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY))
            return false;

        final int relX = (int) (mouseX - this.getX()) + SPACING;
        final int relY = (int) (mouseY - this.getY()) + SPACING;

        for (ColorBoxPosition pos : cachedPositions) {
            if (isHoveredColor(relX, relY, pos.relX(), pos.relY())) {
                setActiveColor(pos.color());
                return true;
            }
        }
        return false;
    }

    public void markDirt() {
        dirty = true;
    }

    private void updateLayoutCache() {
        cachedPositions.clear();
        int rowIndex = 0;
        int renderedCount = 0;

        while (renderedCount < colors.size()) {
            int itemsInRow = Math.min(colors.size() - renderedCount, COLUMNS);
            int rowWidth = itemsInRow * COLOR_BOX + (itemsInRow - 1) * SPACING;
            int startX = (this.width - rowWidth) / 2;

            for (int i = 0; i < itemsInRow; i++) {
                int x = startX + i * (COLOR_BOX + SPACING);
                int y = rowIndex * (COLOR_BOX + SPACING) + getFont().lineHeight + 4;
                cachedPositions.add(new ColorBoxPosition(x, y, colors.get(renderedCount + i)));
            }

            renderedCount += itemsInRow;
            rowIndex++;
        }
        dirty = false;
    }

    private boolean isHoveredColor(double mouseX, double mouseY, int relX, int relY) {
        return mouseX >= relX &&
                mouseX <= relX + COLOR_BOX &&
                mouseY >= relY &&
                mouseY <= relY + COLOR_BOX;
    }

    private void drawColorBox(GuiGraphics gui, int x, int y, AEColor color) {
        int argb = COLOR_CACHE.computeIfAbsent(
                color,
                c -> c.getVariantByTintIndex(AEColor.TINTINDEX_MEDIUM_BRIGHT) | 0xFF000000);
        gui.fill(x, y, x + COLOR_BOX, y + COLOR_BOX, argb);
    }

    private void drawSelectionIndicator(GuiGraphics gui, int x, int y) {
        gui.renderOutline(x, y, COLOR_BOX, COLOR_BOX, 0xFFFFFFFF); // 白色外框
    }

    public void setActiveColor(AEColor color) {
        if (this.activeColor != color) {
            this.activeColor = color;
            if (callback != null) {
                callback.onColorChange(color);
            }
            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }

    public void setOnColorChanged(OnColorChanged callback) {
        this.callback = callback;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }
}
