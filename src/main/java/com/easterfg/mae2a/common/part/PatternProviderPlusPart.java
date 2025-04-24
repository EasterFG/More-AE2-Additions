package com.easterfg.mae2a.common.part;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.core.AppEng;
import appeng.items.parts.PartModels;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocator;
import appeng.parts.PartModel;
import appeng.parts.crafting.PatternProviderPart;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.common.definition.ModBlocks;
import com.easterfg.mae2a.common.definition.ModParts;
import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;
import com.easterfg.mae2a.common.menu.host.PatternProviderPlusLoginHost;
import com.easterfg.mae2a.common.menu.logic.PatternProviderPlusLogic;
import com.easterfg.mae2a.config.MAE2AConfig;

/**
 * @author EasterFG on 2025/4/5
 */
public class PatternProviderPlusPart extends PatternProviderPart
        implements IUpgradeableObject, PatternProviderPlusLoginHost {

    public static final ResourceLocation MODEL_BASE = new ResourceLocation(MoreAE2Additions.MOD_ID,
            "part/pattern_provider_plus_base");

    @PartModels
    public static final PartModel MODELS_OFF = new PartModel(MODEL_BASE,
            new ResourceLocation(AppEng.MOD_ID, "part/interface_off"));

    @PartModels
    public static final PartModel MODELS_ON = new PartModel(MODEL_BASE,
            new ResourceLocation(AppEng.MOD_ID, "part/interface_on"));

    @PartModels
    public static final PartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE,
            new ResourceLocation(AppEng.MOD_ID, "part/interface_has_channel"));
    private final IUpgradeInventory upgrades;

    public PatternProviderPlusPart(IPartItem<?> partItem) {
        super(partItem);
        upgrades = UpgradeInventories.forMachine(ModBlocks.PATTERN_PROVIDER_PLUS, 1, this::saveChanges);

    }

    @Override
    protected PatternProviderPlusLogic createLogic() {
        return new PatternProviderPlusLogic(this.getMainNode(), this, MAE2AConfig.plusMaxSlot);
    }

    @Override
    public PatternProviderPlusLogic getLogic() {
        return (PatternProviderPlusLogic) logic;
    }

    @Override
    public void openMenu(Player player, MenuLocator locator) {
        MenuOpener.open(PatternProviderPlusMenu.TYPE, player, locator);
    }

    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        MenuOpener.returnTo(PatternProviderPlusMenu.TYPE, player, subMenu.getLocator());
    }

    @Override
    public void writeToNBT(CompoundTag data) {
        super.writeToNBT(data);
        this.upgrades.writeToNBT(data, "upgrades");
    }

    @Override
    public void readFromNBT(CompoundTag data) {
        super.readFromNBT(data);
        this.upgrades.readFromNBT(data, "upgrades");
    }

    @Override
    public void addAdditionalDrops(List<ItemStack> drops, boolean wrenched) {
        super.addAdditionalDrops(drops, wrenched);

        for (var upgrade : upgrades) {
            drops.add(upgrade);
        }
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return ModParts.PATTERN_PROVIDER_PLUS.stack(1);
    }

    @Override
    public IPartModel getStaticModels() {
        if (this.isActive() && this.isPowered()) {
            return MODELS_HAS_CHANNEL;
        } else if (this.isPowered()) {
            return MODELS_ON;
        } else {
            return MODELS_OFF;
        }
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return upgrades;
    }
}
