package dev.ryanhcode.sable.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * A destination for packets. Replaces Veil's SablePacketSink.
 */
public interface SablePacketSink {

    void sendPacket(Packet<?> packet);

    default Packet<?> wrap(final CustomPacketPayload payload) {
        return new ClientboundCustomPayloadPacket(payload);
    }

    default void sendPacket(final CustomPacketPayload... payloads) {
        for (final CustomPacketPayload payload : payloads) {
            this.sendPacket(this.wrap(payload));
        }
    }
}
