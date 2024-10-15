package com.easterfg.mae2a.common;

import com.easterfg.mae2a.MoreAE2Additions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ModCreativeModTabs {
    public static final String TUTORIAL_TAB_STRING = "item.mae2a.creative.tab";
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreAE2Additions.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("mae2a_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.PATTERN_MODIFY_TOOL.get()))
                    .title(Component.translatable(TUTORIAL_TAB_STRING))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.PATTERN_MODIFY_TOOL.get());
                        pOutput.accept(ModItems.INFINITE_DYES_CELL.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}