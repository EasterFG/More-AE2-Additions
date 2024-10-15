package com.easterfg.mae2a.client.screen;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AECheckbox;
import appeng.client.gui.widgets.AETextField;
import com.easterfg.mae2a.common.menu.PatternModifyMenu;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author EasterFG on 2024/10/1
 */
public class PatternModifyScreen extends AEBaseScreen<PatternModifyMenu> {

    private final AETextField itemInput;
    private final AETextField fluidInput;
    private final AECheckbox saveByProducts;
    private final AECheckbox actionSwitchMultiply;
    private final AECheckbox actionSwitchDivide;
    private final PatternModifySetting pms;

    // 匹配整数正则
    private static final Pattern INTEGER_REGEX = Pattern.compile("\\d+");
    // 匹配最多三位浮点数正则
    private static final Pattern FLOAT_REGEX = Pattern.compile("\\d+(\\.\\d{0,3})?");
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.###");

    public PatternModifyScreen(PatternModifyMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        pms = menu.getHost().getPatternModifySetting();
        itemInput = widgets.addTextField("max_item_input");
        itemInput.setFilter(str -> str.isEmpty() || INTEGER_REGEX.matcher(str).matches());
        itemInput.setMaxLength(15);
        itemInput.setResponder(s -> this.onUpdate(s, 0));
        fluidInput = widgets.addTextField("max_fluid_input");
        fluidInput.setFilter(str -> str.isEmpty() || FLOAT_REGEX.matcher(str).matches());
        fluidInput.setMaxLength(15);
        fluidInput.setResponder(s -> this.onUpdate(s, 1));
        saveByProducts = widgets.addCheckbox("save_by_products", Component.translatable("gui.mae2a.retain_by_products"), () -> {
            pms.setSaveByProducts(!pms.isSaveByProducts());
            saveChange();
        });
        actionSwitchMultiply = widgets.addCheckbox("action_switch_multiply", Component.translatable("gui.mae2a.multiply"), this::switchAction);
        actionSwitchMultiply.setRadio(true);
        actionSwitchDivide = widgets.addCheckbox("action_switch_divide", Component.translatable("gui.mae2a.divide"), this::switchAction);
        actionSwitchDivide.setRadio(true);
        updateState();
    }

    private void updateState() {
        boolean isMultiply = pms.getMode() == 0;
        this.setTextContent("item_input_tip", Component.translatable(
                String.format("gui.mae2a.pattern_%s_item_limit", isMultiply ? "max" : "min")));
        this.setTextContent("fluid_input_tip", Component.translatable(
                String.format("gui.mae2a.pattern_%s_fluid_limit", isMultiply ? "max" : "min")));
        this.itemInput.setPlaceholder(Component.translatable(
                String.format("gui.mae2a.pattern_%s_item_limit_placeholder", isMultiply ? "max" : "min")));
        this.fluidInput.setPlaceholder(Component.translatable(
                String.format("gui.mae2a.pattern_%s_fluid_limit_placeholder", isMultiply ? "max" : "min")));
        itemInput.setTooltipMessage(List.of(Component
                .translatable("gui.mae2a.%s_input_tip"
                        .formatted(isMultiply ? "max" : "min")
                )));
        itemInput.setValue(String.valueOf(isMultiply ? pms.getMaxItemLimit() : pms.getMinItemLimit()));
        fluidInput.setValue(NUMBER_FORMAT.format((isMultiply ? pms.getMaxFluidLimit() : pms.getMinFluidLimit()) / 1000D));
        saveByProducts.setSelected(pms.isSaveByProducts());
        actionSwitchMultiply.setSelected(isMultiply);
        actionSwitchDivide.setSelected(!isMultiply);
    }

    private void saveChange() {
        menu.saveChange(pms);
    }

    private void switchAction() {
        boolean flag = pms.getMode() == 0;
        pms.setMode(pms.getMode() == 0 ? 1 : 0);
        this.actionSwitchMultiply.setSelected(flag);
        this.actionSwitchDivide.setSelected(!flag);
        updateState();
    }

    public void onUpdate(String s, int type) {
        if (s == null || s.isEmpty()) {
            return;
        }
        try {
            Number number = NUMBER_FORMAT.parse(s);
            if (type == 0) {
                if (pms.getMode() == 0) {
                    pms.setMaxItemLimit(number.intValue());
                } else {
                    pms.setMinItemLimit(number.intValue());
                }
            } else if (type == 1) {
                if (pms.getMode() == 0) {
                    pms.setMaxFluidLimit((int) (number.doubleValue() * 1000));
                } else {
                    pms.setMinFluidLimit((int) (number.doubleValue() * 1000));
                }
            }
        } catch (ParseException ignored) {
        }
        saveChange();
    }
}
