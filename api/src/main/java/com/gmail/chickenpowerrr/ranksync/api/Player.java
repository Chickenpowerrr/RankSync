package com.gmail.chickenpowerrr.ranksync.api;

import java.util.Collection;
import java.util.UUID;

/**
 * This interface gives a Developer the possibility to manage
 * players in the same way over multiple platforms
 *
 * @author Chickenpowerrr
 * @since  1.0.0
 */
public interface Player {

    /**
     * Get the ranks the Player currently has on this service
     *
     * @return the ranks the Player currently has on this service
     */
    Collection<Rank> getRanks();

    /**
     * Set the ranks for the Player on this service
     *
     * @param ranks the ranks the Player should have
     */
    void setRanks(Collection<Rank> ranks);

    /**
     * Get the id that represents the Player on the other service
     *
     * @return the id that represents the Player on the other service
     */
    UUID getUuid();

    /**
     * Get the id that represents the Player on this service
     *
     * @return the id that represents the Player on this service
     */
    String getPersonalId();

    /**
     * Get the {@code Bot} that uses this player
     *
     * @return the {@code Bot} that uses this player
     */
    Bot getBot();

    /**
     * Send a private message to the Player on this service
     *
     * @param message the message the Player should receive
     */
    void sendPrivateMessage(String message);

    /**
     * Get the name the Player wants to hear when you're talking to it
     *
     * @return the name the Player wants to hear when you're talking to it
     */
    String getFancyName();

    /**
     * Synchronize the current ranks of this Player
     *
     */
    void updateRanks();

    /**
     * Update the id that represents this player on the other service
     *
     * @param uuid the id that represents this player on the other service
     */
    void setUuid(UUID uuid);
}
