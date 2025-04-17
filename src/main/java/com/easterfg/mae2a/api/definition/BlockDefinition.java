package com.easterfg.mae2a.api.definition;

import java.util.Objects;

import com.google.common.base.Preconditions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * @author EasterFG on 2025/4/3
 */
public class BlockDefinition<T extends Block> extends ItemDefinition<BlockItem> {

    private final T block;

    public BlockDefinition(ResourceLocation id, T block, BlockItem item, String englishName, String chineseName) {
        super(id, item, englishName, chineseName);
        this.block = Objects.requireNonNull(block, "block");
    }

    public T asBlock() {
        return block;
    }

    @Override
    public final ItemStack stack(int stackSize) {
        Preconditions.checkArgument(stackSize > 0);

        return new ItemStack(block, stackSize);
    }
}
