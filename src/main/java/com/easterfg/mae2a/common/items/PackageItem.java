package com.easterfg.mae2a.common.items;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import com.easterfg.mae2a.common.definition.ModItems;

/**
 * @author EasterFG on 2025/3/31
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PackageItem extends Item {
    public PackageItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        var player = context.getPlayer();
        if (player == null || !stack.hasTag()) {
            return InteractionResult.PASS;
        }

        getStorageItem(stack).forEach(item -> {
            ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY() + 0.1, player.getZ(), item);
            level.addFreshEntity(itemEntity);
        });
        return InteractionResult.SUCCESS;
    }

    public static List<ItemStack> getStorageItem(ItemStack itemStack) {
        if (!itemStack.hasTag()) {
            return Collections.emptyList();
        }
        var nbt = itemStack.getTag();
        if (nbt != null && nbt.contains("Items", Tag.TAG_LIST)) {
            ListTag items = nbt.getList("Items", Tag.TAG_COMPOUND);
            return items.stream()
                    .map(tag -> ItemStack.of((CompoundTag) tag))
                    .toList();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        if (!itemStack.hasTag()) {
            return Optional.empty();
        }
        return Optional.of(new PackageItemTooltipComponent(getStorageItem(itemStack), 1, false));
    }

    public static ItemStack create(@NotNull List<ItemStack> items) {
        if (items.isEmpty()) {
            return ItemStack.EMPTY;
        }
        var item = new ItemStack(ModItems.PACKAGES_ITEM, 1);
        var tag = new ListTag();
        for (ItemStack stack : items) {
            CompoundTag itemTag = new CompoundTag();
            stack.save(itemTag);
            tag.add(itemTag);
        }
        item.getOrCreateTag().put("Items", tag);
        return item;
    }

}
