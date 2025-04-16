package com.easterfg.mae2a.mixins.ae;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.style.WidgetStyle;

/**
 * @author EasterFG on 2025/3/26
 */
@Mixin(ScreenStyle.class)
public interface IScreenStyleMixin {
    @Accessor(value = "widgets", remap = false)
    Map<String, WidgetStyle> getWidgets();
}
