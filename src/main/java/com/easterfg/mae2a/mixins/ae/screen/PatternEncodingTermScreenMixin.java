package com.easterfg.mae2a.mixins.ae.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.me.items.PatternEncodingTermMenu;

import com.easterfg.mae2a.mixins.ae.IScreenStyleMixin;
import com.easterfg.mae2a.util.GuiUtil;
import com.easterfg.mae2a.util.Platform;

/**
 * @author EasterFG on 2025/3/26
 */
@Mixin(PatternEncodingTermScreen.class)
public abstract class PatternEncodingTermScreenMixin<C extends PatternEncodingTermMenu> extends MEStorageScreen<C> {

    public PatternEncodingTermScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initHook(PatternEncodingTermMenu menu, Inventory playerInventory, Component title, ScreenStyle style,
            CallbackInfo ci) {
        if (Platform.isModLoaded("ae2wtlib")) {
            if (menu instanceof de.mari_023.ae2wtlib.wet.WETMenu) {
                return;
            }
        }
        IScreenStyleMixin styleMixin = (IScreenStyleMixin) style;
        styleMixin.getWidgets().put("upgrades", GuiUtil.WidgetStyleBuilder.create()
                .setTop(111)
                .setRight(-2)
                .build());
    }
}
