package com.easterfg.pte.mixin.ae2;

import appeng.client.gui.WidgetContainer;
import appeng.client.gui.me.items.EncodingModePanel;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.me.items.ProcessingEncodingPanel;
import com.easterfg.pte.api.PatterEncodingTermMenuModify;
import com.easterfg.pte.gui.ModifyIcon;
import com.easterfg.pte.gui.ModifyIconButton;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author EasterFG on 2024/9/12
 */

@Mixin(ProcessingEncodingPanel.class)
public abstract class ProcessingEncodingPanelMixin extends EncodingModePanel {
    @Unique
    private ModifyIconButton patternTerminalExtended$multipleTow;
    @Unique
    private ModifyIconButton patternTerminalExtended$multipleThree;
    @Unique
    private ModifyIconButton patternTerminalExtended$multipleFive;
    @Unique
    private ModifyIconButton patternTerminalExtended$dividingTow;
    @Unique
    private ModifyIconButton patternTerminalExtended$dividingThree;
    @Unique
    private ModifyIconButton patternTerminalExtended$dividingFive;

    public ProcessingEncodingPanelMixin(PatternEncodingTermScreen<?> screen, WidgetContainer widgets) {
        super(screen, widgets);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(PatternEncodingTermScreen<?> screen, WidgetContainer widgets, CallbackInfo ci) {
        patternTerminalExtended$multipleTow = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).patternTerminalExtended$modifyPatter(2), ModifyIcon.MULTIPLY_2,
                Component.literal("样板配方 x 2"),
                Component.literal("将样板材料和配方数量 x 2"));

        patternTerminalExtended$multipleThree = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).patternTerminalExtended$modifyPatter(3), ModifyIcon.MULTIPLY_3,
                Component.literal("样板配方 x 3"),
                Component.literal("将样板材料和配方数量 x 3"));

        patternTerminalExtended$multipleFive = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).patternTerminalExtended$modifyPatter(5), ModifyIcon.MULTIPLY_5,
                Component.literal("样板配方 x 5"),
                Component.literal("将样板材料和配方数量 x 5"));

        patternTerminalExtended$dividingTow = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).patternTerminalExtended$modifyPatter(-2), ModifyIcon.DIVISION_2,
                Component.literal("样板配方 ÷ 2"),
                Component.literal("将样板材料和配方数量 ÷ 2"));

        patternTerminalExtended$dividingThree = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).patternTerminalExtended$modifyPatter(-3), ModifyIcon.DIVISION_3,
                Component.literal("样板配方 ÷ 3"),
                Component.literal("将样板材料和配方数量 ÷ 3"));

        patternTerminalExtended$dividingFive = new ModifyIconButton(b -> ((PatterEncodingTermMenuModify) this.menu).patternTerminalExtended$modifyPatter(-5), ModifyIcon.DIVISION_5,
                Component.literal("样板配方 ÷ 5"),
                Component.literal("将样板材料和配方数量 ÷ 5"));

        widgets.add("modify1", patternTerminalExtended$multipleTow);
        widgets.add("modify2", patternTerminalExtended$multipleThree);
        widgets.add("modify3", patternTerminalExtended$multipleFive);
        widgets.add("modify4", patternTerminalExtended$dividingTow);
        widgets.add("modify5", patternTerminalExtended$dividingThree);
        widgets.add("modify6", patternTerminalExtended$dividingFive);
    }

    @Inject(method = "setVisible", at = @At("TAIL"), remap = false)
    public void setVisibleHooks(boolean visible, CallbackInfo ci) {
        this.patternTerminalExtended$multipleTow.setVisibility(visible);
        this.patternTerminalExtended$multipleThree.setVisibility(visible);
        this.patternTerminalExtended$multipleFive.setVisibility(visible);
        this.patternTerminalExtended$dividingTow.setVisibility(visible);
        this.patternTerminalExtended$dividingThree.setVisibility(visible);
        this.patternTerminalExtended$dividingFive.setVisibility(visible);
    }
}
