package com.easterfg.mae2a.client.register;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.IForgeRegistry;

import appeng.core.AppEng;

import com.easterfg.mae2a.common.menu.CablePlaceToolMenu;
import com.easterfg.mae2a.common.menu.PatternModifyMenu;
import com.easterfg.mae2a.common.menu.PatternPreviewListMenu;
import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;

/**
 * @author EasterFG on 2024/12/4
 */
public class MenuTypeRegister {
    public static void init(IForgeRegistry<MenuType<?>> registry) {
        registry.register(AppEng.makeId("pattern_modify"), PatternModifyMenu.TYPE);
        registry.register(AppEng.makeId("pattern_provider_plus"), PatternProviderPlusMenu.TYPE);
        registry.register(AppEng.makeId("pattern_preview_list"), PatternPreviewListMenu.TYPE);
        registry.register(AppEng.makeId("cable_place_tool"), CablePlaceToolMenu.TYPE);
    }
}
