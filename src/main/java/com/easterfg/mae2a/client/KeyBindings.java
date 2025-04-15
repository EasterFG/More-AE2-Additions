package com.easterfg.mae2a.client;

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

    public static final String KEY_CATEGORY_EXAMPLE_MOD = "key.category.mae2a";
    // 我们添加一个按键的描述，是可以被语言化文件处理的。
    public static final String KEY_DRINK_WATER = "key.mae2a.open_cable_ui";

    public static final KeyMapping OPEN_CABLE_UI = new KeyMapping(KEY_CATEGORY_EXAMPLE_MOD, KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_DRINK_WATER);

    public void init() {
        LocalizationProvider.add("key.category.mae2a", "更多AE2工具", "More AE2 Additions");
        LocalizationProvider.add("key.mae2a.open_cable_ui", "打开线缆放置工具配置", "Open Cable Place Tool Config");
    }
}
