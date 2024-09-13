package com.easterfg.pte;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

/**
 * @author EasterFG on 2024/9/13
 */
@Mod(PatternTerminalExtendedMod.MOD_ID)
public class PatternTerminalExtendedMod {

    public static final String MOD_ID = "pattern_extended";
    public static final String NAME = "Pattern Terminal Extended";

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public PatternTerminalExtendedMod() {
    }
}
