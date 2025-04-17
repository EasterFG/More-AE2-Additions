package com.easterfg.mae2a.api.definition;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import lombok.Getter;

/**
 * @author EasterFG on 2025/3/23
 */
public class ItemDefinition<T extends Item> implements ItemLike {

    private final ResourceLocation id;

    @Getter
    private final String englishName;

    @Getter
    private final String chineseName;

    private final T item;

    public ItemDefinition(ResourceLocation id, T item, String englishName, String chineseName) {
        Objects.requireNonNull(id, "id");
        this.id = id;
        this.item = item;
        this.englishName = englishName;
        this.chineseName = chineseName;
    }

    public ItemStack stack(int size) {
        return new ItemStack(item, size);
    }

    @Override
    public @NotNull Item asItem() {
        return item;
    }

    public final ResourceLocation id() {
        return id;
    }

}
