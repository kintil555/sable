package dev.ryanhcode.sable.network;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Loader-agnostic context for a received Sable payload. Replaces Veil's SablePacketContext.
 */
public interface SablePacketContext {

    /**
     * @return the receiving player (the sender on the server, the local player on the client)
     */
    Player player();

    /**
     * @return the level the player is in
     */
    default Level level() {
        return this.player().level();
    }
}
