package com.easterfg.mae2a.common.menu.host;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.api.util.AEColor;

import com.easterfg.mae2a.util.NBTHelper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author EasterFG on 2025/4/10
 */
//@Getter
public class CablePlaceToolHost extends ItemMenuHost {

    private final Settings settings;

    public CablePlaceToolHost(Player player, @Nullable Integer slot, ItemStack itemStack) {
        super(player, slot, itemStack);
        this.settings = NBTHelper.readSetting(itemStack);
    }

    public void setColor(@NotNull AEColor color) {
        settings.color = color;
        saveSetting();
    }

    public void setCable(int cable) {
        settings.cable = cable;
        saveSetting();
    }

    public void setPicker(int picker) {
        settings.picker = picker;
        saveSetting();
    }

    public void setReplace(boolean replace) {
        settings.setReplace(replace);
        saveSetting();
    }

    public AEColor getColor() {
        return settings.getColor();
    }

    public int getCable() {
        return settings.getCable();
    }

    public int getPicker() {
        return settings.getPicker();
    }

    @Data
    @AllArgsConstructor
    public static class Settings {
        private AEColor color;
        private int cable;
        private int picker;
        private boolean replace;

        public Settings() {
            this.color = AEColor.TRANSPARENT;
            this.cable = 0;
            this.picker = 1;
            this.replace = false;
        }

    }

    protected void saveSetting() {
        var stack = getItemStack();
        var setting = stack.getOrCreateTagElement(NBTHelper.SETTING_ID);
        if (settings.getColor() != null) {
            setting.putInt(NBTHelper.SETTING_COLOR_ID, settings.getColor().ordinal());
        }

        if (settings.getCable() != -1) {
            setting.putInt(NBTHelper.SETTING_CABLE_ID, settings.getCable());
        }

        if (settings.getPicker() > 0 && settings.getPicker() <= 64) {
            setting.putInt(NBTHelper.SETTING_PICKER_ID, settings.getPicker());
        }

        setting.putBoolean(NBTHelper.SETTING_REPLACE_ID, settings.isReplace());
    }
}
