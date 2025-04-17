package com.easterfg.mae2a.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;

import appeng.core.definitions.AEParts;
import appeng.core.definitions.ColoredItemDefinition;
import appeng.items.parts.ColoredPartItem;
import appeng.parts.networking.*;

import lombok.Getter;

/**
 * @author EasterFG on 2025/4/12
 */
public enum CableType {

    GLASS_CABLE(GlassCablePart.class, AEParts.GLASS_CABLE, Component.translatable("tools.mae2a.glass_cable")),
    SMART_CABLE(SmartCablePart.class, AEParts.SMART_CABLE, Component.translatable("tools.mae2a.smart_cable")),
    COVERED_CABLE(CoveredCablePart.class, AEParts.COVERED_CABLE, Component.translatable("tools.mae2a.covered_cable")),
    SMART_DENSE_CABLE(SmartDenseCablePart.class, AEParts.SMART_DENSE_CABLE,
            Component.translatable("tools.mae2a.smart_dense_cable")),
    COVERED_DENSE_CABLE(CoveredDenseCablePart.class, AEParts.COVERED_DENSE_CABLE,
            Component.translatable("tools.mae2a.covered_dense_cable"));

    private final Class<? extends CablePart> cable;
    @Getter
    private final Component translation;
    @Getter
    private final ColoredItemDefinition<? extends ColoredPartItem<?>> items;

    CableType(Class<? extends CablePart> cable, ColoredItemDefinition<? extends ColoredPartItem<?>> items,
            Component translation) {
        this.cable = cable;
        this.translation = translation;
        this.items = items;
    }

    public boolean isValid(ItemLike like) {
        if (!(like instanceof ColoredPartItem<?> item))
            return false;
        return item.getPartClass().equals(cable);
    }
}
