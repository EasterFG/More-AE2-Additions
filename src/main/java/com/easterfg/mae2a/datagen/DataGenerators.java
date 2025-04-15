package com.easterfg.mae2a.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.datagen.providers.LocalizationProvider;
import com.easterfg.mae2a.datagen.providers.loot.BlockLootProvider;
import com.easterfg.mae2a.datagen.providers.models.BlockModelProvider;
import com.easterfg.mae2a.datagen.providers.models.ItemModelProvider;
import com.easterfg.mae2a.datagen.providers.recipes.ModRecipeProvider;

/**
 * @author EasterFG on 2025/3/23
 */
@Mod.EventBusSubscriber(modid = MoreAE2Additions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new LocalizationProvider(generator));
        generator.addProvider(event.includeServer(), new ItemModelProvider(generator.getPackOutput(),
                event.getExistingFileHelper()));

        generator.addProvider(event.includeServer(), new BlockModelProvider(generator.getPackOutput(),
                event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new BlockLootProvider(generator.getPackOutput()));
    }
}
