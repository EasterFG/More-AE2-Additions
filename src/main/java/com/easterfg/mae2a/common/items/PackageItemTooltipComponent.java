package com.easterfg.mae2a.common.items;

import java.util.List;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

/**
 * @author EasterFG on 2025/4/1
 */
public record PackageItemTooltipComponent(List<ItemStack> content, int limit,
        boolean pressShift) implements TooltipComponent {
    public List<ItemStack> content() {
        return content;
    }
}
