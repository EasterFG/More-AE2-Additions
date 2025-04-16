package com.easterfg.mae2a.integration.wt;

import appeng.api.upgrades.Upgrades;

import com.easterfg.mae2a.common.definition.ModItems;

import de.mari_023.ae2wtlib.AE2wtlib;

/**
 * @author EasterFG on 2025/4/6
 */
public class WTCommonLoad {
    public static void init() {
        Upgrades.add(ModItems.PATTERN_REFILL_CARD, AE2wtlib.PATTERN_ENCODING_TERMINAL, 1);
        Upgrades.add(ModItems.PATTERN_REFILL_CARD, AE2wtlib.UNIVERSAL_TERMINAL, 1);
    }
}
