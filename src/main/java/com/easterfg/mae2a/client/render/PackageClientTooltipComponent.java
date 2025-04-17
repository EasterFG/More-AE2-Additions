package com.easterfg.mae2a.client.render;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.me.common.StackSizeRenderer;

import com.easterfg.mae2a.common.items.PackageItemTooltipComponent;

/**
 * @author EasterFG on 2025/4/1
 */
@OnlyIn(Dist.CLIENT)
public class PackageClientTooltipComponent implements ClientTooltipComponent {
    private static final int MAX_SHOW = 8;

    private final PackageItemTooltipComponent tooltipComponent;

    public PackageClientTooltipComponent(PackageItemTooltipComponent tooltipComponent) {
        this.tooltipComponent = tooltipComponent;
    }

    @Override
    public int getHeight() {
        return (int) ((Math.ceil(tooltipComponent.content().size() / (double) MAX_SHOW)) * 17);
    }

    @Override
    public int getWidth(@NotNull Font pFont) {
        int width = 0;
        var content = tooltipComponent.content();
        if (!content.isEmpty()) {
            var size = content.size();
            width = size > 8 ? 136 : 17 * size;
        }
        return width;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics graphics) {
        var content = tooltipComponent.content();
        if (!content.isEmpty()) {
            int index = 0;
            for (ItemStack stack : content) {
                int xo = index % MAX_SHOW * 17;
                int yo = index / MAX_SHOW * 17;
                RenderHelper.drawItem(Minecraft.getInstance(), graphics, x + xo, y + yo, stack);
                StackSizeRenderer.renderSizeLabel(graphics, font, x + xo, y + yo,
                        String.valueOf(stack.getCount()), false);
                index++;
            }
        }
    }
}
