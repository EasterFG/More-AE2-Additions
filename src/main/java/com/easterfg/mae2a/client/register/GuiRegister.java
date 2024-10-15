package com.easterfg.mae2a.client.register;

import appeng.init.client.InitScreens;
import com.easterfg.mae2a.client.screen.PatternModifyScreen;
import com.easterfg.mae2a.common.menu.PatternModifyMenu;

/**
 * @author EasterFG on 2024/10/1
 */
public class GuiRegister {

    public static void register() {
        InitScreens.register(
                PatternModifyMenu.TYPE,
                PatternModifyScreen::new,
                "/screens/pattern_modify.json");
    }

}
