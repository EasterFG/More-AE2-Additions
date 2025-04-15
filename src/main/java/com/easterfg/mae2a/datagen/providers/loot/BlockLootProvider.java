package com.easterfg.mae2a.datagen.providers.loot;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import com.easterfg.mae2a.MoreAE2Additions;

/**
 * @author EasterFG on 2025/4/13
 */
public class BlockLootProvider extends BlockLootSubProvider implements DataProvider {

    private final Path outputFolder;

    public BlockLootProvider(PackOutput output) {
        super(Set.of(), FeatureFlagSet.of());
        this.outputFolder = output.getOutputFolder();
    }

    @Override
    protected void generate() {
    }

    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        var futures = new ArrayList<CompletableFuture<?>>();

        for (var entry : BuiltInRegistries.BLOCK.entrySet()) {
            LootTable.Builder builder;
            if (entry.getKey().location().getNamespace().equals(MoreAE2Additions.MOD_ID)) {
                builder = defaultBuilder(entry.getValue());

                futures.add(DataProvider.saveStable(cache, toJson(builder),
                        getPath(outputFolder, entry.getKey().location())));
            }
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private Path getPath(Path root, ResourceLocation id) {
        return root.resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
    }

    public JsonElement toJson(LootTable.Builder builder) {
        return LootDataType.TABLE.parser().toJsonTree(builder.setParamSet(LootContextParamSets.BLOCK).build());
    }

    @Override
    public @NotNull String getName() {
        return MoreAE2Additions.MOD_ID + "Block Drops";
    }

    private LootTable.Builder defaultBuilder(Block block) {
        LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(block);
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
                .when(ExplosionCondition.survivesExplosion());

        return LootTable.lootTable().withPool(pool);
    }

}
