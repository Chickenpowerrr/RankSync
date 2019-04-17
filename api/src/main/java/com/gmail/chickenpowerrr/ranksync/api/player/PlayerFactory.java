package com.gmail.chickenpowerrr.ranksync.api.player;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This interface manages all of the data that a Developer will probably need to synchronize the
 * Player data between multiple services
 *
 * @param <P> the Player class used by the platform API, not the RankSync API
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface PlayerFactory<P> {

  /**
   * Turns an internally used player into a player that is supported by the RankSync API
   *
   * @param internalObject the player object used by the platform API, not the RankSync API
   * @return the RankSync representation of the player
   */
  CompletableFuture<Player> getPlayer(P internalObject);

  /**
   * Returns a Player object based on its UUID that came from the other service
   *
   * @param uuid the UUID that represents this player on another service
   * @return a CompletableFuture that will be completed whenever the player has been found
   */
  CompletableFuture<Player> getPlayer(UUID uuid);

  /**
   * Updates the link for a given Player
   *
   * @param playerId the id that represents the player on the current service
   * @param uuid the uuid that represents the player on the other service
   * @return a CompletableFuture that will be completed whenever the process is done
   */
  CompletableFuture<Void> setUuid(String playerId, UUID uuid);

  /**
   * Returns the Bot that uses this factory
   *
   * @return the Bot that uses this factory
   */
  Bot<P, ?> getBot();
}
