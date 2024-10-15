package com.easterfg.mae2a.network.packet;

import com.easterfg.mae2a.common.menu.PatternModifyMenu;
import com.easterfg.mae2a.common.settings.PatternModifySetting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

/**
 * @author EasterFG on 2024/10/3
 */
public class ChangeSettingPacket implements IMessage<ChangeSettingPacket> {

    private PatternModifySetting setting;

    public ChangeSettingPacket() {
        // NOTHING
    }

    public ChangeSettingPacket(PatternModifySetting setting) {
        this.setting = setting;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(setting.getMode());
        buf.writeInt(setting.getMaxItemLimit());
        buf.writeInt(setting.getMaxFluidLimit());
        buf.writeInt(setting.getMinItemLimit());
        buf.writeInt(setting.getMinFluidLimit());
        buf.writeBoolean(setting.isSaveByProducts());
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        setting = new PatternModifySetting();
        setting.setMode(buf.readInt());
        setting.setMaxItemLimit(buf.readInt());
        setting.setMaxFluidLimit(buf.readInt());
        setting.setMinItemLimit(buf.readInt());
        setting.setMinFluidLimit(buf.readInt());
        setting.setSaveByProducts(buf.readBoolean());
    }

    @Override
    public void onMessage(Player player) {
        if (player.containerMenu instanceof PatternModifyMenu menu) {
            menu.saveChange(setting);
        }
    }

    @Override
    public Class<ChangeSettingPacket> getPacketClass() {
        return ChangeSettingPacket.class;
    }

    @Override
    public boolean isClient() {
        return false;
    }
}
