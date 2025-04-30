package com.easterfg.mae2a.common.settings;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import appeng.client.gui.style.Blitter;

import com.easterfg.mae2a.MoreAE2Additions;

import lombok.Data;

@Data
public class PatternModifySetting {
    private int maxItemLimit = 64;
    private int maxFluidLimit = 64000;
    private int minItemLimit = 1;
    private int minFluidLimit = 1000;
    private boolean saveByProducts = false;
    private ModifyMode mode = ModifyMode.MULTIPLY;
    private boolean isProduct = true;
    private boolean limitMode = true;
    private int rate = 2;

    public enum ModifyMode {

        MULTIPLY(Component.translatable("gui.mae2a.multiply"), 0, 0),
        DIVIDE(Component.translatable("gui.mae2a.divide"), 16, 0);

        static final Blitter TEXTURE = Blitter.texture(MoreAE2Additions.id("textures/guis/action.png"), 32, 16);

        final Component tooltip;
        final int x;
        final int y;

        ModifyMode(Component tooltip, int x, int y) {
            this.tooltip = tooltip;
            this.x = x;
            this.y = y;
        }

        public Component tooltip() {
            return tooltip;
        }

        public Blitter icon() {
            return TEXTURE.copy().src(x, y, 16, 16);
        }
    }

    public void readFromNBT(CompoundTag data) {
        if (data.contains("setting", CompoundTag.TAG_COMPOUND)) {
            CompoundTag compound = data.getCompound("setting");
            maxItemLimit = compound.getInt("maxItemLimit");
            maxFluidLimit = compound.getInt("maxFluidLimit");
            minItemLimit = compound.getInt("minItemLimit");
            minFluidLimit = compound.getInt("minFluidLimit");
            saveByProducts = compound.getBoolean("saveByProducts");
            mode = ModifyMode.valueOf(compound.getString("mode"));
            isProduct = compound.getBoolean("isProduct");
            limitMode = compound.getBoolean("limitMode");
            rate = compound.getInt("rate");
        }
    }

    public void writeFromNBT(CompoundTag data) {
        data.putInt("maxItemLimit", maxItemLimit);
        data.putInt("maxFluidLimit", maxFluidLimit);
        data.putInt("minItemLimit", minItemLimit);
        data.putInt("minFluidLimit", minFluidLimit);
        data.putBoolean("saveByProducts", saveByProducts);
        data.putString("mode", mode.name());
        data.putBoolean("isProduct", isProduct);
        data.putBoolean("limitMode", limitMode);
        data.putInt("rate", rate);
    }
}
