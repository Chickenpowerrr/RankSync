package com.gmail.chickenpowerrr.ranksync.spigot.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

@AllArgsConstructor
public class RankHelper {

  private static final Logger LOGGER = Logger.getLogger(RankHelper.class.getSimpleName());

  private final Map<String, Map<Bot, String>> ranks;

  public void validateRanks(Bot bot) {
    this.ranks.values().stream().map(Map::entrySet).flatMap(Collection::stream)
        .filter(entry -> entry.getKey().equals(bot)).map(Map.Entry::getValue).forEach(rank -> {
      if (bot.getRankFactory().getRoleFromName(rank) == null) {
        LOGGER.severe("The '" + bot.getPlatform() + "' rank '" + rank + "' couldn't be found");
      }
    });

    for (String rank : this.ranks.keySet()) {
      bot.getEffectiveDatabase().isValidRank(rank).thenAccept(valid -> {
        if (!valid) {
          LOGGER.severe("The 'Minecraft' rank '" + rank + "' couldn't be found");
        }
      });
    }
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
}
