package com.easterfg.mae2a.datagen.providers.langs;

import static com.easterfg.mae2a.datagen.providers.LocalizationProvider.add;

public class ConfigLanguage {
    public static void init() {
        add("config.mae2a.button_tip", "样板预览界面按钮倍率", "Pattern Preview Button Rate");
        add("config.mae2a.slot", "样板供应器Plus最大槽位", "Pattern Provider Plus Max Slot");
        add("config.mae2a.preview_color", "线缆放置工具预览颜色", "Cable Place Tool Preview Color");
        add("config.mae2a.allow_other_show_amount", "供应器显示样板主输出数量",
                "Provider display amount of pattern primary outputs");
        add("config.mae2a.pattern_upgrade_white_list", "样板供应器升级白名单", "Pattern Provide Upgrade White List");
    }
}
