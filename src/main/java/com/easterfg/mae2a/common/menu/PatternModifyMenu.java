package com.easterfg.mae2a.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.menu.AEBaseMenu;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.MenuTypeBuilder;

import com.easterfg.mae2a.common.menu.host.PatternModifyHost;
import com.easterfg.mae2a.common.settings.PatternModifySetting.ModifyMode;

import lombok.Getter;

/**
 * @author EasterFG on 2024/10/1
 */
@Getter
public class PatternModifyMenu extends AEBaseMenu {

    protected final PatternModifyHost host;

    private static final String ACTION_SAVE_ITEM = "save_item";
    private static final String ACTION_SAVE_FLUID = "save_fluid";
    private static final String ACTION_SWITCH_SAVE = "switch_save";
    private static final String ACTION_SET_MODE = "set_mode";

    private ModifyMode currentMode;

    @Getter
    @GuiSync(1)
    public ModifyMode mode = ModifyMode.MULTIPLY;

    public static final MenuType<PatternModifyMenu> TYPE = MenuTypeBuilder
            .create(PatternModifyMenu::new, PatternModifyHost.class)
            .build("pattern_modify");

    public PatternModifyMenu(int id, Inventory ip, PatternModifyHost host) {
        super(TYPE, id, ip, host);
        this.host = host;

        registerClientAction(ACTION_SAVE_ITEM, Integer.class, this::setItemLimit);
        registerClientAction(ACTION_SAVE_FLUID, Integer.class, this::setFluidLimit);
        registerClientAction(ACTION_SWITCH_SAVE, this::setSaveByProducts);
        registerClientAction(ACTION_SET_MODE, ModifyMode.class, host::setMode);
    }

    public void setItemLimit(int value) {
        if (isClientSide()) {
            sendClientAction(ACTION_SAVE_ITEM, value);
            return;
        }

        this.host.setItemLimit(value);
    }

    public void setFluidLimit(int value) {
        if (isClientSide()) {
            sendClientAction(ACTION_SAVE_FLUID, value);
            return;
        }

        this.host.setFluidLimit(value);
    }

    public void setSaveByProducts() {
        if (isClientSide()) {
            sendClientAction(ACTION_SWITCH_SAVE);
            return;
        }
        this.host.switchSave();
    }

    public void setMode(ModifyMode mode) {
        if (isClientSide()) {
            sendClientAction(ACTION_SET_MODE, mode);
        } else {
            this.mode = mode;
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (isServerSide()) {
            if (this.mode != host.getMode()) {
                this.setMode(host.getMode());
            }
        }
    }

    @Override
    public void onServerDataSync() {
        super.onServerDataSync();

        if (this.currentMode != this.mode) {
            this.host.setMode(this.mode);
            this.currentMode = this.mode;
        }

    }
}
