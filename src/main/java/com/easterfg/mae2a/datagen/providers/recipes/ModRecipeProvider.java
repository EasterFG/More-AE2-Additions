package com.easterfg.mae2a.datagen.providers.recipes;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;

import appeng.api.util.AEColor;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.common.definition.ModBlocks;
import com.easterfg.mae2a.common.definition.ModItems;
import com.easterfg.mae2a.common.definition.ModParts;

/**
 * @author EasterFG on 2025/4/2
 */
public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.PATTERN_MODIFY_TOOL)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("CDC")
                .define('A', AEItems.FLUIX_CRYSTAL)
                .define('B', AEItems.BLANK_PATTERN)
                .define('C', Items.DIAMOND)
                .define('D', AEItems.LOGIC_PROCESSOR)
                .unlockedBy("has_blank_pattern", RecipeProvider.has(AEItems.BLANK_PATTERN))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PATTERN_REFILL_CARD)
                .requires(AEItems.ADVANCED_CARD)
                .requires(AEItems.BLANK_PATTERN)
                .unlockedBy("has_blank_pattern", RecipeProvider.has(AEItems.BLANK_PATTERN))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FAKE_CRAFT_CARD)
                .requires(AEItems.ADVANCED_CARD)
                .requires(AEItems.CALCULATION_PROCESSOR)
                .requires(AEItems.CRAFTING_CARD)
                .unlockedBy("has_crafting_card", RecipeProvider.has(AEItems.CRAFTING_CARD))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.PATTERN_PROVIDER_PLUS)
                .requires(ModParts.PATTERN_PROVIDER_PLUS)
                .unlockedBy("has_cable_pattern_provider_plus", RecipeProvider.has(ModParts.PATTERN_PROVIDER_PLUS))
                .save(writer, MoreAE2Additions.id("pattern_provider_plus_alt"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModParts.PATTERN_PROVIDER_PLUS)
                .requires(ModBlocks.PATTERN_PROVIDER_PLUS)
                .unlockedBy("has_pattern_provider_plus", RecipeProvider.has(ModBlocks.PATTERN_PROVIDER_PLUS))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PATTERN_PROVIDER_PLUS)
                .pattern("ABA")
                .pattern(" C ")
                .define('A', AEItems.CAPACITY_CARD)
                .define('B', AEItems.ENGINEERING_PROCESSOR)
                .define('C', AEBlocks.PATTERN_PROVIDER)
                .unlockedBy("has_pattern_provider", RecipeProvider.has(AEBlocks.PATTERN_PROVIDER))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CABLE_PLACE_TOOL)
                .pattern("ADA")
                .pattern("LUR")
                .pattern("III")
                .define('A', AEParts.GLASS_CABLE.item(AEColor.TRANSPARENT))
                .define('D', Items.DIAMOND)
                .define('L', AEItems.LOGIC_PROCESSOR)
                .define('U', AEItems.CALCULATION_PROCESSOR)
                .define('R', AEItems.ENGINEERING_PROCESSOR)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_diamond", RecipeProvider.has(Items.DIAMOND))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PATTERN_PROVIDER_UPGRADE)
                .pattern("ABA")
                .pattern(" C ")
                .define('A', AEItems.CAPACITY_CARD)
                .define('B', AEItems.ENGINEERING_PROCESSOR)
                .define('C', Items.PAPER)
                .unlockedBy("has_pattern_provider", RecipeProvider.has(AEBlocks.PATTERN_PROVIDER))
                .save(writer);
    }
}
