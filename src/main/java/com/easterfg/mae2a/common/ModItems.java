package com.easterfg.mae2a.common;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.common.items.InfiniteDyesCellItem;
import com.easterfg.mae2a.common.items.PatternModifyToolItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author EasterFG on 2024/9/25
 */
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreAE2Additions.MOD_ID);

    public static final RegistryObject<Item> PATTERN_MODIFY_TOOL = ITEMS.register("pattern_modify_tool",
            PatternModifyToolItem::new);

    public static final RegistryObject<Item> INFINITE_DYES_CELL = ITEMS.register("infinite_dyes_cell",
            InfiniteDyesCellItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
