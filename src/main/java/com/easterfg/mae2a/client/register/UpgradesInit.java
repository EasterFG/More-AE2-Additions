package com.easterfg.mae2a.client.register;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEParts;

import com.easterfg.mae2a.common.definition.ModBlocks;
import com.easterfg.mae2a.common.definition.ModItems;
import com.easterfg.mae2a.common.definition.ModParts;

/**
 * @author EasterFG on 2025/4/2
 */
public class UpgradesInit {
    public static void init() {
        Upgrades.add(ModItems.PATTERN_REFILL_CARD, AEParts.PATTERN_ENCODING_TERMINAL, 1);
        Upgrades.add(ModItems.FAKE_CRAFT_CARD, ModParts.PATTERN_PROVIDER_PLUS, 1, "gui.mae2a.pattern_provider_plus");
        Upgrades.add(ModItems.FAKE_CRAFT_CARD, ModBlocks.PATTERN_PROVIDER_PLUS, 1, "gui.mae2a.pattern_provider_plus");
    }

}
