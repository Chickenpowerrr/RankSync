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
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;

/**
 * This class uses LuckPerms to get a Player's ranks
 *
 * @author Chickenpowerrr
 * @since 1.4.0
 */
public class LuckPermsFiveRankResource extends LuckPermsRankResource {

  private final LuckPerms api;
  private RankHelper rankHelper;

  /**
   * @param rankSyncPlugin the ranksync plugin
   * @param bot the Bot that is giving the ranks to players
   */
  public LuckPermsFiveRankResource(RankSyncServerPlugin rankSyncPlugin, Bot bot) {
    super(rankSyncPlugin, bot, true);

    this.api = LuckPermsProvider.get();
    this.rankHelper = null;
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
            user.getDistinctNodes().stream()
                .filter(NodeType.INHERITANCE::matches)
                .map(NodeType.INHERITANCE::cast)
                .map(InheritanceNode::getGroupName)
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
    return this.api.getGroupManager().getGroup(name) != null;
  }

  /**
   * Returns all of the ranks the database contains
   */
  @Override
  public Collection<String> getAvailableRanks() {
    try {
      this.api.getGroupManager().loadAllGroups().get();
      return this.api.getGroupManager().getLoadedGroups().stream()
          .map(Group::getName)
          .collect(Collectors.toSet());
    } catch (ExecutionException | InterruptedException e) {
      throwAs(e);
      // Never reached
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private <T extends Throwable> void throwAs(Throwable t) throws T {
    throw (T) t;
  }
}
