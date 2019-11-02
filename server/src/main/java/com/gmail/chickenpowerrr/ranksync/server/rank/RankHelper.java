package com.gmail.chickenpowerrr.ranksync.server.rank;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.link.Link;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.server.language.Translation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
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

  private final Map<String, List<Link>> ranks;

  /**
   * @param ranks all ranks that have been synchronized between the Minecraft and the platform
   */
  public RankHelper(List<Link> ranks) {
    this.ranks = new LinkedHashMap<>();
    ranks.forEach(link -> link.getMinecraftRanks().forEach(minecraftRank -> {
      if (!this.ranks.containsKey(minecraftRank)) {
        this.ranks.put(minecraftRank, new ArrayList<>());
      }
      this.ranks.get(minecraftRank).add(link);
    }));
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
        .flatMap(Collection::stream)
        .anyMatch(link -> link.getBot().equals(bot) && link.getPlatformRanks().stream()
            .anyMatch(name -> name.equalsIgnoreCase(rank.getName())));
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
    if (this.ranks.containsKey(minecraftGroupName)) {
      return this.ranks.get(minecraftGroupName).stream()
          .filter(link -> link.getBot().equals(bot))
          .map(Link::getPlatformRanks)
          .flatMap(Collection::stream)
          .map(roleName -> bot.getRankFactory().getRankFromRole(
              bot.getRankFactory().getRoleFromName(roleName)))
          .distinct()
          .collect(Collectors.toList());
    } else {
      return null;
    }
  }

  /**
   * Returns all ranks synchronized by the bot
   *
   * @param bot the running bot
   * @return all ranks synchronized by the bot
   */
  @SuppressWarnings("unchecked")
  @Override
  public Collection<Rank> getRanks(Bot bot) {
    return this.ranks.values().stream()
        .flatMap(Collection::stream)
        .filter(link -> link.getBot().equals(bot))
        .flatMap(link -> link.getPlatformRanks().stream())
        .map(roleName -> bot.getRankFactory().getRankFromRole(
            bot.getRankFactory().getRoleFromName(roleName)))
        .distinct()
        .collect(Collectors.toList());
  }

  /**
   * Validates all cached ranks and if they don't exist in the given Bot or Minecraft server, a
   * message will be send to the console
   */
  @Override
  public void validateRanks() {
    this.ranks.forEach((minecraftRank, syncedRanks) -> {
      AtomicBoolean minecraftChecked = new AtomicBoolean(false);

      syncedRanks.forEach(link -> link.getPlatformRanks().forEach(syncedRank -> {
            Bot<?, ?> bot = link.getBot();

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
          })
      );
    });
  }
}
