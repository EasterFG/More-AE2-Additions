package com.easterfg.mae2a.mixins.ae.part;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundTag;

import appeng.api.parts.IPartItem;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.helpers.IPatternTerminalLogicHost;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.parts.encoding.PatternEncodingTerminalPart;
import appeng.parts.reporting.AbstractTerminalPart;

/**
 * @author EasterFG on 2025/4/2
 */
@Mixin(PatternEncodingTerminalPart.class)
public abstract class PatternEncodingTerminalPartMixin extends AbstractTerminalPart
        implements IPatternTerminalLogicHost, IPatternTerminalMenuHost {

    @Unique
    private IUpgradeInventory mae2a_upgrades;

    public PatternEncodingTerminalPartMixin(IPartItem<?> partItem) {
        super(partItem);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initHook(IPartItem<?> partItem, CallbackInfo ci) {
        this.mae2a_upgrades = UpgradeInventories.forMachine(partItem, 1, this::mae2a_onUpgradesChanged);
    }

    @Inject(method = "writeToNBT", at = @At("HEAD"), remap = false)
    private void writeToNBTHook(CompoundTag data, CallbackInfo ci) {
        this.mae2a_upgrades.writeToNBT(data, "upgrades");
    }

    @Inject(method = "readFromNBT", at = @At("HEAD"), remap = false)
    private void readFromNBTHook(CompoundTag data, CallbackInfo ci) {
        this.mae2a_upgrades.readFromNBT(data, "upgrades");
    }

    @Unique
    private void mae2a_onUpgradesChanged() {
        this.getHost().markForSave();
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return mae2a_upgrades;
    }
}
