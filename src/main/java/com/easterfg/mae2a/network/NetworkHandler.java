package com.easterfg.mae2a.network;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.network.packet.IMessage;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * @author EasterFG on 2024/10/3
 */
public class NetworkHandler {

    private final ResourceLocation channel;
    int id = 0;
    private final Consumer<IMessage<?>> clientHandler;
    private final Int2ObjectMap<Supplier<IMessage<?>>> packetFactoryMap = new Int2ObjectOpenHashMap<>();
    private final Object2IntMap<Class<?>> packetIDMap = new Object2IntOpenHashMap<>();

    public static final NetworkHandler INSTANCE = new NetworkHandler();

    public NetworkHandler() {
        this.channel = MoreAE2Additions.id("network");
        EventNetworkChannel ec = NetworkRegistry.ChannelBuilder.named(channel)
                .networkProtocolVersion(() -> "1").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true)
                .eventNetworkChannel();
        ec.addListener(this::serverPacket);
        ec.addListener(this::clientPacket);
        this.clientHandler = DistExecutor.unsafeRunForDist(() -> () -> NetworkHandler::onClientPacketData,
                () -> () -> pkt -> {
                });
    }

    public void registerPacket(Class<?> clazz, Supplier<IMessage<?>> factory) {
        this.packetIDMap.put(clazz, this.id);
        this.packetFactoryMap.put(this.id, factory);
        this.id++;
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientPacketData(IMessage<?> packet) {
        if (packet.isClient()) {
            packet.onMessage(Minecraft.getInstance().player);
        }
    }

    @SubscribeEvent
    public void serverPacket(final NetworkEvent.ClientCustomPayloadEvent ev) {
        try {
            NetworkEvent.Context ctx = ev.getSource().get();
            ctx.setPacketHandled(true);
            var bytes = ev.getPayload();
            var packet = this.packetFactoryMap.get(bytes.readVarInt()).get();
            packet.fromBytes(bytes);
            var player = ctx.getSender();
            ctx.enqueueWork(() -> {
                try {
                    packet.onMessage(player);
                } catch (final IllegalArgumentException e) {
                    MoreAE2Additions.LOGGER.warn(e);
                }
            });
        } catch (final RunningOnDifferentThreadException ignored) {
        }
    }

    @SubscribeEvent
    public void clientPacket(NetworkEvent.ServerCustomPayloadEvent ev) {
        if (ev instanceof NetworkEvent.ServerCustomPayloadLoginEvent) {
            return;
        }
        if (this.clientHandler != null) {
            try {
                NetworkEvent.Context ctx = ev.getSource().get();
                ctx.setPacketHandled(true);
                var bytes = ev.getPayload();
                var packet = this.packetFactoryMap.get(bytes.readVarInt()).get();
                packet.fromBytes(bytes);
                ctx.enqueueWork(() -> this.clientHandler.accept(packet));
            } catch (RunningOnDifferentThreadException ignored) {
            }
        }
    }

    /**
     * send network packet to all players
     * 
     * @param message the packet to send
     */
    public void sendToAll(IMessage<?> message) {
        var server = MoreAE2Additions.INSTANCE.getServer();
        if (server != null) {
            server.getPlayerList().broadcastAll(this.toPacket(message, NetworkDirection.PLAY_TO_CLIENT));
        }
    }

    /**
     * send network packet to target player
     * 
     * @param message the packet to send
     * @param player  target player
     */
    public void sendTo(IMessage<?> message, ServerPlayer player) {
        player.connection.send(this.toPacket(message, NetworkDirection.PLAY_TO_CLIENT));
    }

    /**
     * send network packet to server
     * 
     * @param message the packet to send
     */
    public void sendToServer(IMessage<?> message) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null)
            return;
        connection.send(this.toPacket(message, NetworkDirection.PLAY_TO_SERVER));
    }

    private Packet<?> toPacket(IMessage<?> message, NetworkDirection direction) {
        var buf = new FriendlyByteBuf(Unpooled.buffer(1024));
        var id = this.packetIDMap.getOrDefault(message.getPacketClass(), -1);
        if (id == -1) {
            MoreAE2Additions.LOGGER.error(String.format("Unregistered Packet: %s", message.getPacketClass()));
        }
        buf.writeVarInt(id);
        message.toBytes(buf);
        return direction.buildPacket(Pair.of(buf, 0), channel).getThis();
    }
}
