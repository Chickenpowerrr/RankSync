package com.gmail.chickenpowerrr.ranksync.server.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * This class uses LuckPerms to get a Player's ranks
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class LuckPermsRankResource implements RankResource {

  private final RankResource rankResource;

  @Getter(value = AccessLevel.PROTECTED)
  private final RankSyncServerPlugin rankSyncPlugin;

  @Getter(value = AccessLevel.PROTECTED)
  private Bot bot;

  /**
   * @param rankSyncPlugin the ranksync plugin
   * @param bot the Bot that is giving the ranks to players
   */
  public LuckPermsRankResource(RankSyncServerPlugin rankSyncPlugin, Bot bot) {
    this.rankSyncPlugin = rankSyncPlugin;
    this.bot = bot;

    boolean lpFive = true;
    try {
      Class.forName("net.luckperms.api.LuckPerms");
    } catch (ClassNotFoundException e) {
      lpFive = false;
    }

    this.rankResource = lpFive ? new LuckPermsFiveRankResource(rankSyncPlugin, bot)
        : new LuckPermsFourRankResource(rankSyncPlugin, bot);
  }

  /**
   * @param rankSyncPlugin the ranksync plugin
   */
  public LuckPermsRankResource(RankSyncServerPlugin rankSyncPlugin) {
    this(rankSyncPlugin, null);
  }

  protected LuckPermsRankResource(RankSyncServerPlugin rankSyncPlugin, Bot bot, boolean a) {
    this.rankResource = null;
    this.bot = bot;
    this.rankSyncPlugin = rankSyncPlugin;
  }

  @Override
  public void setBot(Bot bot) {
    if (this.rankResource != null) {
      this.rankResource.setBot(bot);
    }
    this.bot = bot;
  }

  /**
   * Requests the ranks for a given Player
   *
   * @param uuid the UUID that represents the Player
   * @return a {@code CompletableFuture} that will be completed whenever the ranks have been found
   */
  @Override
  public CompletableFuture<List<Rank>> getRanks(UUID uuid) {
    return this.rankResource.getRanks(uuid);
  }

  /**
   * Checks if the given rank is supported by this Resource
   *
   * @param name the name of the rank
   * @return true if the rank is supported, false if it's not
   */
  @Override
  public boolean isValidRank(String name) {
    return this.rankResource.isValidRank(name);
  }

  /**
   * Returns all of the ranks the database contains
   */
  @Override
  public Collection<String> getAvailableRanks() {
    System.out.println("Available: " + this.rankResource.getAvailableRanks());
    return this.rankResource.getAvailableRanks();
  }

  /**
   * Returns if the ranks are case sensitive when they are requested by their name
   *
   * @return false
   */
  @Override
  public boolean hasCaseSensitiveRanks() {
    return true;
  }
}
