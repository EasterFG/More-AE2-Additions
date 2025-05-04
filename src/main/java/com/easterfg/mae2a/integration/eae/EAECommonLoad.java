package com.easterfg.mae2a.integration.eae;

import com.glodblock.github.extendedae.common.items.ItemMEPackingTape;

import com.easterfg.mae2a.MoreAE2Additions;

public class EAECommonLoad {

    public static void init() {
        ItemMEPackingTape.registerPackableDevice(MoreAE2Additions.id("pattern_provider_plus"));
        ItemMEPackingTape.registerPackableDevice(MoreAE2Additions.id("cable_pattern_provider_plus"));
    }
}
