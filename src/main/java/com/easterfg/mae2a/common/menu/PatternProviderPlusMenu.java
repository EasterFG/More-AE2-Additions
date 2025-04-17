package com.easterfg.mae2a.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;

import appeng.api.config.YesNo;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.implementations.PatternProviderMenu;

import com.easterfg.mae2a.common.menu.host.PatternProviderPlusLoginHost;

import lombok.Getter;

/**
 * @author EasterFG on 2025/4/3
 */
@Getter
public class PatternProviderPlusMenu extends PatternProviderMenu {

    public static final MenuType<PatternProviderPlusMenu> TYPE = MenuTypeBuilder
            .create(PatternProviderPlusMenu::new, PatternProviderPlusLoginHost.class)
            .build("pattern_provider_plus");

    @GuiSync(8)
    public YesNo packageItem = YesNo.NO;

    private final PatternProviderPlusLoginHost host;

    protected PatternProviderPlusMenu(int id, Inventory playerInventory, PatternProviderPlusLoginHost host) {
        super(TYPE, id, playerInventory, host);
        this.host = host;
        this.setupUpgrades();

    }

    protected void setupUpgrades() {
        setupUpgrades(host.getUpgrades());
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
    }

    public IUpgradeInventory getUpgrades() {
        return host.getUpgrades();
    }

    public boolean hasUpgrade(ItemLike upgradeCard) {
        return host.getUpgrades().isInstalled(upgradeCard);
    }
}
