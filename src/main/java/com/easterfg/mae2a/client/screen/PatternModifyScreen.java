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
import com.easterfg.mae2a.client.gui.widget.CustomIconButton;
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
    private final AETextField rateInput;
    private final PatternModifySetting setting;
    private final Map<ModifyMode, TabButton> modeTabButtons = new EnumMap<>(ModifyMode.class);

    private static final Pattern INTEGER_REGEX = Pattern.compile("\\d+");
    private static final Pattern FLOAT_REGEX = Pattern.compile("\\d+(\\.\\d{0,3})?");
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.###");

    public PatternModifyScreen(PatternModifyMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        setting = menu.getHost().getPatternModifySetting();

        var switchAction = new CustomIconButton(__ -> setActionMode(!menu.isLimitMode()),
                MoreAE2Additions.id("textures/guis/modify_action.png"),
                Component.translatable("gui.mae2a.action_limit"),
                Component.translatable("gui.mae2a.action_rate"));
        switchAction.setStatusSupplier(menu::isLimitMode);
        switchAction.setMessage(Component.translatable("gui.mae2a.action_switch_tip"));
        this.addToLeftToolbar(switchAction);

        itemInput = widgets.addTextField("item_input");
        fluidInput = widgets.addTextField("fluid_input");
        rateInput = widgets.addTextField("rate_input");

        setInput(itemInput, INTEGER_REGEX, 15, 0);
        setInput(fluidInput, FLOAT_REGEX, 15, 1);
        setInput(rateInput, INTEGER_REGEX, 8, 2);

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
                () -> {
                    setting.setSaveByProducts(!setting.isSaveByProducts());
                    menu.saveSetting(setting);
                });
        saveByProducts.setSelected(setting.isSaveByProducts());
        updateState(setting.getMode(), setting.isLimitMode());
    }

    private void setInput(AETextField input, Pattern pattern, int maxLength, int type) {
        input.setFilter(s -> s.isEmpty() || pattern.matcher(s).matches());
        input.setMaxLength(maxLength);
        input.setResponder(s -> this.onUpdate(s, type));
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
        setting.setMode(mode);
        updateState(mode, setting.isLimitMode());
    }

    private void setActionMode(boolean visible) {
        menu.setLimitMode(visible);
        setting.setLimitMode(visible);
        updateState(setting.getMode(), visible);
    }

    private void updateState(ModifyMode mode, boolean isLimitMode) {
        String itemPlaceholderKey, fluidPlaceholderKey, ratePlaceholderTooltip, tooltipKey, itemValue, fluidValue;
        String tooltipA;
        String tooltipB;

        switch (mode) {
            case MULTIPLY -> {
                tooltipA = "gui.mae2a.pattern_max_item_limit";
                tooltipB = "gui.mae2a.pattern_max_fluid_limit";
                itemPlaceholderKey = "gui.mae2a.pattern_max_item_limit";
                fluidPlaceholderKey = "gui.mae2a.pattern_max_fluid_limit";
                ratePlaceholderTooltip = "gui.mae2a.pattern_rate_multiply";
                tooltipKey = "gui.mae2a.max_input_tip";

            }
            case DIVIDE -> {
                tooltipA = "gui.mae2a.pattern_min_item_limit";
                tooltipB = "gui.mae2a.pattern_min_fluid_limit";
                itemPlaceholderKey = "gui.mae2a.pattern_min_item_limit";
                fluidPlaceholderKey = "gui.mae2a.pattern_min_fluid_limit";
                ratePlaceholderTooltip = "gui.mae2a.pattern_rate_divide";
                tooltipKey = "gui.mae2a.min_input_tip";
            }
            default -> {
                MoreAE2Additions.LOGGER.warn("Unknown ModifyMode: " + mode);
                return;
            }
        }

        if (isLimitMode) {
            this.setTextHidden("tooltip_2", false);
            this.setTextContent("tooltip_1", Component.translatable(tooltipA));
            this.setTextContent("tooltip_2", Component.translatable(tooltipB));
        } else {
            this.setTextHidden("tooltip_2", true);
            this.setTextContent("tooltip_1", Component.translatable("gui.mae2a.pattern_rate"));
        }

        itemValue = String.valueOf(getItemLimit(mode));
        fluidValue = NUMBER_FORMAT.format(getFluidLimit(mode) / 1000D);
        updateInputFields(itemInput, itemPlaceholderKey, tooltipKey, itemValue);
        updateInputFields(fluidInput, fluidPlaceholderKey, tooltipKey, fluidValue);
        updateInputFields(rateInput, "gui.mae2a.pattern_rate", ratePlaceholderTooltip,
                String.valueOf(setting.getRate()));

        saveByProducts.setSelected(setting.isSaveByProducts());
        updateInput(setting.isLimitMode());
    }

    private void updateInput(boolean visible) {
        this.itemInput.setVisible(visible);
        this.fluidInput.setVisible(visible);
        this.rateInput.setVisible(!visible);
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
                setItemLimit(number.intValue());
            } else if (type == 1) {
                setFluidLimit((int) (number.doubleValue() * 1000));
            } else if (type == 2) {
                setting.setRate(number.intValue());
            }
            menu.saveSetting(setting);
        } catch (ParseException ignored) {
        }
    }

    private void setItemLimit(int limit) {
        if (setting.getMode() == ModifyMode.MULTIPLY) {
            if (setting.getMaxItemLimit() == limit)
                return;
            setting.setMaxItemLimit(limit);
        } else {
            if (setting.getMinItemLimit() == limit)
                return;
            setting.setMinItemLimit(limit);
        }
    }

    private void setFluidLimit(int limit) {
        if (setting.getMode() == ModifyMode.MULTIPLY) {
            if (setting.getMaxFluidLimit() == limit)
                return;
            setting.setMaxFluidLimit(limit);
        } else {
            if (setting.getMinFluidLimit() == limit)
                return;
            setting.setMinFluidLimit(limit);
        }
    }

    private int getItemLimit(ModifyMode mode) {
        if (mode == ModifyMode.MULTIPLY) {
            return setting.getMaxItemLimit();
        }
        return setting.getMinItemLimit();
    }

    private int getFluidLimit(ModifyMode mode) {
        if (mode == ModifyMode.MULTIPLY) {
            return setting.getMaxFluidLimit();
        }
        return setting.getMinFluidLimit();
    }
}
