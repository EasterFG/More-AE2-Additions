package com.easterfg.mae2a.api.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import appeng.api.inventories.InternalInventory;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.menu.slot.AppEngSlot;

import lombok.Getter;
import lombok.Setter;

/**
 * @author EasterFG on 2025/4/7
 */
@Getter
@Setter
public class PreviewSlot extends AppEngSlot {

    public enum Status {
        ENABLE,
        DISABLE
    }

    private Status status = Status.ENABLE;

    public PreviewSlot(InternalInventory inv, int invSlot) {
        super(inv, invSlot);
    }

    public boolean isEnable() {
        return status == Status.ENABLE;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public ItemStack getDisplayStack() {
        if (isRemote()) {
            final ItemStack is = super.getDisplayStack();
            if (!is.isEmpty() && is.getItem() instanceof EncodedPatternItem iep) {
                final ItemStack out = iep.getOutput(is);
                if (!out.isEmpty()) {
                    return out;
                }
            }
        }
        return super.getDisplayStack();
    }
}
