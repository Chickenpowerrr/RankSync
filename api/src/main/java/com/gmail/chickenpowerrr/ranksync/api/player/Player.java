package com.gmail.chickenpowerrr.ranksync.api.player;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import java.util.Collection;
import java.util.UUID;

/**
 * This interface gives a Developer the possibility to manage players in the same way over multiple
 * platforms
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface Player {

  /**
   * Returns the ranks the Player currently has on this service
   */
  Collection<Rank> getRanks();

  /**
   * Sets the ranks for the Player on this service
   *
   * @param ranks the ranks the Player should have
   */
  void setRanks(Collection<Rank> ranks);

  /**
   * Returns the id that represents the Player on the other service
   */
  UUID getUuid();

  /**
   * Returns the id that represents the Player on this service
   */
  String getPersonalId();

  /**
   * Returns the Bot that uses this player
   */
  Bot getBot();

  /**
   * Sends a private message to the Player on this service
   *
   * @param message the message the Player should receive
   */
  void sendPrivateMessage(String message);

  /**
   * Returns the name the Player wants to hear when you're talking to it
   */
  String getFancyName();

  /**
   * Synchronizes the current ranks of this Player
   */
  void updateRanks();

  /**
   * Sets the id that represents this player on the other service
   *
   * @param uuid the id that represents this player on the other service
   */
  void setUuid(UUID uuid);
}
