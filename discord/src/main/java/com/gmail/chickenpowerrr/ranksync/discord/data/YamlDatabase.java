package com.gmail.chickenpowerrr.ranksync.discord.data;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.data.AbstractFileDatabase;
import com.gmail.chickenpowerrr.ranksync.api.data.Properties;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;

public class YamlDatabase extends AbstractFileDatabase<YamlFile> {

  @Getter
  private final RankResource rankResource;
  private final Bot<?, ?> bot;

  public YamlDatabase(Bot bot, Properties properties, String basePath) {
    super(properties, new YamlFile(basePath, "players"));

    this.bot = bot;

    if (properties.has("rank_resource")) {
      this.rankResource = (RankResource) properties.getObject("rank_resource");
    } else {
      throw new IllegalStateException("This resource needs a rank resource");
    }
  }

  @Override
  public CompletableFuture<UUID> getUuid(String playerId) {
    CompletableFuture<UUID> completableFuture = CompletableFuture
        .supplyAsync(() -> {
          String uuid = super.players.getValue(playerId);
          return uuid == null ? null : UUID.fromString(uuid);
        });
    completableFuture.exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });
    return completableFuture;
  }

  @Override
  public CompletableFuture<Void> setUuid(String playerId, UUID uuid) {
    CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
      if (uuid != null) {
        super.players.setValue(playerId, uuid.toString());
        super.players.setValue(uuid.toString() + "." + this.bot.getPlatform(), playerId);
        super.players.save();
      } else {
        String syncedUuid = super.players.getValue(playerId);
        super.players.removeValue(playerId);
        if (syncedUuid != null) {
          super.players.removeValue(syncedUuid + "." + this.bot.getPlatform());
        }
      }
      return null;
    });

    completableFuture.exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });
    return completableFuture;
  }

  @Override
  public CompletableFuture<String> getPlayerId(UUID uuid) {
    return CompletableFuture
        .supplyAsync(() -> super.players.getValue(uuid.toString() + "." + this.bot.getPlatform()));
  }

  @Override
  public CompletableFuture<Collection<Rank>> getRanks(UUID uuid) {
    return this.rankResource.getRanks(uuid);
  }

  @Override
  public CompletableFuture<Boolean> isValidRank(String rankName) {
    CompletableFuture<Boolean> future = CompletableFuture
        .supplyAsync(() -> this.rankResource.isValidRank(rankName));

    future.exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });

    return future;
  }

  @Override
  public Collection<String> getAvailableRanks() {
    return this.rankResource.getAvailableRanks();
  }

  @Override
  public boolean hasCaseSensitiveRanks() {
    return this.rankResource.hasCaseSensitiveRanks();
  }
}
