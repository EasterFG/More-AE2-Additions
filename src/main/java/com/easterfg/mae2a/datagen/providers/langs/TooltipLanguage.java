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
        add("tooltip.mae2a.pattern_modify_tool.2", "§7物品/流体的§b上限/下限§c仅对主产物生效§7，材料数量§a自动翻倍",
                "§7§bUpper/Lower limits§c only affect primary products§7. Material quantity will §aauto-double");
        add("tooltip.mae2a.pattern_modify_tool.3", "§a预览模式§7: 预览即将处理的全部样板 (§6右键§7样板供应器)",
                "§aPreview Mode§7: Preview all pending patterns (§6Right-click§7 Pattern Provider)");
        add("tooltip.mae2a.pattern_modify_tool.4", "§a方块模式§7: 翻倍供应器中的全部样板 (§6Shift右键§7样板供应器)",
                "§aBlock Mode§7: Double all patterns in provider (§6Shift + Right-click§7 Pattern Provider)");
        add("tooltip.mae2a.pattern_modify_tool.5", "§a网络模式§7: 翻倍网络中的全部样板 (§6Shift右键§7样板编码终端)",
                "§aNetwork Mode§7: Double all patterns in network (§6Shift + Right-click§7 Pattern Encoding Terminal)");
        add("tooltip.mae2a.pattern_modify_tool.6", "§c注意: 翻倍网络中的全部样板将覆盖全部样板，请谨慎使用",
                "§cWarning: Doubling all patterns in the network will overwrite all existing patterns. Use with caution");
        add("tooltip.mae2a.pattern_modify_tool.many_result", "本次操作了§6%s§r台机器的§6%s§r个样板",
                "§7Modified §6%s§r patterns across §6%s§r machines");
        add("tooltip.mae2a.pattern_modify_tool.one_result", "本次操作了§6%s§r个样板", "§7Modified §6%s§r pattern");
        add("tooltip.mae2a.infinite_dyes_cell", "这是一个无限染料元件", "This is an infinite dyes cell");
        // fast place
        add("tooltip.mae2a.cable_place_tool.1", "§7右键空气清除起始坐标", "§7§eRight-click air§7 to clear start position");
        add("tooltip.mae2a.cable_place_tool.2", "§7右键§bME无线访问点§7绑定ME网络",
                "§7Right-click §bME Wireless Access Point §7to bind ME network");
        add("tooltip.mae2a.cable_place_tool.bind_ae", "§a已连接至ME网络 §8[§7%s, %s, %s§8]",
                "§aConnected to ME Network §8[§7%s, %s, %s§8]");
        add("tooltip.mae2a.cable_place_tool.not_bind_ae", "§c未连接至ME网络", "§cNot connected to ME Network");
        add("tooltip.mae2a.cable_place_tool.start_pos", "§a起始坐标已设定 §8[§7%s, %s, %s§8]",
                "§aStart position set §8[§7%s, %s, %s§8]");
        add("tooltip.mae2a.cable_place_tool.not_start_pos", "§c未设定起始坐标", "§cStart position not configured");
        add("tooltip.mae2a.cable_place_tool.cable", "§7线缆类型: %s", "§Cable Type: %s");
        add("tooltip.mae2a.cable_place_tool.color", "§7线缆颜色: %s", "§Cable Color: %s");
        add("tooltip.mae2a.cable_place_tool.open_ui", "%s§7 打开配置界面", "%s§7 Open Configuration GUI");
    }
}
