package com.easterfg.mae2a.common.menu.host;

import appeng.api.implementations.menuobjects.ItemMenuHost;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @author EasterFG on 2024/10/1
 */
public class PatternModifyHost extends ItemMenuHost {
    private final PatternModifySetting pms;

    private Player player;

    public PatternModifyHost(Player player, @Nullable Integer slot, ItemStack itemStack) {
        super(player, slot, itemStack);
        pms = new PatternModifySetting();
        if (itemStack.hasTag()) {
            this.pms.readFromNBT(itemStack.getOrCreateTag());
        }
    }

    public PatternModifySetting getPatternModifySetting() {
        return pms;
    }

    public void saveChange(PatternModifySetting pms) {
        CompoundTag tag = getItemStack().getOrCreateTag();
        pms.writeFromNBT(tag);
    }

}

