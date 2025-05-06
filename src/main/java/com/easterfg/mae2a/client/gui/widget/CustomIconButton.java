package com.easterfg.mae2a.client.gui.widget;

import java.util.List;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.Icon;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.widgets.ITooltip;

import lombok.Setter;

/**
 * @author EasterFG on 2025/3/28
 */
@OnlyIn(Dist.CLIENT)
public class CustomIconButton extends Button implements ITooltip {

    private final Blitter texture;
    private final Blitter disableTexture;

    private final Component onTooltip;
    private final Component offTooltip;

    @Setter
    private Supplier<Boolean> statusSupplier;

    public CustomIconButton(int width, int height, Button.OnPress onPress, Blitter texture,
            Blitter disableTexture, Component onTooltip, Component offTooltip) {
        super(0, 0, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.disableTexture = disableTexture;
        this.onTooltip = onTooltip;
        this.offTooltip = offTooltip;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            guiGraphics.pose().pushPose();
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();

            if (this.isFocused()) {
                guiGraphics.fill(getX() - 1, getY() - 1, getX() + width + 1, getY(), 0xFFFFFFFF);
                guiGraphics.fill(getX() - 1, getY(), getX(), getY() + height, 0xFFFFFFFF);
                guiGraphics.fill(getX() + width, getY(), getX() + width + 1, getY() + height, 0xFFFFFFFF);
                guiGraphics.fill(getX() - 1, getY() + height, getX() + width + 1, getY() + height + 1, 0xFFFFFFFF);
            }

            Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter().dest(getX(), getY()).blit(guiGraphics);

            final Blitter currentTexture = (this.disableTexture != null && !this.statusSupplier.get())
                    ? this.disableTexture
                    : this.texture;

            final float alpha = (this.disableTexture == null && !this.statusSupplier.get())
                    ? 0.5f
                    : 1.0f;

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            currentTexture.dest(getX(), getY()).blit(guiGraphics);

            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public List<Component> getTooltipMessage() {
        return List.of(this.getMessage(), this.statusSupplier.get() ? this.onTooltip : this.offTooltip);
    }

    @Override
    public Rect2i getTooltipArea() {
        return new Rect2i(this.getX(), this.getY(), 16, 16);
    }

    @Override
    public boolean isTooltipAreaVisible() {
        return this.visible;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {

        private int widget = 16;
        private int height = 16;

        private Blitter texture;
        private Blitter disableTexture;

        private Component onTooltip;
        private Component offTooltip;
        private Component message;

        private Supplier<Boolean> status = () -> true;
        private OnPress press;

        public static Builder builder(OnPress press) {
            Builder builder = new Builder();
            builder.press = press;
            return builder;
        }

        public Builder widget(int widget) {
            this.widget = widget;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder location(ResourceLocation location) {
            this.texture = Blitter.texture(location, 16, 16).src(0, 0, 16, 16);
            return this;
        }

        public Builder texture(Blitter texture) {
            this.texture = texture;
            return this;
        }

        public Builder disableTexture(Blitter disableTexture) {
            this.disableTexture = disableTexture;
            return this;
        }

        public Builder status(@NotNull Supplier<Boolean> status) {
            this.status = status;
            return this;
        }

        public Builder tooltip(Component onTooltip, Component offTooltip) {
            this.onTooltip = onTooltip;
            this.offTooltip = offTooltip;
            return this;
        }

        public Builder message(Component message) {
            this.message = message;
            return this;
        }

        public CustomIconButton build() {
            var button = new CustomIconButton(this.widget, this.height, press, texture, disableTexture, onTooltip,
                    offTooltip);
            button.setStatusSupplier(status);

            if (message != null) {
                button.setMessage(message);
            }

            return button;
        }
    }
}
