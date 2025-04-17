package com.easterfg.mae2a.client.screen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.api.upgrades.Upgrades;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.UpgradesPanel;
import appeng.core.localization.GuiText;
import appeng.menu.SlotSemantics;

import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;

/**
 * @author EasterFG on 2025/4/3
 */
public class PatternProviderPlusScreen extends PatternProviderScreen<PatternProviderPlusMenu> {

//    private final CustomIconButton packageMode;

    public PatternProviderPlusScreen(PatternProviderPlusMenu menu, Inventory playerInventory, Component title,
            ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.widgets.add("upgrades", new UpgradesPanel(
                menu.getSlots(SlotSemantics.UPGRADE),
                this::getCompatibleUpgrades));
//        if (menu.getHost().getConfigManager().hasSetting(AdditionalSettings.PACKAGE_ITEM)) {
//            packageMode = new CustomIconButton((__) -> setPackageMode(),
//                    MoreAE2Additions.id("textures/item/fake_crafting_card.png"), Component.empty(), Component.empty());
//            addToLeftToolbar(packageMode);
//        } else {
//            packageMode = null;
//        }
    }

    private List<Component> getCompatibleUpgrades() {
        var list = new ArrayList<Component>();
        list.add(GuiText.CompatibleUpgrades.text());
        list.addAll(Upgrades.getTooltipLinesForMachine(menu.getUpgrades().getUpgradableItem()));
        return list;
    }

//    private void setPackageModeMessage() {
//        switch (menu.getPackageItem()) {
//            case YES -> packageMode.setMessage(Component.translatable("gui.mae2a.pattern_provider_plus.package_mode",
//                    Component.translatable("gui.mae2a.mode.on")));
//            case NO -> packageMode.setMessage(Component.translatable("gui.mae2a.pattern_provider_plus.package_mode",
//                    Component.translatable("gui.mae2a.mode.off")));
//        }
//    }
//
//    @Override
//    protected void updateBeforeRender() {
//        super.updateBeforeRender();
//        if (this.packageMode != null) {
//            this.packageMode.setActive(menu.getPackageItem() == YesNo.YES);
//            this.packageMode.setVisibility(menu.hasUpgrade(ModItems.FAKE_CRAFT_CARD));
//            this.setPackageModeMessage();
//        }
//    }
}
