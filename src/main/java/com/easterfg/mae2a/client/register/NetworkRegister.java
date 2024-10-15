package com.easterfg.mae2a.client.register;

import com.easterfg.mae2a.network.NetworkHandler;
import com.easterfg.mae2a.network.packet.ChangeSettingPacket;

/**
 * @author EasterFG on 2024/10/3
 */
public class NetworkRegister {

    public static void register() {
        NetworkHandler.INSTANCE.registerPacket(ChangeSettingPacket.class, ChangeSettingPacket::new);
    }

}
