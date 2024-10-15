package com.easterfg.mae2a.common.menu;

import appeng.menu.AEBaseMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import com.easterfg.mae2a.common.menu.host.PatternModifyHost;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import com.easterfg.mae2a.network.NetworkHandler;
import com.easterfg.mae2a.network.packet.ChangeSettingPacket;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

/**
 * @author EasterFG on 2024/10/1
 */
@Getter
public class PatternModifyMenu extends AEBaseMenu {

    protected final PatternModifyHost host;

    public static final MenuType<PatternModifyMenu> TYPE =
            MenuTypeBuilder.create(PatternModifyMenu::new, PatternModifyHost.class)
                    .build("pattern_modify");

    public PatternModifyMenu(int id, Inventory ip, PatternModifyHost host) {
        super(TYPE, id, ip, host);
        this.host = host;
    }

    public void saveChange(PatternModifySetting pms) {
        if (isClientSide()) {
            NetworkHandler.INSTANCE.sendToServer(new ChangeSettingPacket(pms));
            return;
        }
        ItemStack stack = host.getItemStack();
        if (stack == null) {
            return;
        }
        CompoundTag tag = stack.getOrCreateTag();
        pms.writeFromNBT(tag);
    }
}
