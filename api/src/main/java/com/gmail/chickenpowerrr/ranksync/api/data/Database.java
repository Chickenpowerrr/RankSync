package com.gmail.chickenpowerrr.ranksync.api.data;

import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import java.util.Collection;
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
   * Returns the id that represents a player on the other service by their id on this service
   *
   * @param playerId the id that represents the player on this service
   * @return a CompletableFuture that will be completed whenever the id of the other service has
   * been found
   */
  CompletableFuture<UUID> getUuid(String playerId);

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
   * Returns the id that represents a player on this service by their id on the other service
   *
   * @param uuid the id that represents the player on the other service
   * @return a CompletableFuture that will be completed whenever the id of this service has been
   * found
   */
  CompletableFuture<String> getPlayerId(UUID uuid);

  /**
   * Returns the ranks based on the id that represents the player on the other service
   *
   * @param uuid the id that represents the player on the other service
   * @return a CompletableFuture that will be completed whenever the ranks of this service has been
   * found
   */
  CompletableFuture<Collection<Rank>> getRanks(UUID uuid);

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
}
