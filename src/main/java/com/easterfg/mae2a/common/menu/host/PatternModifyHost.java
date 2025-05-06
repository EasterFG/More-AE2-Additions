package com.easterfg.mae2a.common.menu.host;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.helpers.patternprovider.PatternProviderLogic;

import com.easterfg.mae2a.common.items.PatternModifyToolItem;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import com.easterfg.mae2a.util.PatternUtils;

import lombok.Getter;

/**
 * @author EasterFG on 2024/10/1
 */
public class PatternModifyHost extends ItemMenuHost {
    private final PatternModifySetting setting;

    @Getter
    @Nullable
    private PatternProviderLogic providerLogic;

    @Getter
    private List<ItemStack> patterns;

    public PatternModifyHost(Player player, @Nullable Integer slot, ItemStack itemStack, BlockPos clickPos) {
        super(player, slot, itemStack);
        setting = new PatternModifySetting();
        Vec3 hitPos = null;
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getOrCreateTag();
            this.setting.readFromNBT(tag);
            hitPos = PatternUtils.readVec3(tag.getCompound("hitPos"));
            tag.remove("hitPos");
        }

        if (hitPos != null && clickPos != null) {
            Level level = player.getCommandSenderWorld();
            providerLogic = PatternModifyToolItem.findPatternProvide(level, clickPos, hitPos);
            if (providerLogic != null) {
                patterns = PatternUtils.getProcessingPatterns(level, providerLogic, setting);
            }
        }
    }

    public PatternModifySetting getPatternModifySetting() {
        return setting;
    }

    public void saveSetting() {
        ItemStack itemStack = getItemStack();
        if (itemStack.isEmpty()) {
            return;
        }
        setting.writeFromNBT(itemStack.getOrCreateTagElement("setting"));
    }

    public void saveSetting(PatternModifySetting setting) {
        ItemStack stack = getItemStack();
        if (stack == null) {
            return;
        }
        setting.writeFromNBT(stack.getOrCreateTagElement("setting"));
    }

    public void setMode(PatternModifySetting.ModifyMode mode) {
        setting.setMode(mode);
        saveSetting();
    }

    public PatternModifySetting.ModifyMode getMode() {
        return setting.getMode();
    }

    public void setActionMode(boolean limit) {
        setting.setLimitMode(limit);
        saveSetting();
    }

    public boolean isLimitMode() {
        return setting.isLimitMode();
    }

}
