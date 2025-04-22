package com.easterfg.mae2a.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.api.util.AEColor;
import appeng.menu.AEBaseMenu;
import appeng.menu.implementations.MenuTypeBuilder;

import com.easterfg.mae2a.common.menu.host.CablePlaceToolHost;

import lombok.Getter;

/**
 * @author EasterFG on 2025/4/10
 */
@Getter
public class CablePlaceToolMenu extends AEBaseMenu {

    public static final MenuType<CablePlaceToolMenu> TYPE = MenuTypeBuilder
            .create(CablePlaceToolMenu::new, CablePlaceToolHost.class)
            .build("cable_place_tool");

    private final static String ACTION_COLOR = "change_color";
    private final static String ACTION_CABLE = "change_cable";
    private final static String ACTION_PICKER = "change_picker";
    private final static String ACTION_REPLACE = "change_replace";

    private final CablePlaceToolHost host;

    public CablePlaceToolMenu(int id, Inventory playerInventory, CablePlaceToolHost host) {
        super(TYPE, id, playerInventory, host);
        this.host = host;

        registerClientAction(ACTION_COLOR, AEColor.class, this::changeColor);
        registerClientAction(ACTION_CABLE, Integer.class, this::changeCable);
        registerClientAction(ACTION_PICKER, Integer.class, this::changePicker);
        registerClientAction(ACTION_REPLACE, Boolean.class, this::changeReplaceMode);
    }

    public void changeColor(AEColor color) {
        if (isClientSide()) {
            sendClientAction(ACTION_COLOR, color);
            return;
        }

        if (this.host.getColor() == color) {
            return;
        }
        this.host.setColor(color);
    }

    public void changeCable(int index) {
        if (isClientSide()) {
            sendClientAction(ACTION_CABLE, index);
            return;
        }

        if (index != -1 && index != this.host.getCable()) {
            this.host.setCable(index);
        }
    }

    public void changePicker(int distance) {
        if (isClientSide()) {
            sendClientAction(ACTION_PICKER, distance);
            return;
        }

        if (distance < 0 || distance > 64 || this.host.getPicker() == distance) {
            return;
        }

        this.host.setPicker(distance);
    }

    public void changeReplaceMode(boolean replace) {
        if (isClientSide()) {
            sendClientAction(ACTION_REPLACE, replace);
            return;
        }

        if (this.host.isReplace() == replace) {
            return;
        }
        this.host.setReplace(replace);
    }
}
