package com.easterfg.mae2a.datagen.providers.langs;

import static com.easterfg.mae2a.datagen.providers.LocalizationProvider.add;

/**
 * @author EasterFG on 2025/4/1
 */
public final class TooltipLanguage {
    public static void init() {
        add("tooltip.mae2a.creative.tab", "更多AE2工具", "More AE2 Additions");
        add("tooltip.mae2a.pattern_modify_tool.1", "§7一个快捷修改样板的工具，§e右键空气§7打开设置菜单",
                "§7A tool for quick pattern modification. §eRight-click air§7 to open settings");
        add("tooltip.mae2a.pattern_modify_tool.limit",
                "§b限制模式§r: §6将产物/原料调整至目标数量§r，§a自动平衡材料配比",
                "§bLimit Mode§r: §6Adjusts Inputs/Outputs to target quantity§r, §aauto-balances materials");
        add("tooltip.mae2a.pattern_modify_tool.rate",
                "§b倍率模式§r: §6直接应用乘/除运算§r，§c除法仅处理整除配方",
                "§bRatio Mode§r: §6Applies multiplier/divisor directly§r, §crequires exact division§r");
        add("tooltip.mae2a.pattern_modify_tool.preview", "§a预览模式§7: 预览即将处理的全部样板 (§6右键§7样板供应器)",
                "§aPreview Mode§7: Preview all pending patterns (§6Right-click§7 Pattern Provider)");
        add("tooltip.mae2a.pattern_modify_tool.block", "§a方块模式§7: 翻倍供应器中的全部样板 (§6Shift右键§7样板供应器)",
                "§aBlock Mode§7: Double all patterns in provider (§6Shift + Right-click§7 Pattern Provider)");
        add("tooltip.mae2a.pattern_modify_tool.network", "§a网络模式§7: 翻倍网络中的全部样板 (§6Shift右键§7样板编码终端)",
                "§aNetwork Mode§7: Double all patterns in network (§6Shift + Right-click§7 Pattern Encoding Terminal)");
        add("tooltip.mae2a.pattern_modify_tool.warring", "§c注意: 翻倍网络中的全部样板将覆盖全部样板，请谨慎使用",
                "§cWarning: Doubling all patterns in the network will overwrite all existing patterns. Use with caution");
        add("tooltip.mae2a.pattern_modify_tool.many_result", "本次操作了§6%s§r台机器的§6%s§r个样板",
                "§7Modified §6%s§r patterns across §6%s§r machines");
        add("tooltip.mae2a.pattern_modify_tool.one_result", "本次操作了§6%s§r个样板", "§7Modified §6%s§r pattern");
        add("tooltip.mae2a.infinite_dyes_cell", "这是一个无限染料元件", "This is an infinite dyes cell");
        // fast place
        add("tooltip.mae2a.fast_place_tool.select_tip", "§6右键§7将坐标添加至合集",
                "§6Right-click§7 to add coordinates to the collection");
        add("tooltip.mae2a.fast_place_tool.undo_select_tip", "§6Shift右键空气§7撤销最近一次添加",
                "§6Shift+Right-click air§7 to undo the last addition");
        add("tooltip.mae2a.cable_place_tool.bind_tip", "§7右键§bME无线访问点§7绑定ME网络",
                "§7Right-click §bME Wireless Access Point §7to bind ME network");
        add("tooltip.mae2a.cable_place_tool.bind_ae", "§a已连接至ME网络 §8[§7%s, %s, %s§8]",
                "§aConnected to ME Network §8[§7%s, %s, %s§8]");
        add("tooltip.mae2a.cable_place_tool.not_bind_ae", "§c未连接至ME网络", "§cNot connected to ME Network");
        add("tooltip.mae2a.cable_place_tool.cable", "§7线缆类型: %s", "§cCable Type: %s");
        add("tooltip.mae2a.cable_place_tool.color", "§7线缆颜色: %s", "§cCable Color: %s");
        add("tooltip.mae2a.cable_place_tool.open_ui", "%s§7 打开配置界面", "%s§7 Open Configuration GUI");
        add("tooltip.mae2a.cable_place_tool.place_cable", "%s§7 放置线缆", "%s§7 Place the cable");
    }
}
