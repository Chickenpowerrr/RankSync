package com.gmail.chickenpowerrr.ranksync.spigot.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;

/**
 * This class can be used to save the data from the config.yml and use it to sync the ranks
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@Slf4j
public class RankHelper {

  private static final Logger LOGGER = Logger.getLogger(RankHelper.class.getSimpleName());

  private final Map<String, Map<Bot<?, ?>, String>> ranks;

  /**
   * @param ranks a map with the synced ranks, with the key: Minecraft Rank and the value is a map
   * with the Bot that syncs a rank and the value is the synced Rank
   */
  public RankHelper(Map<String, Map<Bot<?, ?>, String>> ranks) {
    this.ranks = ranks;
  }

  /**
   * Returns a Rank based on the Bot that is able to give to users and the name of the Minecraft
   * Rank
   *
   * @param bot the running Bot
   * @param minecraftGroupName the name of the Minecraft Rank
   * @return a Rank based on the Bot that is able to give to users and the name of the Rank
   */
  @SuppressWarnings("unchecked")
  Rank getRank(Bot bot, String minecraftGroupName) {
    if (this.ranks.containsKey(minecraftGroupName) && this.ranks.get(minecraftGroupName)
        .containsKey(bot)) {
      return bot.getRankFactory().getRankFromRole(
          bot.getRankFactory().getRoleFromName(this.ranks.get(minecraftGroupName).get(bot)));
    } else {
      return null;
    }
  }

  /**
   * Validates all cached and if they don't exist in the given Bot or Minecraft server, a message
   * will be send to the console
   */
  public void validateRanks() {
    this.ranks.forEach((minecraftRank, syncedRanks) -> {
      AtomicBoolean minecraftChecked = new AtomicBoolean(false);

      syncedRanks.forEach((bot, syncedRank) -> {
        if (bot.hasCaseSensitiveRanks() && !bot.getAvailableRanks().contains(syncedRank)) {
          log.error(syncedRank + " is not a valid rank on " + bot.getPlatform());
        } else if (bot.getAvailableRanks().stream()
            .noneMatch(ranks -> ranks.equalsIgnoreCase(syncedRank))) {
          log.error(syncedRank + " is not a valid rank on " + bot.getPlatform());
        }

        if (!minecraftChecked.get()) {
          minecraftChecked.set(true);
          if (bot.getEffectiveDatabase().hasCaseSensitiveRanks() && !bot.getEffectiveDatabase()
              .getAvailableRanks().contains(minecraftRank)) {
            log.error(minecraftRank + " is not a valid rank on Minecraft");
          } else if (bot.getEffectiveDatabase().getAvailableRanks().stream()
              .noneMatch(ranks -> ranks.equalsIgnoreCase(minecraftRank))) {
            log.error(minecraftRank + " is not a valid rank on Minecraft");
          }
        }
      });
    });
  }
}
