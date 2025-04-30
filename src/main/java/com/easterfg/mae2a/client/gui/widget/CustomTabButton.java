package com.easterfg.mae2a.client.gui.widget;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import appeng.client.gui.Icon;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.widgets.TabButton;

import lombok.Getter;
import lombok.Setter;

public class CustomTabButton extends TabButton {

    @Getter
    @Setter
    private Style style = Style.BOX;
    private final Blitter icon;

    @Getter
    @Setter
    private boolean selected;

    public CustomTabButton(Blitter icon, Component message, OnPress onPress) {
        super((ItemStack) null, message, onPress);
        this.icon = icon;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.visible) {
            // Selects the button border from the sprite-sheet, where each type occupies a
            // 2x2 slot
            var backdrop = switch (this.style) {
                case CORNER -> this.isFocused() ? Icon.TAB_BUTTON_BACKGROUND_BORDERLESS_FOCUS
                        : Icon.TAB_BUTTON_BACKGROUND_BORDERLESS;
                case BOX -> this.isFocused() ? Icon.TAB_BUTTON_BACKGROUND_FOCUS : Icon.TAB_BUTTON_BACKGROUND;
                case HORIZONTAL -> {
                    if (this.isFocused()) {
                        yield Icon.HORIZONTAL_TAB_FOCUS;
                    } else if (this.selected) {
                        yield Icon.HORIZONTAL_TAB_SELECTED;
                    }
                    yield Icon.HORIZONTAL_TAB;
                }
            };

            backdrop.getBlitter().dest(getX(), getY()).blit(guiGraphics);

            var iconX = switch (this.style) {
                case CORNER -> 4;
                case BOX -> 3;
                case HORIZONTAL -> 1;
            };
            var iconY = 3;

            if (this.icon != null) {
                this.icon.dest(getX() + iconX, getY() + iconY).blit(guiGraphics);
            }
        }
    }
}
