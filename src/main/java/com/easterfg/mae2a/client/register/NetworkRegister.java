package com.easterfg.mae2a.client.register;

import com.easterfg.mae2a.network.NetworkHandler;
import com.easterfg.mae2a.network.packet.OpenMenuPacket;
import com.easterfg.mae2a.network.packet.UpdateSettingPackage;

/**
 * @author EasterFG on 2024/10/3
 */
public class NetworkRegister {
    public static void register() {
        NetworkHandler.INSTANCE.registerPacket(OpenMenuPacket.class, OpenMenuPacket::new);
        NetworkHandler.INSTANCE.registerPacket(UpdateSettingPackage.class, UpdateSettingPackage::new);
    }
}
