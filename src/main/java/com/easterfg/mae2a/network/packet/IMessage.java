package com.easterfg.mae2a.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

/**
 * @author EasterFG on 2024/10/3
 */
public interface IMessage<MSG> {
    void toBytes(FriendlyByteBuf buf);

    void fromBytes(FriendlyByteBuf buf);

    void onMessage(Player player);

    Class<MSG> getPacketClass();

    boolean isClient();

}
