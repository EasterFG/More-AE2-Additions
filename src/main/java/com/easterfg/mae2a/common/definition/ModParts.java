package com.easterfg.mae2a.common.definition;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import appeng.api.parts.PartModels;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.api.definition.ItemDefinition;
import com.easterfg.mae2a.common.part.PatternProviderPlusPart;

/**
 * @author EasterFG on 2025/4/5
 */
public class ModParts {

    public static final ItemDefinition<PartItem<PatternProviderPlusPart>> PATTERN_PROVIDER_PLUS = createPart(
            "ME Pattern Provider Plus", "ME样板供应器Plus",
            MoreAE2Additions.id("cable_pattern_provider_plus"), PatternProviderPlusPart.class,
            PatternProviderPlusPart::new);

    private static <T extends IPart> ItemDefinition<PartItem<T>> createPart(
            String en,
            String cn,
            ResourceLocation id,
            Class<T> partClass,
            Function<IPartItem<T>, T> factory) {

        PartModels.registerModels(PartModelsHelper.createModels(partClass));
        return ModItems.item(en, cn, id, props -> new PartItem<>(props, partClass, factory));
    }

    public static void init() {
    }
}
