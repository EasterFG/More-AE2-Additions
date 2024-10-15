package com.easterfg.mae2a.common.settings;

import lombok.Data;
import net.minecraft.nbt.CompoundTag;

/**
 * @author EasterFG on 2024/10/2
 */
@Data
public class PatternModifySetting {
    private int maxItemLimit = 64;
    private int maxFluidLimit = 64000;
    private int minItemLimit = 1;
    private int minFluidLimit = 1000;
    private boolean saveByProducts = false;
    /**
     * 0 is Multiply, 1 is Divide
     */
    private int mode = 0;

    public void readFromNBT(CompoundTag data) {
        if (data.contains("setting", CompoundTag.TAG_COMPOUND)) {
            CompoundTag compound = data.getCompound("setting");
            maxItemLimit = compound.getInt("maxItemLimit");
            maxFluidLimit = compound.getInt("maxFluidLimit");
            minItemLimit = compound.getInt("minItemLimit");
            minFluidLimit = compound.getInt("minFluidLimit");
            saveByProducts = compound.getBoolean("saveByProducts");
            mode = compound.getInt("mode");
        }
    }

    public void writeFromNBT(CompoundTag data) {
        var tag = new CompoundTag();
        tag.putInt("maxItemLimit", maxItemLimit);
        tag.putInt("maxFluidLimit", maxFluidLimit);
        tag.putInt("minItemLimit", minItemLimit);
        tag.putInt("minFluidLimit", minFluidLimit);
        tag.putBoolean("saveByProducts", saveByProducts);
        tag.putInt("mode", mode);
        data.put("setting", tag);
    }
}
