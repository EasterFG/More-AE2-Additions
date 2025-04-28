package com.easterfg.mae2a.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import appeng.block.AEBaseBlock;
import appeng.block.AEBaseBlockItem;
import appeng.items.AEBaseItem;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.api.definition.ItemDefinition;
import com.easterfg.mae2a.common.definition.MAE2AItems;

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
                .icon(() -> MAE2AItems.PATTERN_MODIFY_TOOL.stack(1))
                .displayItems(MainCreativeMod::buildDisplayItems)
                .build();
        Registry.register(registry, MAIN, tab);
    }

    public static void add(ItemDefinition<?> itemDef) {
        itemDefinitions.add(itemDef);
    }

    private static void buildDisplayItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters,
            CreativeModeTab.Output output) {
        for (var itemDef : itemDefinitions) {
            var item = itemDef.asItem();

            if (item instanceof AEBaseBlockItem baseItem
                    && baseItem.getBlock() instanceof AEBaseBlock baseBlock) {
                baseBlock.addToMainCreativeTab(output);
            } else if (item instanceof AEBaseItem baseItem) {
                baseItem.addToMainCreativeTab(output);
            } else {
                output.accept(itemDef);
            }
        }
    }
}
