package com.easterfg.mae2a.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;

/**
 * @author EasterFG on 2025/4/11
 */
public class OpenMenuPacket implements IMessage<OpenMenuPacket> {
    public String menuId;
    public InteractionHand hand;

    public OpenMenuPacket() {
    }

    public OpenMenuPacket(String menuId, InteractionHand hand) {
        this.menuId = menuId;
        this.hand = hand;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(menuId);
        buf.writeEnum(hand);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        menuId = buf.readUtf();
        hand = buf.readEnum(InteractionHand.class);
    }

    @Override
    public void onMessage(Player player) {
        MenuType<?> menuType = ForgeRegistries.MENU_TYPES.getValue(new ResourceLocation(menuId));
        MenuOpener.open(menuType, player, MenuLocators.forHand(player, hand));
    }

    @Override
    public Class<OpenMenuPacket> getPacketClass() {
        return OpenMenuPacket.class;
    }

}
