package com.easterfg.mae2a.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.api.definition.ItemDefinition;
import com.easterfg.mae2a.common.definition.ModItems;

/**
 * @author EasterFG on 2025/3/25
 */
public class MainCreativeMod {

    private static final List<ItemDefinition<?>> itemDefinitions = new ArrayList<>();

    public static final ResourceKey<CreativeModeTab> MAIN = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            MoreAE2Additions.id("main"));

    public static void init(Registry<CreativeModeTab> registry) {
        var tab = CreativeModeTab.builder()
                .title(Component.translatable("tooltip.mae2a.creative.tab"))
                .icon(() -> ModItems.PATTERN_MODIFY_TOOL.stack(1))
                .displayItems((__, output) -> itemDefinitions.forEach(output::accept))
                .build();
        Registry.register(registry, MAIN, tab);
    }

    public static void add(ItemDefinition<?> itemDef) {
        itemDefinitions.add(itemDef);
    }
}
