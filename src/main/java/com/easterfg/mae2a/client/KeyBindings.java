package com.easterfg.mae2a.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.platform.InputConstants;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Mod;

import com.easterfg.mae2a.datagen.providers.LocalizationProvider;

/**
 * @author EasterFG on 2025/4/11
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindings {

    private static final List<KeyMapping> KEY_BINDINGS = new ArrayList<>();

    public static final KeyMapping OPEN_CABLE_UI = create(GLFW.GLFW_KEY_G,
            "key.mae2a.open_cable_ui");
    public static final KeyMapping PLACE_CABLE = create(GLFW.GLFW_KEY_P, "key.mae2a.place_cable");

    static KeyMapping create(int keyCode, String description) {
        var key = new KeyMapping(description, KeyConflictContext.IN_GAME, KeyModifier.CONTROL,
                InputConstants.Type.KEYSYM, keyCode,
                "key.category.mae2a");
        KEY_BINDINGS.add(key);
        return key;
    }

    public static List<KeyMapping> getKeyBindings() {
        return Collections.unmodifiableList(KEY_BINDINGS);
    }

    public static void init() {
        LocalizationProvider.add("key.category.mae2a", "更多AE2工具", "More AE2 Additions");
        LocalizationProvider.add("key.mae2a.open_cable_ui", "打开线缆放置工具配置", "Open Cable Place Tool Config");
        LocalizationProvider.add("key.mae2a.place_cable", "放置线缆", "Place Cable");
    }
}
