package com.gmail.chickenpowerrr.ranksync.api.rank;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This interface is used to get ranks from an external resource
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface RankResource {

  /**
   * Requests the ranks for a given Player
   *
   * @param uuid the UUID that represents the Player
   * @return a {@code CompletableFuture} that will be completed whenever the ranks have been found
   */
  CompletableFuture<List<Rank>> getRanks(UUID uuid);

  /**
   * Sets the {@code Bot} that uses this Resource
   *
   * @param bot the instance of the bot that makes use of this Resource
   */
  void setBot(Bot bot);

  /**
   * Checks if the given rank is supported by this Resource
   *
   * @param name the name of the rank
   * @return true if the rank is supported, false if it's not
   */
  boolean isValidRank(String name);

  /**
   * Returns all of the ranks the database contains
   */
  Collection<String> getAvailableRanks();

  /**
   * Returns if the ranks are case sensitive when they are requested by their name
   */
  boolean hasCaseSensitiveRanks();
}
