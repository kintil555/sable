package dev.ryanhcode.sable.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet sinks for Sable payloads. Replaces Veil's VeilPacketManager.
 */
public final class SableNetwork {

    private SableNetwork() {
    }

    /**
     * @return a sink sending clientbound payloads to a single player
     */
    public static SablePacketSink player(final ServerPlayer player) {
        return packet -> player.connection.send(packet);
    }

    /**
     * @return a sink sending serverbound payloads from the local client. Must only be used on the client.
     */
    public static SablePacketSink server() {
        return new SablePacketSink() {
            @Override
            public void sendPacket(final Packet<?> packet) {
                final var connection = Minecraft.getInstance().getConnection();
                if (connection != null) {
                    connection.send(packet);
                }
            }

            @Override
            public Packet<?> wrap(final CustomPacketPayload payload) {
                return new ServerboundCustomPayloadPacket(payload);
            }
        };
    }
}
