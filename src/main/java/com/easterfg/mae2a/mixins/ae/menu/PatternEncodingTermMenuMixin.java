package com.easterfg.mae2a.mixins.ae.menu;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.MEStorage;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.core.definitions.AEItems;
import appeng.helpers.IMenuCraftingPacket;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;

import com.easterfg.mae2a.common.definition.MAE2AItems;

/**
 * @author EasterFG on 2025/3/20
 */
@Mixin(PatternEncodingTermMenu.class)
public abstract class PatternEncodingTermMenuMixin extends MEStorageMenu implements IMenuCraftingPacket {

    @Shadow(remap = false)
    @Final
    private RestrictedInputSlot blankPatternSlot;

    public PatternEncodingTermMenuMixin(MenuType<?> menuType, int id, Inventory ip, ITerminalHost host) {
        super(menuType, id, ip, host);
    }

    @Unique
    private IUpgradeInventory mae2a_getUpgrades() {
        return getHost().getUpgrades();
    }

    @Inject(method = "encode", at = @At(value = "INVOKE", target = "Lappeng/menu/slot/RestrictedInputSlot;set(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 1))
    private void hookEncode(CallbackInfo ci) {
        if (!mae2a_getUpgrades().isInstalled(MAE2AItems.PATTERN_REFILL_CARD))
            return;
        ItemStack blankPattern = this.blankPatternSlot.getItem();
        MEStorage inventory = getHost().getInventory();
        if (inventory != null) {
            if (blankPattern.getCount() > 32)
                return;
            MEStorage meStorage = this.getHost().getInventory();
            if (meStorage != null) {
                int amount = (int) meStorage.extract(AEItemKey.of(AEItems.BLANK_PATTERN),
                        (64 - blankPattern.getCount()), Actionable.MODULATE, getActionSource());
                if (blankPattern.isEmpty()) {
                    blankPatternSlot.set(new ItemStack(AEItems.BLANK_PATTERN, amount));
                } else {
                    blankPattern.grow(amount);
                }
            }
        }
    }
}
