package com.gmail.chickenpowerrr.ranksync.server.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankHelper;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.Setter;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;

/**
 * This class uses LuckPerms to get a Player's ranks
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class LuckPermsRankResource implements RankResource {

  private final LuckPermsApi api;
  private RankHelper rankHelper = null;
  private final RankSyncServerPlugin rankSyncPlugin;

  @Setter
  private Bot bot;

  /**
   * @param rankSyncPlugin the ranksync plugin
   * @param luckPermsApi the LuckPermsApi instance that is currently used on the server
   * @param bot the Bot that is giving the ranks to players
   */
  public LuckPermsRankResource(RankSyncServerPlugin rankSyncPlugin, LuckPermsApi luckPermsApi,
      Bot bot) {
    this.rankSyncPlugin = rankSyncPlugin;
    this.api = luckPermsApi;
    this.bot = bot;
  }

  /**
   * @param rankSyncPlugin the ranksync plugin
   * @param luckPermsApi the LuckPermsApi instance that is currently used on the server
   */
  public LuckPermsRankResource(RankSyncServerPlugin rankSyncPlugin, LuckPermsApi luckPermsApi) {
    this(rankSyncPlugin, luckPermsApi, null);
  }

  /**
   * Requests the ranks for a given Player
   *
   * @param uuid the UUID that represents the Player
   * @return a {@code CompletableFuture} that will be completed whenever the ranks have been found
   */
  @Override
  public CompletableFuture<Collection<Rank>> getRanks(UUID uuid) {
    CompletableFuture<Collection<Rank>> completableFuture = new CompletableFuture<>();

    if (this.rankHelper == null) {
      this.rankHelper = this.rankSyncPlugin.getRankHelper();
    }

    this.api.getUserManager().loadUser(uuid).thenAcceptAsync(user ->
        completableFuture.complete(
            user.getOwnNodes().stream()
                .filter(Node::isGroupNode)
                .map(Node::getGroupName)
                .map(groupName -> this.rankHelper.getRanks(this.bot, groupName))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull).collect(Collectors.toSet())));

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

  /**
   * Returns if the ranks are case sensitive when they are requested by their name
   *
   * @return false
   */
  @Override
  public boolean hasCaseSensitiveRanks() {
    return false;
  }
}
