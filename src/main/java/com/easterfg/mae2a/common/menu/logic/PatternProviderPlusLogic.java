package com.easterfg.mae2a.common.menu.logic;

import com.google.common.collect.ImmutableSet;

import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.IGrid;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.me.cluster.implementations.CraftingCPUCluster;

import com.easterfg.mae2a.common.definition.ModItems;
import com.easterfg.mae2a.common.menu.host.PatternProviderPlusLoginHost;
import com.easterfg.mae2a.integration.mixin.ICrafting;

/**
 * @author EasterFG on 2025/4/3
 */
public class PatternProviderPlusLogic extends PatternProviderLogic {

    private final PatternProviderPlusLoginHost host;
    private final IManagedGridNode mainNode;

    public PatternProviderPlusLogic(IManagedGridNode mainNode, PatternProviderLogicHost host,
            int patternInventorySize) {
        super(mainNode, host, patternInventorySize);
        this.host = (PatternProviderPlusLoginHost) host;
        this.mainNode = mainNode;
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        if (!(patternDetails instanceof AEProcessingPattern) ||
                !host.getUpgrades().isInstalled(ModItems.FAKE_CRAFT_CARD)) {
            return super.pushPattern(patternDetails, inputHolder);
        }
        var result = super.pushPattern(patternDetails, inputHolder);
        if (result) {
            cancel(patternDetails);
        }
        return result;
    }

    protected void cancel(IPatternDetails details) {
        IGrid grid = mainNode.getGrid();
        if (grid != null) {
            ImmutableSet<ICraftingCPU> cpus = grid.getCraftingService().getCpus();
            for (ICraftingCPU cpu : cpus) {
                if (!cpu.isBusy())
                    continue;
                if (cpu instanceof CraftingCPUCluster cluster) {
                    boolean hasTask = ((ICrafting) cluster.craftingLogic).mae2a_hasTask(details);
                    if (hasTask) {
                        cpu.cancelJob();
                        return;
                    }
                }
            }
        }
    }
}
