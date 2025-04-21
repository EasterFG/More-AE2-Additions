package com.easterfg.mae2a.datagen.providers.langs;

import static com.easterfg.mae2a.datagen.providers.LocalizationProvider.add;

/**
 * @author EasterFG on 2025/4/2
 */
public final class GuiLanguage {
    public static void init() {
        add("gui.mae2a.pattern_tool_setting", "样板工具设置 - %s", "Pattern Tool Configuration - %s");

        add("gui.mae2a.pattern_max_item_limit", "最大物品数量", "Max Item Quantity");
        add("gui.mae2a.pattern_max_fluid_limit", "最大流体数量(B)", "Max Fluid Volume(B)");

        add("gui.mae2a.pattern_min_item_limit", "最小物品数量", "Min Item Quantity");
        add("gui.mae2a.pattern_min_fluid_limit", "最小流体数量(B)", "Min Fluid Volume(B)");

        add("gui.mae2a.max_item_input_placeholder", "请输入最大物品数量", "Enter max item quantity");
        add("gui.mae2a.max_fluid_input_placeholder", "请输入最大流体数量", "Enter max fluid volume");
        add("gui.mae2a.min_item_input_placeholder", "请输入物品下限数量", "Set item lower limit");
        add("gui.mae2a.min_fluid_input_placeholder", "请输入流体下限数量", "Set fluid lower limit");

        add("gui.mae2a.multiply", "乘法模式", "Multiply Mode");
        add("gui.mae2a.divide", "除法模式", "Divide Mode");
        add("gui.mae2a.action_rate", "倍率模式", "Rate Mode");
        add("gui.mae2a.action_limit", "限制模式", "Limit Mode");
        add("gui.mae2a.target_product", "产物数量", "Output Count");
        add("gui.mae2a.target_material", "原料数量", "Input Count");

        add("gui.mae2a.action_switch_tip", "切换操作模式", "Switch Action Mode");
        add("gui.mae2a.target_tip", "切换限制模式目标", "Switch target for limit mode");

        add("gui.mae2a.pattern_rate", "单次操作倍率", "Batch Operation Rate");
        add("gui.mae2a.pattern_rate_multiply", "将产物和原料数量✖指定倍数", "Multiply products and ingredients by ?");
        add("gui.mae2a.pattern_rate_divide", "将产物和原料数量➗指定倍数, 无法整除部分将被跳过",
                "Divide products and ingredients by ?, remainders will be discarded");

        add("gui.mae2a.retain_by_products", "保留副产物", "Retain By-products");
        add("gui.mae2a.max_input_tip", "输入0来禁用，产物/原料数量不会高于设定上限", "Set 0 to disable - Input/Output won't exceed limit");
        add("gui.mae2a.min_input_tip", "输入0来禁用，产物/原料数量不会低于设定下限",
                "Enter 0 to disable - Input/Output quantity will not fall below the set lower limit");

        add("gui.mae2a.pattern_provider_plus", "ME样板供应器Plus", "ME Pattern Provider Plus");

        add("gui.mae2a.pattern_provider_plus.package_mode", "打包模式: %s", "Package Mode: %s");
        add("gui.mae2a.mode.off", "禁用", "Off");
        add("gui.mae2a.mode.on", "启用", "On");

        add("gui.mae2a.pattern_list", "样板处理预览", "Pattern Processing Preview");
        add("gui.mae2a.pattern_list.disable", "§c此样板已禁用", "§cThis pattern is disabled");
        add("gui.mae2a.pattern_list.info", "§7后续操作将跳过此样板", "§7Skipped during duplication process");
        add("gui.mae2a.pattern_list.cant", "§a✔ 此样板无需调整", "§a✔ Pattern requires no modification");
        add("gui.mae2a.pattern_list.cant_info", "§7当前样板已达成预设目标", "§Pattern already meets target parameters");
        add("gui.mae2a.pattern_list.disable_helper", "§7单击§c禁用§7此样板", "§7Click to §cdisable §7this pattern");
        add("gui.mae2a.pattern_list.enable_helper", "§7单击§a启用§7此样板", "§7Click to §aenable§7 this pattern");
        add("gui.mae2a.confirm", "确认", "Confirm");
        add("gui.mae2a.cancel", "取消", "Cancel");

        add("gui.mae2a.cable_place_tool", "线缆放置工具", "Cable Place Tool");
        add("gui.mae2a.color_picker", "颜色选择", "Color Picker");
        add("gui.mae2a.cable_picker", "线缆选择", "Cable Picker");
        add("gui.mae2a.picker.select_range", "§7选取范围: ", "§7Selection Range: ");
        add("gui.mae2a.picker.unit", "§7格", "§7Blocks");
        add("gui.mae2a.picker.replace", "替换模式", "Switch Replace Mode");
    }
}
