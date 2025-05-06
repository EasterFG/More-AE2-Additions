package com.easterfg.mae2a.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.implementations.PatternProviderMenu;

import com.easterfg.mae2a.common.menu.host.PatternProviderPlusLogicHost;

import lombok.Getter;

/**
 * @author EasterFG on 2025/4/3
 */
@Getter
public class PatternProviderPlusMenu extends PatternProviderMenu {

    public static final MenuType<PatternProviderPlusMenu> TYPE = MenuTypeBuilder
            .create(PatternProviderPlusMenu::new, PatternProviderPlusLogicHost.class)
            .build("pattern_provider_plus");

    private final PatternProviderPlusLogicHost host;

    protected PatternProviderPlusMenu(int id, Inventory playerInventory, PatternProviderPlusLogicHost host) {
        super(TYPE, id, playerInventory, host);
        this.host = host;
        this.setupUpgrades();

    }

    protected void setupUpgrades() {
        setupUpgrades(host.getUpgrades());
    }

    public IUpgradeInventory getUpgrades() {
        return host.getUpgrades();
    }
}
