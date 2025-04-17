package com.easterfg.mae2a.common.definition;

import static com.easterfg.mae2a.MoreAE2Additions.id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import appeng.api.upgrades.Upgrades;

import com.easterfg.mae2a.api.MainCreativeMod;
import com.easterfg.mae2a.api.definition.ItemDefinition;
import com.easterfg.mae2a.common.items.InfiniteDyesCellItem;
import com.easterfg.mae2a.common.items.ItemCablePlaceTool;
import com.easterfg.mae2a.common.items.PackageItem;
import com.easterfg.mae2a.common.items.PatternModifyToolItem;

/**
 * @author EasterFG on 2024/9/25
 */
@SuppressWarnings("unused")
public class ModItems {
    private static final List<ItemDefinition<?>> ITEMS = new ArrayList<>();

    public static final ItemDefinition<Item> PATTERN_MODIFY_TOOL = item("Pattern Modify Tool", "样板修改工具",
            id("pattern_modify_tool"), p -> new PatternModifyToolItem(p.stacksTo(1)));
    public static final ItemDefinition<Item> INFINITE_DYES_CELL = item("Infinite Dyes Cell", "无限染料",
            id("infinite_dyes_cell"), p -> new InfiniteDyesCellItem(p.stacksTo(1)));
    public static final ItemDefinition<Item> PACKAGES_ITEM = item("Item Package", "物品包裹",
            id("item_package"), p -> new PackageItem(p.stacksTo(1)));
    public static final ItemDefinition<Item> FAKE_CRAFT_CARD = item("Fake Craft Card", "伪合成卡",
            id("fake_crafting_card"), Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<Item> PATTERN_REFILL_CARD = item("Pattern Refill Card", "空白样板重填卡",
            id("pattern_refill_card"), Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<Item> CABLE_PLACE_TOOL = item("Cable Place Tools", "线缆放置工具",
            id("cable_place_tools"), p -> new ItemCablePlaceTool(p.stacksTo(1)));

    static <T extends Item> ItemDefinition<T> item(String eng, String zh, ResourceLocation id,
            Function<Item.Properties, T> factory) {
        Item.Properties p = new Item.Properties();
        T item = factory.apply(p);
        ItemDefinition<T> definition = new ItemDefinition<>(id, item, eng, zh);
        MainCreativeMod.add(definition);
        ITEMS.add(definition);
        return definition;
    }

    public static List<ItemDefinition<?>> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    public static void init() {
    }
}
