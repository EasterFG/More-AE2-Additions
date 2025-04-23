package com.easterfg.mae2a.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import appeng.api.util.AEColor;

import com.easterfg.mae2a.common.menu.host.CablePlaceToolHost;

/**
 * @author EasterFG on 2025/4/9
 */
public final class NBTHelper {

    public static final String BIND_POS_ID = "bind_pos";
    public static final String SETTING_ID = "tool_setting";
    public static final String SETTING_COLOR_ID = "color";
    public static final String SETTING_CABLE_ID = "cable";
    public static final String SETTING_PICKER_ID = "picker_distance";
    public static final String SETTING_REPLACE_ID = "replace_mode";
    public static final String POS_LIST_ID = "pos_list";

    private NBTHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Nullable
    public static BlockPos getBlockPos(ItemStack stack, String name) {
        var tag = stack.getTag();
        return getBlockPos(tag, name);
    }

    @Nullable
    public static BlockPos getBlockPos(@Nullable CompoundTag tag, String name) {
        if (tag == null || !tag.contains(name)) {
            return null;
        }
        return BlockPos.of(tag.getLong(name));
    }

    public static void saveBlockPos(@NotNull ItemStack stack, @NotNull BlockPos pos, String name) {
        var tag = stack.getOrCreateTag();
        tag.putLong(name, pos.asLong());
    }

    public static void addBlock(ItemStack stack, String name, BlockPos pos) {
        var tag = stack.getOrCreateTag();
        ListTag list = tag.getList(name, Tag.TAG_LONG);
        var value = LongTag.valueOf(pos.asLong());
        list.add(value);
        tag.put(name, list);
    }

    public static void removeLastBlock(ItemStack stack, String name) {
        var tag = stack.getTag();
        if (tag == null)
            return;
        ListTag list = tag.getList(name, Tag.TAG_LONG);
        if (list.isEmpty())
            return;
        list.remove(list.size() - 1);
        tag.put(name, list);
    }

    public static List<BlockPos> getBlockList(CompoundTag tag, String name) {
        if (tag == null || !tag.contains(name, Tag.TAG_LIST))
            return new ArrayList<>();
        ListTag list = tag.getList(name, Tag.TAG_LONG);
        return list.stream().map(item -> {
            var value = (LongTag) item;
            return BlockPos.of(value.getAsLong());
        }).collect(Collectors.toList());
    }

    public static CablePlaceToolHost.Settings readSetting(ItemStack stack) {
        CompoundTag setting = stack.getTagElement(SETTING_ID);
        if (setting != null) {
            AEColor color = AEColor.TRANSPARENT;
            int cable = 0;
            int index = setting.getInt(SETTING_COLOR_ID);
            if (index > -1 && index < AEColor.values().length) {
                color = AEColor.values()[index];
            }

            index = setting.getInt(SETTING_CABLE_ID);
            if (index > -1 && index < CableType.values().length) {
                cable = index;
            }

            index = Math.max(1, setting.getInt(SETTING_PICKER_ID));

            boolean isReplace = setting.getBoolean(SETTING_REPLACE_ID);

            return new CablePlaceToolHost.Settings(color, cable, index, isReplace);
        }
        return new CablePlaceToolHost.Settings();
    }
}
