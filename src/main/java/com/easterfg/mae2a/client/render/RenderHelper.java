package com.easterfg.mae2a.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

/**
 * @author EasterFG on 2025/4/1
 */
public class RenderHelper {

    public static void drawItem(Minecraft minecraft, GuiGraphics guiGraphics, int x, int y, ItemStack stack) {
        var poseStack = guiGraphics.pose();
        poseStack.pushPose();

        guiGraphics.renderItem(stack, x, y);
        guiGraphics.renderItemDecorations(minecraft.font, stack, x, y, "");

        poseStack.popPose();
    }

}
