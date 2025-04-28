package com.easterfg.mae2a.client.register;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEParts;

import com.easterfg.mae2a.common.definition.MAE2ABlocks;
import com.easterfg.mae2a.common.definition.MAE2AItems;
import com.easterfg.mae2a.common.definition.MAE2AParts;

/**
 * @author EasterFG on 2025/4/2
 */
public class UpgradesInit {
    public static void init() {
        Upgrades.add(MAE2AItems.PATTERN_REFILL_CARD, AEParts.PATTERN_ENCODING_TERMINAL, 1);
        Upgrades.add(MAE2AItems.FAKE_CRAFT_CARD, MAE2AParts.PATTERN_PROVIDER_PLUS, 1,
                "gui.mae2a.pattern_provider_plus");
        Upgrades.add(MAE2AItems.FAKE_CRAFT_CARD, MAE2ABlocks.PATTERN_PROVIDER_PLUS, 1,
                "gui.mae2a.pattern_provider_plus");
    }

}
