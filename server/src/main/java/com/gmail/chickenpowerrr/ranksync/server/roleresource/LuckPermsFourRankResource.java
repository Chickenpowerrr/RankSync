package com.gmail.chickenpowerrr.ranksync.server.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankHelper;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;

/**
 * This class uses LuckPerms to get a Player's ranks
 *
 * @author Chickenpowerrr
 * @since 1.4.0
 */
public class LuckPermsFourRankResource extends LuckPermsRankResource {

  private final LuckPermsApi api;
  private RankHelper rankHelper = null;

  /**
   * @param rankSyncPlugin the ranksync plugin
   * @param bot the Bot that is giving the ranks to players
   */
  public LuckPermsFourRankResource(RankSyncServerPlugin rankSyncPlugin, Bot bot) {
    super(rankSyncPlugin, bot, true);

    this.api = LuckPerms.getApi();
  }

  /**
   * @param rankSyncPlugin the ranksync plugin
   */
  public LuckPermsFourRankResource(RankSyncServerPlugin rankSyncPlugin) {
    this(rankSyncPlugin, null);
  }

  /**
   * Requests the ranks for a given Player
   *
   * @param uuid the UUID that represents the Player
   * @return a {@code CompletableFuture} that will be completed whenever the ranks have been found
   */
  @Override
  public CompletableFuture<List<Rank>> getRanks(UUID uuid) {
    CompletableFuture<List<Rank>> completableFuture = new CompletableFuture<>();

    if (this.rankHelper == null) {
      this.rankHelper = getRankSyncPlugin().getRankHelper();
    }

    this.api.getUserManager().loadUser(uuid).thenAcceptAsync(user ->
        completableFuture.complete(
            user.getOwnNodes().stream()
                .filter(Node::isGroupNode)
                .map(Node::getGroupName)
                .map(groupName -> this.rankHelper.getRanks(getBot(), groupName))
                .flatMap(Collection::stream)
                .distinct()
                .filter(Objects::nonNull).collect(Collectors.toList())));

    completableFuture.exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });

    return completableFuture;
  }

  /**
   * Checks if the given rank is supported by this Resource
   *
   * @param name the name of the rank
   * @return true if the rank is supported, false if it's not
   */
  @Override
  public boolean isValidRank(String name) {
    return this.api.getGroup(name) != null;
  }

  /**
   * Returns all of the ranks the database contains
   */
  @Override
  public Collection<String> getAvailableRanks() {
    return this.api.getGroups().stream().map(me.lucko.luckperms.api.Group::getName)
        .collect(Collectors.toSet());
  }
}
