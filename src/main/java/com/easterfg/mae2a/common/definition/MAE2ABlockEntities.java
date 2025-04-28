package com.easterfg.mae2a.common.definition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.api.definition.BlockDefinition;
import com.easterfg.mae2a.common.block.PatternProviderPlusBlockEntity;

/**
 * @author EasterFG on 2025/4/3
 */
@SuppressWarnings("unused")
public class MAE2ABlockEntities {

    private static final Map<ResourceLocation, BlockEntityType<?>> BLOCK_ENTITY_TYPES = new HashMap<>();

    public static final BlockEntityType<PatternProviderPlusBlockEntity> PATTERN_PROVIDER_PLUS_BLOCK_ENTITY = create(
            "pattern_provider_plus",
            PatternProviderPlusBlockEntity.class,
            PatternProviderPlusBlockEntity::new,
            MAE2ABlocks.PATTERN_PROVIDER_PLUS);

    @SuppressWarnings("unchecked")
    @SafeVarargs
    private static <T extends AEBaseBlockEntity> BlockEntityType<T> create(String shortId,
            Class<T> entityClass,
            MAE2ABlockEntities.BlockEntityFactory<T> factory,
            BlockDefinition<? extends AEBaseEntityBlock<?>>... blockDefinitions) {
        Preconditions.checkArgument(blockDefinitions.length > 0);

        ResourceLocation id = MoreAE2Additions.id(shortId);

        var blocks = Arrays.stream(blockDefinitions)
                .map(BlockDefinition::asBlock)
                .toArray(AEBaseEntityBlock[]::new);

        AtomicReference<BlockEntityType<T>> typeHolder = new AtomicReference<>();
        BlockEntityType.BlockEntitySupplier<T> supplier = (blockPos, blockState) -> factory.create(typeHolder.get(),
                blockPos, blockState);
        var type = BlockEntityType.Builder.of(supplier, blocks).build(null);
        typeHolder.set(type); // Makes it available to the supplier used above
        BLOCK_ENTITY_TYPES.put(id, type);

        AEBaseBlockEntity.registerBlockEntityItem(type, blockDefinitions[0].asItem());

        for (var block : blocks) {
            AEBaseEntityBlock<T> baseBlock = (AEBaseEntityBlock<T>) block;
            baseBlock.setBlockEntity(entityClass, type, null, null);
        }

        return type;
    }

    public static Map<ResourceLocation, BlockEntityType<?>> getBlockEntityTypes() {
        return ImmutableMap.copyOf(BLOCK_ENTITY_TYPES);
    }

    @FunctionalInterface
    interface BlockEntityFactory<T extends AEBaseBlockEntity> {
        T create(BlockEntityType<T> type, BlockPos pos, BlockState state);
    }

}
