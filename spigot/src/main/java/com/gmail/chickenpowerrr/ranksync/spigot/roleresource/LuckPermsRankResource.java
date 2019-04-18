package com.gmail.chickenpowerrr.ranksync.spigot.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.Setter;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class uses LuckPerms to get a Player's ranks
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class LuckPermsRankResource implements RankResource {

  private final LuckPermsApi api;
  @Setter private Bot bot;
  private RankHelper rankHelper = null;

  /**
   * @param luckPermsApi the LuckPermsApi instance that is currently used on the server
   * @param bot the Bot that is giving the ranks to players
   */
  public LuckPermsRankResource(LuckPermsApi luckPermsApi, Bot bot) {
    this.api = luckPermsApi;
    this.bot = bot;
  }

  /**
   * @param luckPermsApi the LuckPermsApi instance that is currently used on the server
   */
  public LuckPermsRankResource(LuckPermsApi luckPermsApi) {
    this(luckPermsApi, null);
  }

  @Override
  public CompletableFuture<Collection<Rank>> getRanks(UUID uuid) {
    CompletableFuture<Collection<Rank>> completableFuture = new CompletableFuture<>();

    if (this.rankHelper == null) {
      this.rankHelper = JavaPlugin.getPlugin(RankSyncPlugin.class).getRankHelper();
    }

    this.api.getUserManager().loadUser(uuid).thenAcceptAsync(user ->
        completableFuture.complete(
            user.getOwnNodes().stream().filter(Node::isGroupNode).map(Node::getGroupName)
                .map(groupName -> this.rankHelper.getRank(this.bot, groupName))
                .filter(Objects::nonNull).collect(Collectors.toSet())));

    completableFuture.exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });

    return completableFuture;
  }

  @Override
  public boolean isValidRank(String name) {
    return this.api.getGroup(name) != null;
  }

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
