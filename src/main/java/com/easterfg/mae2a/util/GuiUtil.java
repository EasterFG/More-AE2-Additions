package com.easterfg.mae2a.util;

import appeng.client.gui.style.WidgetStyle;

/**
 * @author EasterFG on 2025/3/26
 */
@SuppressWarnings("unused")
public final class GuiUtil {
    private GuiUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
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
