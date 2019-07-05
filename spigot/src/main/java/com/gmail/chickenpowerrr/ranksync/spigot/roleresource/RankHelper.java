package com.gmail.chickenpowerrr.ranksync.spigot.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.spigot.language.Translation;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * This class can be used to save the data from the config.yml and use it to sync the ranks
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@Slf4j
public class RankHelper implements com.gmail.chickenpowerrr.ranksync.api.rank.RankHelper {

  private static final Logger LOGGER = Logger.getLogger(RankHelper.class.getSimpleName());

  private final Map<String, Map<Bot<?, ?>, Collection<String>>> ranks;

  /**
   * @param ranks a map with the synced ranks, with the key: Minecraft Rank and the value is a map
   * with the Bot that syncs a rank and the value is the synced Rank
   */
  public RankHelper(Map<String, Map<Bot<?, ?>, Collection<String>>> ranks) {
    this.ranks = ranks;
  }

  /**
   * Returns if the given rank has been synchronized over the platforms
   *
   * @param bot the bot that could synchronize the rank
   * @param rank the rank that could be synchronized
   * @return if the given rank has been synchronized over the platforms
   */
  @Override
  public boolean isSynchronized(Bot bot, Rank rank) {
    return this.ranks.values().stream()
        .anyMatch((map) -> map.containsKey(bot) && map.get(bot).stream()
            .anyMatch(entry -> entry.equalsIgnoreCase(rank.getName())));
  }

  /**
   * Returns the Ranks based on the Bot that is able to give to users and the name of the Minecraft
   * Rank
   *
   * @param bot the running Bot
   * @param minecraftGroupName the name of the Minecraft Rank
   * @return the Ranks based on the Bot that is able to give to users and the name of the Rank
   */
  @SuppressWarnings("unchecked")
  @Override
  public Collection<Rank> getRanks(Bot bot, String minecraftGroupName) {
    if (this.ranks.containsKey(minecraftGroupName) && this.ranks.get(minecraftGroupName)
        .containsKey(bot)) {
      return this.ranks.get(minecraftGroupName).get(bot).stream()
          .map(roleName -> bot.getRankFactory().getRankFromRole(
              bot.getRankFactory().getRoleFromName(roleName))).collect(Collectors.toSet());
    } else {
      return null;
    }
  }

  /**
   * Validates all cached ranks and if they don't exist in the given Bot or Minecraft server, a
   * message will be send to the console
   */
  @Override
  public void validateRanks() {
    this.ranks.forEach((minecraftRank, syncedRanks) -> {
      AtomicBoolean minecraftChecked = new AtomicBoolean(false);

      syncedRanks.forEach((bot, syncedRankNames) ->
          syncedRankNames.forEach(syncedRank -> {
            if (bot.hasCaseSensitiveRanks() && !bot.getAvailableRanks().contains(syncedRank)) {
              log.error(Translation.INVALID_RANK
                  .getTranslation("rank", syncedRank, "platform", bot.getPlatform()));
            } else if (bot.getAvailableRanks().stream()
                .noneMatch(ranks -> ranks.equalsIgnoreCase(syncedRank))) {
              log.error(Translation.INVALID_RANK
                  .getTranslation("rank", syncedRank, "platform", bot.getPlatform()));
            }

            if (!minecraftChecked.get()) {
              minecraftChecked.set(true);
              if (bot.getEffectiveDatabase().hasCaseSensitiveRanks() && !bot.getEffectiveDatabase()
                  .getAvailableRanks().contains(minecraftRank)) {
                log.error(Translation.INVALID_RANK
                    .getTranslation("rank", minecraftRank, "platform", "Minecraft"));
              } else if (bot.getEffectiveDatabase().getAvailableRanks().stream()
                  .noneMatch(ranks -> ranks.equalsIgnoreCase(minecraftRank))) {
                log.error(Translation.INVALID_RANK
                    .getTranslation("rank", minecraftRank, "platform", "Minecraft"));
              }
            }
          }));
    });
  }
}
