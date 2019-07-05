package com.gmail.chickenpowerrr.ranksync.api.rank;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import java.util.Collection;

/**
 * This interface contains the methods needed to validate all given ranks
 *
 * @author Chickenpowerrr
 * @since 1.2.1
 */
public interface RankHelper {

  /**
   * Returns if the given rank has been synchronized over the platforms
   *
   * @param bot the bot that could synchronize the rank
   * @param rank the rank that could be synchronized
   * @return if the given rank has been synchronized over the platforms
   */
  boolean isSynchronized(Bot bot, Rank rank);

  /**
   * Returns the Ranks based on the Bot that is able to give to users and the name of the service's
   * Rank
   *
   * @param bot the running Bot
   * @param serviceGroupName the name of the Minecraft Rank
   * @return the Ranks based on the Bot that is able to give to users and the name of the Rank
   */
  Collection<Rank> getRanks(Bot bot, String serviceGroupName);

  /**
   * Validates all cached ranks and if they don't exist in the given Bot or service, a message will
   * be send to the console
   */
  void validateRanks();
}
