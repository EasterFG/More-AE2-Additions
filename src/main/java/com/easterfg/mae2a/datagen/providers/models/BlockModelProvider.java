package com.easterfg.mae2a.datagen.providers.models;

import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraftforge.common.data.ExistingFileHelper;

import appeng.api.orientation.BlockOrientation;
import appeng.block.crafting.PatternProviderBlock;
import appeng.datagen.providers.models.AE2BlockStateProvider;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.common.definition.MAE2ABlocks;

/**
 * @author EasterFG on 2025/4/3
 */
public class BlockModelProvider extends AE2BlockStateProvider {
    public BlockModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MoreAE2Additions.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        patternProvider();
    }

    public void patternProvider() {
        var def = MAE2ABlocks.PATTERN_PROVIDER_PLUS;
        var normalModel = cubeAll(def.asBlock());
        simpleBlockItem(def.asBlock(), normalModel);

        var orientedModel = models().getExistingFile(MoreAE2Additions.id("block/pattern_provider_plus_oriented"));
        var multiVariant = MultiVariantGenerator.multiVariant(def.asBlock(), Variant.variant())
                .with(PropertyDispatch.property(PatternProviderBlock.PUSH_DIRECTION).generate(pushDirection -> {
                    var forward = pushDirection.getDirection();
                    if (forward == null) {
                        return Variant.variant().with(VariantProperties.MODEL, normalModel.getLocation());
                    } else {
                        var orientation = BlockOrientation.get(forward);
                        return applyRotation(
                                Variant.variant().with(VariantProperties.MODEL, orientedModel.getLocation()),
                                // + 90 because the default model is oriented UP, while block orientation assumes NORTH
                                orientation.getAngleX() + 90,
                                orientation.getAngleY(),
                                0);
                    }
                }));
        this.registeredBlocks.put(def.asBlock(), () -> multiVariant.get().getAsJsonObject());
    }
}
