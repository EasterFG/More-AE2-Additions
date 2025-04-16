package com.easterfg.mae2a.mixins.ae.craft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import appeng.api.crafting.IPatternDetails;
import appeng.crafting.execution.CraftingCpuLogic;
import appeng.crafting.execution.ExecutingCraftingJob;

import com.easterfg.mae2a.integration.mixin.ICrafting;

/**
 * @author EasterFG on 2025/4/4
 */
@Mixin(CraftingCpuLogic.class)
public abstract class CraftingCpuLogicMixin implements ICrafting {

    @Shadow(remap = false)
    private ExecutingCraftingJob job;

    @Override
    public boolean mae2a_hasTask(IPatternDetails details) {
        if (job == null) {
            return false;
        }
        return ((ICrafting) (job)).mae2a_hasTask(details);
    }
}
