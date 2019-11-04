package com.gmail.chickenpowerrr.ranksync.api.data;

import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This interface contains all of the methods we need to get the data we need to synchronize ranks
 * properly between multiple services
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface Database {

  /**
   * Returns the Player that represents the link between the two platforms
   *
   * @param playerId the id that represents the player on this service
   * @param constructor the player constructor based on the retrieved data
   * @return a CompletableFuture that will be completed whenever the link has been found
   */
  CompletableFuture<Player> getPlayer(String playerId, PlayerConstructor<Player> constructor);

  /**
   * Sets the id that represents a player on the other service by their id on this service
   *
   * @param playerId the id that represents the player on this service
   * @param uuid the id that represents the player on the other service
   * @return a CompletableFuture that will be completed whenever the id of the other service has
   * been linked to this service
   */
  CompletableFuture<Void> setUuid(String playerId, UUID uuid);

  /**
   * Returns the Player that represents the link between the two platforms
   *
   * @param uuid the id that represents the player on the other service
   * @param constructor the player constructor based on the retrieved data
   * @return a CompletableFuture that will be completed whenever the link has been found
   */
  CompletableFuture<Player> getPlayer(UUID uuid, PlayerConstructor<Player> constructor);

  /**
   * Returns the ranks based on the id that represents the player on the other service
   *
   * @param uuid the id that represents the player on the other service
   * @return a CompletableFuture that will be completed whenever the ranks of this service has been
   * found
   */
  CompletableFuture<List<Rank>> getRanks(UUID uuid);

  /**
   * Returns the if a rank with the given name exists
   *
   * @param rankName the name of the Rank
   * @return a CompletableFuture that will be completed whenever we know if the rank exists, true if
   * it exists, false if it doesn't
   */
  CompletableFuture<Boolean> isValidRank(String rankName);

  /**
   * Returns all of the ranks the database contains
   */
  Collection<String> getAvailableRanks();

  /**
   * Returns if the ranks are case sensitive when they are requested by their name
   */
  boolean hasCaseSensitiveRanks();

  /**
   * This functional interface makes sure a database can turn data into a player
   *
   * @param <P> the player type that should be returned
   */
  @FunctionalInterface
  interface PlayerConstructor<P extends Player> {
    P apply(UUID uuid, String identifier, int timesSynced, int timesUnsynced);
  }
}
