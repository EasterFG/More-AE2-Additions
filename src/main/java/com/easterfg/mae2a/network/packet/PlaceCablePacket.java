package com.easterfg.mae2a.network.packet;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import com.easterfg.mae2a.util.CableToolHelper;
import com.easterfg.mae2a.util.NBTHelper;

public class PlaceCablePacket implements IMessage<PlaceCablePacket> {

    public InteractionHand hand;

    public PlaceCablePacket() {
    }

    public PlaceCablePacket(InteractionHand hand) {
        this.hand = hand;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(hand);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        hand = buf.readEnum(InteractionHand.class);
    }

    @Override
    public void onMessage(Player player) {
        var level = player.getCommandSenderWorld();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        var stack = player.getItemInHand(this.hand);
        List<BlockPos> blockList = NBTHelper.getBlockList(stack.getTag(), NBTHelper.POS_LIST_ID);
        if (blockList.isEmpty()) {
            return;
        }
        var bind = NBTHelper.getGlobalPos(stack, NBTHelper.BIND_POS_ID);
        CableToolHelper.place(stack, player, serverLevel, bind, blockList, NBTHelper.readSetting(stack));
        stack.removeTagKey(NBTHelper.POS_LIST_ID);
    }

    @Override
    public Class<PlaceCablePacket> getPacketClass() {
        return PlaceCablePacket.class;
    }
}
