package com.easterfg.mae2a.mixins.ae.screen;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.Icon;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.implementations.PatternProviderMenu;
import appeng.menu.slot.RestrictedInputSlot;

import com.easterfg.mae2a.config.MAE2AConfig;
import com.easterfg.mae2a.util.GuiUtil;

@Mixin(PatternProviderScreen.class)
public abstract class PatternProviderScreenMixin<C extends PatternProviderMenu> extends AEBaseScreen<C> {
    public PatternProviderScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Override
    public void renderSlot(GuiGraphics guiGraphics, Slot s) {
        if (MAE2AConfig.showAmount) {
            if (s instanceof RestrictedInputSlot slot && slot.getIcon() == Icon.BACKGROUND_ENCODED_PATTERN) {
                GuiUtil.renderPatternSlot(guiGraphics, slot, this.imageWidth);
                return;
            }
        }
        super.renderSlot(guiGraphics, s);

    }
}
