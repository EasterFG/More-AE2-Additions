package com.easterfg.mae2a.datagen.providers.langs;

import static com.easterfg.mae2a.datagen.providers.LocalizationProvider.add;

/**
 * @author EasterFG on 2025/4/8
 */
public class MiscLanguage {
    public static void init() {
        add("tools.mae2a.many_patter_result", "本次操作了§6%s§r台机器的§6%s§r个样板",
                "§7Processed §6%s§r patterns across §6%s§r machines");
        add("tools.mae2a.one_patter_result", "本次操作了§6%s§r个样板", "§7Modified §6%s§r pattern instance");
        add("tools.mae2a.no_pattern", "§c这个样板供应器内没有样板", "§cNo patterns found in this Pattern Provider");

        add("tools.mae2a.bind_ae", "§a已成功绑定到ME网络", "§aConnected to ME Network");

        add("tools.mae2a.glass_cable", "ME玻璃线缆", "ME Glass Cable");
        add("tools.mae2a.smart_cable", "ME智能线缆", "ME Smart Cable");
        add("tools.mae2a.smart_dense_cable", "ME致密线缆", "ME Dense Smart Cable");
        add("tools.mae2a.covered_cable", "ME包层线缆", "ME Covered Cable");
        add("tools.mae2a.covered_dense_cable", "ME致密包层线缆", "ME Dense Covered Cable");
    }
}
