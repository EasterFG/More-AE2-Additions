package com.easterfg.mae2a.common.items;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.items.parts.PartItem;
import appeng.parts.crafting.PatternProviderPart;

import com.easterfg.mae2a.common.definition.ModBlockEntities;
import com.easterfg.mae2a.common.definition.ModBlocks;
import com.easterfg.mae2a.common.definition.ModParts;

public class ItemPatternProviderUpgrade extends Item {
    public ItemPatternProviderUpgrade(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        var level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        var te = level.getBlockEntity(clickedPos);
        if (te == null) {
            return InteractionResult.PASS;
        }
        var ctx = new BlockPlaceContext(context);
        if (te instanceof PatternProviderBlockEntity tile && tile.getTerminalPatternInventory().size() < 54) {
            var origin = level.getBlockState(clickedPos);
            var state = ModBlocks.PATTERN_PROVIDER_PLUS.asBlock().getStateForPlacement(ctx);
            if (state == null) {
                return InteractionResult.PASS;
            }
            for (var entry : origin.getValues().entrySet()) {
                var key = entry.getKey();
                var value = entry.getValue();
                try {
                    if (state.hasProperty(key)) {
                        state = state.<Comparable, Comparable>setValue((Property) key, value);
                    }
                } catch (Exception ignore) {
                }
            }
            BlockEntity be = ModBlockEntities.PATTERN_PROVIDER_PLUS_BLOCK_ENTITY.create(clickedPos, state);
            replace(level, clickedPos, te, be, state);
            context.getItemInHand().shrink(1);
            return InteractionResult.CONSUME;
        } else if (te instanceof CableBusBlockEntity cable) {
            var hit = context.getClickLocation();
            Vec3 target = new Vec3(hit.x - clickedPos.getX(), hit.y - clickedPos.getY(), hit.z - clickedPos.getZ());
            var part = cable.getCableBus().selectPartLocal(target).part;
            if (part instanceof PatternProviderPart pp && pp.getTerminalPatternInventory().size() < 54) {
                var side = pp.getSide();
                var content = new CompoundTag();

                PartItem<?> item = (PartItem<?>) ModParts.PATTERN_PROVIDER_PLUS.asItem();
                pp.writeToNBT(content);
                var place = cable.replacePart(item, side, context.getPlayer(), null);
                if (place != null) {
                    place.readFromNBT(content);
                }
                context.getItemInHand().shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    private void replace(Level level, BlockPos pos, BlockEntity oldTile, BlockEntity newTile, BlockState newBlock) {
        CompoundTag content = oldTile.saveWithFullMetadata();
        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);
        level.setBlockAndUpdate(pos, newBlock);
        level.setBlockEntity(newTile);
        newTile.load(content);
        if (newTile instanceof AEBaseBlockEntity aeTile) {
            aeTile.markForUpdate();
        } else {
            newTile.setChanged();
        }
    }
}
