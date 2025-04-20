package com.easterfg.mae2a.client.gui.widget;

import java.util.List;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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

    private final ResourceLocation texture;
    @Nullable
    private ResourceLocation subTexture;

    private final Component onTooltip;
    private final Component offTooltip;

    @Setter
    @NotNull
    private Supplier<Boolean> statusSupplier = () -> true;

    public CustomIconButton(Button.OnPress onPress, ResourceLocation texture, Component onTooltip,
            Component offTooltip) {
        this(16, 16, onPress, texture, onTooltip, offTooltip);
    }

    public CustomIconButton(int width, int height, Button.OnPress onPress, ResourceLocation texture,
            Component onTooltip, Component offTooltip) {
        this(width, height, onPress, texture, null, onTooltip, offTooltip);
    }

    public CustomIconButton(int width, int height, Button.OnPress onPress, ResourceLocation texture,
            ResourceLocation subTexture, Component onTooltip, Component offTooltip) {
        super(0, 0, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.subTexture = subTexture;
        this.onTooltip = onTooltip;
        this.offTooltip = offTooltip;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    public void setActive(boolean active) {
        this.active = active;
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

            // background
            guiGraphics.blit(AE_STATUS_TEXTURE, this.getX(), this.getY(), 16, 16, 240.0F, 240.0F, 16, 16, 256, 256);

            if (!this.statusSupplier.get() && subTexture == null) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
            } else {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

            if (subTexture == null) {
                guiGraphics.blit(this.texture, this.getX(), this.getY(), width, height, 0.0F, 0.0F, 512, 512, 512, 512);
            } else {
                guiGraphics.blit(this.subTexture, this.getX(), this.getY(), width, height, 0.0F, 0.0F, 512, 512, 512,
                        512);
            }
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
