package com.easterfg.mae2a.util;

import net.minecraftforge.fml.ModList;

/**
 * @author EasterFG on 2025/4/6
 */
public final class Platform {
    private Platform() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

}
