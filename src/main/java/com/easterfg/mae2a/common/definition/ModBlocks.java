package com.easterfg.mae2a.common.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import appeng.block.AEBaseBlock;
import appeng.block.AEBaseBlockItem;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.api.MainCreativeMod;
import com.easterfg.mae2a.api.definition.BlockDefinition;
import com.easterfg.mae2a.common.block.PatternProviderPlusBlock;

/**
 * @author EasterFG on 2025/4/3
 */
public class ModBlocks {

    private static final List<BlockDefinition<?>> BLOCKS = new ArrayList<>();

    public static final BlockDefinition<PatternProviderPlusBlock> PATTERN_PROVIDER_PLUS = block("Pattern Provider Plus",
            "样板供应器 Plus",
            MoreAE2Additions.id("pattern_provider_plus"),
            PatternProviderPlusBlock::new, null);

    private static <T extends Block> BlockDefinition<T> block(
            String en,
            String cn,
            ResourceLocation id,
            Supplier<T> blockSupplier,
            @Nullable BiFunction<Block, Item.Properties, BlockItem> itemFactory) {

        // Create block and matching item
        T block = blockSupplier.get();

        Item.Properties itemProperties = new Item.Properties();

        BlockItem item;
        if (itemFactory != null) {
            item = itemFactory.apply(block, itemProperties);
            if (item == null) {
                throw new IllegalArgumentException("BlockItem factory for " + id + " returned null");
            }
        } else if (block instanceof AEBaseBlock) {
            item = new AEBaseBlockItem(block, itemProperties);
        } else {
            item = new BlockItem(block, itemProperties);
        }

        BlockDefinition<T> definition = new BlockDefinition<>(id, block, item, en, cn);
        MainCreativeMod.add(definition);

        BLOCKS.add(definition);

        return definition;
    }

    public static List<BlockDefinition<?>> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }

    public static void init() {
    }
}
