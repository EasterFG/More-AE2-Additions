package com.easterfg.mae2a.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import com.easterfg.mae2a.MoreAE2Additions;

@Mod.EventBusSubscriber(modid = MoreAE2Additions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MAE2AConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue PROVIDER_MAX_SLOT = BUILDER
            .comment("Set Provider Plus max slot, must be a multiple of 9")
            .translation("config.mae2a.slot")
            .worldRestart()
            .defineInRange("plus_max_slot", 54, 36, 576);

    private static final ForgeConfigSpec.ConfigValue<String> PREVIEW_BOX_COLOR = BUILDER
            .comment("Set the color of the preview box")
            .translation("config.mae2a.preview_color")
            .define("preview.color", "FF0EBEFF");

    private static final ForgeConfigSpec.BooleanValue ALLOW_OTHER_PROVIDE_SHOW_AMOUNT = BUILDER
            .comment("Allow other pattern provide to show amount on the screen")
            .translation("config.mae2a.allow_other_show_amount")
            .define("allow_other_show_amount", false);

    public static Integer[] buttonTimes;
    public static int boxColor;
    public static int plusMaxSlot;
    public static boolean showAmount;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        buttonTimes = PreviewConfig.getValues();
        boxColor = toColor(PREVIEW_BOX_COLOR.get());
        plusMaxSlot = PROVIDER_MAX_SLOT.get();
        showAmount = ALLOW_OTHER_PROVIDE_SHOW_AMOUNT.get();
        if (plusMaxSlot % 9 != 0) {
            plusMaxSlot -= plusMaxSlot % 9;
        }
    }

    private static int toColor(String color) {
        try {
            return (int) Long.parseLong(color, 16);
        } catch (NumberFormatException ignored) {
        }
        return 0xFFE6194B;
    }

    public static ForgeConfigSpec build() {
        PreviewConfig.init(BUILDER);
        return BUILDER.build();
    }

    private static class PreviewConfig {
        private static final List<ForgeConfigSpec.IntValue> values = new ArrayList<>();

        public static void init(ForgeConfigSpec.Builder builder) {
            builder.push("preview")
                    .comment("Set up to 4 button multipliers/divide on the preview screen.")
                    .translation("config.mae2a.button_tip");
            values.add(builder.defineInRange("button_0", 2, 2, 65536));
            values.add(builder.defineInRange("button_1", 16, 2, 65536));
            values.add(builder.defineInRange("button_2", 64, 2, 65536));
            values.add(builder.defineInRange("button_3", 100, 2, 65536));
            builder.pop();
        }

        public static Integer[] getValues() {
            return values.stream().map(ForgeConfigSpec.IntValue::get).toArray(Integer[]::new);
        }
    }
}
