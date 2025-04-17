package com.easterfg.mae2a.common.settings;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import lombok.Data;

@Data
public class PatternModifySetting {
    private int maxItemLimit = 64;
    private int maxFluidLimit = 64000;
    private int minItemLimit = 1;
    private int minFluidLimit = 1000;
    private boolean saveByProducts = false;
    private ModifyMode mode = ModifyMode.MULTIPLY;

    public enum ModifyMode {
        MULTIPLY(Component.translatable("gui.mae2a.multiply"), Items.STONE.getDefaultInstance()),
        DIVIDE(Component.translatable("gui.mae2a.divide"), Items.SAND.getDefaultInstance());

        final Component tooltip;
        final ItemStack icon;

        ModifyMode(Component tooltip, ItemStack icon) {
            this.tooltip = tooltip;
            this.icon = icon;
        }

        public Component tooltip() {
            return tooltip;
        }

        public ItemStack icon() {
            return icon;
        }

        public static ModifyMode valueOf(int o) {
            if (o < 0 || o > values().length)
                throw new IllegalArgumentException("Invalid mode");
            return values()[o];
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
        }
    }

    public void writeFromNBT(CompoundTag data) {
        data.putInt("maxItemLimit", maxItemLimit);
        data.putInt("maxFluidLimit", maxFluidLimit);
        data.putInt("minItemLimit", minItemLimit);
        data.putInt("minFluidLimit", minFluidLimit);
        data.putBoolean("saveByProducts", saveByProducts);
        data.putString("mode", mode.name());
    }
}
