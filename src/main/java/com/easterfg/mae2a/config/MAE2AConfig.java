package com.easterfg.mae2a.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import com.easterfg.mae2a.MoreAE2Additions;

@Mod.EventBusSubscriber(modid = MoreAE2Additions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MAE2AConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<List<Integer>> CUSTOM_BUTTON = BUILDER
            .comment("Set up to 4 button multipliers/divide on the preview screen.")
            .translation("config.preview.button_tip")
            .define("preview.button", Lists.newArrayList(2, 16, 64, 100));

    private static final ForgeConfigSpec.IntValue PROVIDER_MAX_SLOT = BUILDER
            .comment("Set me provider plus max slot")
            .defineInRange("plus_max_slot", 54, 36, 576);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static List<Integer> buttonTimes;
    public static int plusMaxSlot;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        buttonTimes = CUSTOM_BUTTON.get().subList(0, 4);
        plusMaxSlot = PROVIDER_MAX_SLOT.get();
        if (plusMaxSlot % 9 != 0) {
            plusMaxSlot -= plusMaxSlot % 9;
        }
    }

}
