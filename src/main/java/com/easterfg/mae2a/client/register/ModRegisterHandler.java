package com.easterfg.mae2a.client.register;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.IForgeRegistry;

import com.easterfg.mae2a.api.definition.ItemDefinition;
import com.easterfg.mae2a.common.definition.*;

/**
 * @author EasterFG on 2025/4/3
 */
public class ModRegisterHandler {
    public static void initBlock(IForgeRegistry<Block> registry) {
        for (var block : MAE2ABlocks.getBlocks()) {
            registry.register(block.id(), block.asBlock());
        }
    }

    public static void initItem(IForgeRegistry<Item> registry) {
        for (ItemDefinition<?> item : MAE2AItems.getItems()) {
            registry.register(item.id(), item.asItem());
        }

        for (ItemDefinition<?> item : MAE2ABlocks.getBlocks()) {
            registry.register(item.id(), item.asItem());
        }
    }

    public static void initBlockEntity(IForgeRegistry<BlockEntityType<?>> registry) {
        for (var blockEntity : MAE2ABlockEntities.getBlockEntityTypes().entrySet()) {
            registry.register(blockEntity.getKey(), blockEntity.getValue());
        }
    }

    public static void init() {
        MAE2AItems.init();
        MAE2ABlocks.init();
        MAE2AParts.init();
    }
}
