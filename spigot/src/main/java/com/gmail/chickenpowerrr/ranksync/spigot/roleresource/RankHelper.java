package com.gmail.chickenpowerrr.ranksync.spigot.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RankHelper {

  private static final Logger LOGGER = Logger.getLogger(RankHelper.class.getSimpleName());

  private final Map<String, Map<Bot<?, ?>, String>> ranks;

  public RankHelper(Map<String, Map<Bot<?, ?>, String>> ranks) {
    this.ranks = ranks;
  }

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
