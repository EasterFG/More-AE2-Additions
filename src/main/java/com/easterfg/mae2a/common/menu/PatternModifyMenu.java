package com.easterfg.mae2a.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import appeng.menu.AEBaseMenu;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.MenuTypeBuilder;

import com.easterfg.mae2a.common.menu.host.PatternModifyHost;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import com.easterfg.mae2a.common.settings.PatternModifySetting.ModifyMode;

import lombok.Getter;

/**
 * @author EasterFG on 2024/10/1
 */
@Getter
public class PatternModifyMenu extends AEBaseMenu {

    protected final PatternModifyHost host;

    private static final String ACTION_SET_MODE = "set_mode";
    private static final String ACTION_SET_ACTION = "set_action";
    private static final String ACTION_SAVE_SETTING = "save_setting";

    private ModifyMode currentMode;
    private boolean currentActionMode;

    @Getter
    @GuiSync(1)
    public ModifyMode mode = ModifyMode.MULTIPLY;

    @Getter
    @GuiSync(2)
    public boolean limitMode = true;

    public static final MenuType<PatternModifyMenu> TYPE = MenuTypeBuilder
            .create(PatternModifyMenu::new, PatternModifyHost.class)
            .build("pattern_modify");

    public PatternModifyMenu(int id, Inventory ip, PatternModifyHost host) {
        super(TYPE, id, ip, host);
        this.host = host;

        registerClientAction(ACTION_SAVE_SETTING, PatternModifySetting.class, this::saveSetting);
        registerClientAction(ACTION_SET_MODE, ModifyMode.class, host::setMode);
        registerClientAction(ACTION_SET_ACTION, Boolean.class, host::setActionMode);
    }

    public void setMode(ModifyMode mode) {
        if (isClientSide()) {
            sendClientAction(ACTION_SET_MODE, mode);
        } else {
            this.mode = mode;
        }
    }

    public void setLimitMode(boolean limitMode) {
        if (isClientSide()) {
            sendClientAction(ACTION_SET_ACTION, limitMode);
        } else {
            this.limitMode = limitMode;
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (isServerSide()) {
            if (this.mode != host.getMode()) {
                this.setMode(host.getMode());
            }

            if (this.limitMode != host.isLimitMode()) {
                this.setLimitMode(host.isLimitMode());
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

        if (this.currentActionMode != this.limitMode) {
            this.host.setActionMode(this.limitMode);
            this.currentActionMode = this.limitMode;
        }

    }

    public void saveSetting(PatternModifySetting setting) {
        if (isClientSide()) {
            sendClientAction(ACTION_SAVE_SETTING, setting);
            return;
        }
        this.host.saveSetting(setting);
    }
}
