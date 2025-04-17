package com.easterfg.mae2a.common.menu.host;

import net.minecraft.world.entity.player.Player;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocator;

import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;

/**
 * @author EasterFG on 2025/4/3
 */
public interface PatternProviderPlusLoginHost extends PatternProviderLogicHost {
    IUpgradeInventory getUpgrades();

    default void openMenu(Player player, MenuLocator locator) {
        MenuOpener.open(PatternProviderPlusMenu.TYPE, player, locator);
    }

    @Override
    default void returnToMainMenu(Player player, ISubMenu subMenu) {
        MenuOpener.returnTo(PatternProviderPlusMenu.TYPE, player, subMenu.getLocator());
    }
}
