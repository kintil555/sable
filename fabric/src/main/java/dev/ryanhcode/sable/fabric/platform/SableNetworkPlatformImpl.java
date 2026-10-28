package dev.ryanhcode.sable.fabric.platform;

import dev.ryanhcode.sable.network.SablePacketContext;
import dev.ryanhcode.sable.platform.SableNetworkPlatform;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class SableNetworkPlatformImpl implements SableNetworkPlatform {

    private static final List<Runnable> CLIENT_RECEIVERS = new ArrayList<>();

    /**
     * Registers all clientbound receivers. Must be called from the client initializer, after {@link dev.ryanhcode.sable.Sable#init()}.
     */
    public static void registerClientReceivers() {
        CLIENT_RECEIVERS.forEach(Runnable::run);
        CLIENT_RECEIVERS.clear();
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientbound(final CustomPacketPayload.Type<T> type, final StreamCodec<? super RegistryFriendlyByteBuf, T> codec, final PayloadHandler<T> handler) {
        PayloadTypeRegistry.clientboundPlay().register(type, codec);
        CLIENT_RECEIVERS.add(() -> ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            final Player player = context.client().player;
            handler.handle(payload, () -> player);
        }));
    }

    @Override
    public <T extends CustomPacketPayload> void registerServerbound(final CustomPacketPayload.Type<T> type, final StreamCodec<? super RegistryFriendlyByteBuf, T> codec, final PayloadHandler<T> handler) {
        PayloadTypeRegistry.serverboundPlay().register(type, codec);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            final Player player = context.player();
            handler.handle(payload, () -> player);
        });
    }
}
