package com.gmail.chickenpowerrr.ranksync.spigot.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankHelper;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.Setter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class uses Vault to get a Player's ranks
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class VaultRankResource implements RankResource {

  private final Permission permission;
  private RankHelper rankHelper = null;

  @Setter
  private Bot bot;

  /**
   * @param permission the Vault object used for permissions
   */
  public VaultRankResource(Permission permission) {
    this.permission = permission;
  }

  @Override
  public CompletableFuture<Collection<Rank>> getRanks(UUID uuid) {
    if (this.rankHelper == null) {
      this.rankHelper = JavaPlugin.getPlugin(RankSyncPlugin.class).getRankHelper();
    }

    CompletableFuture<Collection<Rank>> completableFuture = CompletableFuture.supplyAsync(
        () -> Arrays.stream(this.permission
            .getPlayerGroups(Bukkit.getWorlds().get(0).getName(), Bukkit.getOfflinePlayer(uuid)))
            .map(groupName -> this.rankHelper.getRanks(this.bot, groupName))
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet()));

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
    return Arrays.asList(this.permission.getGroups()).contains(name);
  }

  /**
   * Returns all of the ranks the database contains
   */
  @Override
  public Collection<String> getAvailableRanks() {
    return Arrays.asList(this.permission.getGroups());
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
