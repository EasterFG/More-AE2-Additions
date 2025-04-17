package com.easterfg.mae2a.client.screen;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AECheckbox;
import appeng.client.gui.widgets.AETextField;
import appeng.client.gui.widgets.TabButton;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.common.menu.PatternModifyMenu;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import com.easterfg.mae2a.common.settings.PatternModifySetting.ModifyMode;

/**
 * @author EasterFG on 2024/10/1
 */
public class PatternModifyScreen extends AEBaseScreen<PatternModifyMenu> {

    private final AETextField itemInput;
    private final AETextField fluidInput;
    private final AECheckbox saveByProducts;
    private final PatternModifySetting setting;
    private final Map<ModifyMode, TabButton> modeTabButtons = new EnumMap<>(ModifyMode.class);

    private static final Pattern INTEGER_REGEX = Pattern.compile("\\d+");
    private static final Pattern FLOAT_REGEX = Pattern.compile("\\d+(\\.\\d{0,3})?");
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.###");

    public PatternModifyScreen(PatternModifyMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        setting = menu.getHost().getPatternModifySetting();

        itemInput = widgets.addTextField("item_input");
        fluidInput = widgets.addTextField("fluid_input");
        itemInput.setFilter(str -> str.isEmpty() || INTEGER_REGEX.matcher(str).matches());
        itemInput.setMaxLength(15);
        itemInput.setResponder(s -> this.onUpdate(s, 0));

        fluidInput.setFilter(str -> str.isEmpty() || FLOAT_REGEX.matcher(str).matches());
        fluidInput.setMaxLength(15);
        fluidInput.setResponder(s -> this.onUpdate(s, 1));

        for (var mode : ModifyMode.values()) {
            var tab = new TabButton(
                    mode.icon(),
                    mode.tooltip(),
                    press -> setMode(mode));
            tab.setStyle(TabButton.Style.HORIZONTAL);

            var index = modeTabButtons.size();
            modeTabButtons.put(mode, tab);
            this.widgets.add("tab_" + index, tab);
        }

        saveByProducts = widgets.addCheckbox("save_by_products", Component.translatable("gui.mae2a.retain_by_products"),
                menu::setSaveByProducts);

        updateState(menu.getMode());
    }

    @Override
    protected void updateBeforeRender() {
        super.updateBeforeRender();
        for (var mode : ModifyMode.values()) {
            boolean selected = menu.getMode() == mode;
            modeTabButtons.get(mode).setSelected(selected);
        }
    }

    private void setMode(ModifyMode mode) {
        menu.setMode(mode);
        updateState(mode);
    }

    private void updateState(ModifyMode mode) {
        String itemPlaceholderKey, fluidPlaceholderKey, tooltipKey, itemValue, fluidValue;

        switch (mode) {
            case MULTIPLY -> {
                itemPlaceholderKey = "gui.mae2a.pattern_max_item_limit";
                fluidPlaceholderKey = "gui.mae2a.pattern_max_fluid_limit";
                tooltipKey = "gui.mae2a.max_input_tip";
                itemValue = String.valueOf(setting.getMaxItemLimit());
                fluidValue = NUMBER_FORMAT.format(setting.getMaxFluidLimit() / 1000D);
            }

            case DIVIDE -> {
                itemPlaceholderKey = "gui.mae2a.pattern_min_item_limit";
                fluidPlaceholderKey = "gui.mae2a.pattern_min_fluid_limit";
                tooltipKey = "gui.mae2a.min_input_tip";
                itemValue = String.valueOf(setting.getMinItemLimit());
                fluidValue = NUMBER_FORMAT.format(setting.getMinFluidLimit() / 1000D);
            }
            default -> {
                MoreAE2Additions.LOGGER.warn("Unknown ModifyMode: " + mode);
                return;
            }
        }

        updateInputFields(itemInput, itemPlaceholderKey, tooltipKey, itemValue);
        updateInputFields(fluidInput, fluidPlaceholderKey, tooltipKey, fluidValue);

        saveByProducts.setSelected(setting.isSaveByProducts());
    }

    private void updateInputFields(AETextField input, String placeholder, String tooltip, String value) {
        input.setPlaceholder(Component.translatable(placeholder));
        input.setTooltipMessage(Collections.singletonList(Component.translatable(tooltip)));
        input.setValue(value);
    }

    public void onUpdate(String s, int type) {
        if (s == null || s.isEmpty()) {
            return;
        }
        try {
            Number number = NUMBER_FORMAT.parse(s);
            if (type == 0) {
                menu.setItemLimit(number.intValue());
            } else if (type == 1) {
                menu.setFluidLimit((int) (number.doubleValue() * 1000));
            }
        } catch (ParseException ignored) {
        }
    }
}
