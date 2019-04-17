package com.gmail.chickenpowerrr.ranksync.spigot.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import lombok.Setter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class VaultRankResource implements RankResource {

  private final Permission permission;
  @Setter private Bot bot;
  private RankHelper rankHelper = null;

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
            .map(groupName -> this.rankHelper.getRank(this.bot, groupName)).filter(Objects::nonNull)
            .collect(Collectors.toSet()));

    completableFuture.exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });

    return completableFuture;
  }

  @Override
  public boolean isValidRank(String name) {
    return Arrays.asList(this.permission.getGroups()).contains(name);
  }

  @Override
  public Collection<String> getAvailableRanks() {
    return Arrays.asList(this.permission.getGroups());
  }

  @Override
  public boolean hasCaseSensitiveRanks() {
    return false;
  }
}
