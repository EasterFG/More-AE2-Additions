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

import appeng.client.gui.Icon;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.widgets.ITooltip;

import lombok.Getter;
import lombok.Setter;

/**
 * @author EasterFG on 2025/3/28
 */
@Getter
@Setter
public class CustomIconButton extends Button implements ITooltip {

    private static final ResourceLocation AE_STATUS_TEXTURE = new ResourceLocation("ae2", "textures/guis/states.png");

    private final Blitter texture;
    private final Blitter disableTexture;

    private final Component onTooltip;
    private final Component offTooltip;

    @Setter
    @NotNull
    private Supplier<Boolean> statusSupplier = () -> true;

    public CustomIconButton(Button.OnPress onPress, ResourceLocation texture, Component onTooltip,
            Component offTooltip) {
        this(16, 16, onPress, Blitter.texture(texture, 16, 16).src(0, 0, 16, 16), onTooltip, offTooltip);
    }

    public CustomIconButton(Button.OnPress onPress, Blitter texture, Blitter disableTexture, Component onTooltip,
            Component offTooltip) {
        this(16, 16, onPress, texture, disableTexture, onTooltip, offTooltip);
    }

    public CustomIconButton(int width, int height, Button.OnPress onPress, Blitter texture,
            Component onTooltip, Component offTooltip) {
        this(width, height, onPress, texture, null, onTooltip, offTooltip);
    }

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
}
