package com.easterfg.mae2a.util;

import java.util.Map;
import java.util.WeakHashMap;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AmountFormat;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.me.common.StackSizeRenderer;
import appeng.client.gui.style.WidgetStyle;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.menu.slot.AppEngSlot;

/**
 * @author EasterFG on 2025/3/26
 */
@SuppressWarnings("unused")
public final class GuiUtil {
    private GuiUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final Map<ItemStack, GenericStack> SHOW_CACHE = new WeakHashMap<>();

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static GenericStack getShowAmount(ItemStack item) {
        var show = SHOW_CACHE.get(item);

        if (show != null) {
            return show;
        }

        IPatternDetails details = PatternDetailsHelper.decodePattern(item, Minecraft.getInstance().level, false);
        if (details == null) {
            return null;
        }

        show = details.getPrimaryOutput();
        SHOW_CACHE.put(item, show);
        return show;
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderPatternSlot(GuiGraphics guiGraphics, AppEngSlot s, int imageWidth) {
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

        guiGraphics.renderItem(display, s.x, s.y, s.x + s.y * imageWidth);

        if (!is.isEmpty() && is.getItem() instanceof EncodedPatternItem patternItem) {
            IPatternDetails decode = patternItem.decode(is, Minecraft.getInstance().level, false);
            if (decode != null) {
                GenericStack output = decode.getOutputs()[0];
                String amount = output.what().formatAmount(output.amount(), AmountFormat.SLOT);
                StackSizeRenderer.renderSizeLabel(guiGraphics, Minecraft.getInstance().font, s.x, s.y, amount, false);
            }
        }
        guiGraphics.pose().popPose();
    }

    public static class WidgetStyleBuilder {

        private final WidgetStyle widgetStyle;

        private WidgetStyleBuilder() {
            widgetStyle = new WidgetStyle();
        }

        public static WidgetStyleBuilder create() {
            return new WidgetStyleBuilder();
        }

        public WidgetStyleBuilder setWidget(int widget) {
            widgetStyle.setWidth(widget);
            return this;
        }

        public WidgetStyleBuilder setHeight(int height) {
            widgetStyle.setHeight(height);
            return this;
        }

        public WidgetStyleBuilder setTop(int top) {
            widgetStyle.setTop(top);
            return this;
        }

        public WidgetStyleBuilder setBottom(int bottom) {
            widgetStyle.setBottom(bottom);
            return this;
        }

        public WidgetStyleBuilder setLeft(int left) {
            widgetStyle.setLeft(left);
            return this;
        }

        public WidgetStyleBuilder setRight(int right) {
            widgetStyle.setRight(right);
            return this;
        }

        public WidgetStyleBuilder setHideEdge(boolean hideEdge) {
            widgetStyle.setHideEdge(hideEdge);
            return this;
        }

        public WidgetStyle build() {
            return widgetStyle;
        }

    }

}
