package com.easterfg.mae2a.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import com.easterfg.mae2a.common.menu.CablePlaceToolMenu;

/**
 * @author EasterFG on 2025/4/12
 */
public class UpdateSettingPackage implements IMessage<UpdateSettingPackage> {

    private boolean value;

    public UpdateSettingPackage() {
    }

    public UpdateSettingPackage(boolean value) {
        this.value = value;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(value);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        this.value = buf.readBoolean();
    }

    @Override
    public void onMessage(Player player) {
        if (player.containerMenu instanceof CablePlaceToolMenu menu) {
            menu.setReplace(value);
        }
    }

    @Override
    public Class<UpdateSettingPackage> getPacketClass() {
        return UpdateSettingPackage.class;
    }
}
