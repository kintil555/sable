package dev.ryanhcode.sable.platform;

import dev.ryanhcode.sable.network.SablePacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface SableNetworkPlatform {

    SableNetworkPlatform INSTANCE = SablePlatformUtil.load(SableNetworkPlatform.class);

    @FunctionalInterface
    interface PayloadHandler<T extends CustomPacketPayload> {
        void handle(T payload, SablePacketContext context);
    }

    <T extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PayloadHandler<T> handler);

    <T extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, PayloadHandler<T> handler);
}
