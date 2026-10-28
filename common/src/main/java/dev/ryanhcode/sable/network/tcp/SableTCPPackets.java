package dev.ryanhcode.sable.network.tcp;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.network.packets.ClientboundSableSnapshotDualPacket;
import dev.ryanhcode.sable.network.packets.ClientboundSableSnapshotInfoDualPacket;
import dev.ryanhcode.sable.network.packets.tcp.*;
import dev.ryanhcode.sable.platform.SableNetworkPlatform;

public class SableTCPPackets {

    public static void init() {
        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundSableSnapshotDualPacket.TYPE, ClientboundSableSnapshotDualPacket.CODEC, ClientboundSableSnapshotDualPacket::handle);
        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundSableSnapshotInfoDualPacket.TYPE, ClientboundSableSnapshotInfoDualPacket.CODEC, ClientboundSableSnapshotInfoDualPacket::handle);
        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundStopMovingSubLevelPacket.TYPE, ClientboundStopMovingSubLevelPacket.CODEC, ClientboundStopMovingSubLevelPacket::handle);

        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundChangeSubLevelNamePacket.TYPE, ClientboundChangeSubLevelNamePacket.CODEC, ClientboundChangeSubLevelNamePacket::handle);

        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundStartTrackingSubLevelPacket.TYPE, ClientboundStartTrackingSubLevelPacket.CODEC, ClientboundStartTrackingSubLevelPacket::handle);
        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundFinalizeSubLevelPacket.TYPE, ClientboundFinalizeSubLevelPacket.CODEC, ClientboundFinalizeSubLevelPacket::handle);
        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundStopTrackingSubLevelPacket.TYPE, ClientboundStopTrackingSubLevelPacket.CODEC, ClientboundStopTrackingSubLevelPacket::handle);
        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundChangeBoundsSubLevelPacket.TYPE, ClientboundChangeBoundsSubLevelPacket.CODEC, ClientboundChangeBoundsSubLevelPacket::handle);

        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundFreezePlayerPacket.TYPE, ClientboundFreezePlayerPacket.CODEC, ClientboundFreezePlayerPacket::handle);

        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundPhysicsPropertyPacket.TYPE, ClientboundPhysicsPropertyPacket.CODEC, ClientboundPhysicsPropertyPacket::handle);
        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundFloatingBlockMaterialPacket.TYPE, ClientboundFloatingBlockMaterialPacket.CODEC, ClientboundFloatingBlockMaterialPacket::handle);
        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundDimensionPhysicsPacket.TYPE, ClientboundDimensionPhysicsPacket.CODEC, ClientboundDimensionPhysicsPacket::handle);

        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundRecentlySplitSubLevelPacket.TYPE, ClientboundRecentlySplitSubLevelPacket.CODEC, ClientboundRecentlySplitSubLevelPacket::handle);

        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundSableUDPActivationPacket.TYPE, ClientboundSableUDPActivationPacket.CODEC, ClientboundSableUDPActivationPacket::handle);

        SableNetworkPlatform.INSTANCE.registerClientbound(ClientboundEnterGizmoPacket.TYPE, ClientboundEnterGizmoPacket.CODEC, ClientboundEnterGizmoPacket::handle);

        SableNetworkPlatform.INSTANCE.registerServerbound(ServerboundPunchSubLevelPacket.TYPE, ServerboundPunchSubLevelPacket.CODEC, ServerboundPunchSubLevelPacket::handle);
        SableNetworkPlatform.INSTANCE.registerServerbound(ServerboundGizmoMoveSubLevelPacket.TYPE, ServerboundGizmoMoveSubLevelPacket.CODEC, ServerboundGizmoMoveSubLevelPacket::handle);
    }
}
