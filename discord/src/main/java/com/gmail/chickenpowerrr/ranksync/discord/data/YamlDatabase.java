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

/**
 * This class uses a SQL server to save the synchronization data
 *
 * @author Chickenpowerrr
 * @since 1.2.0
 */
public class YamlDatabase extends AbstractFileDatabase<YamlFile> {

  @Getter private final RankResource rankResource;
  private final Bot<?, ?> bot;

  /**
   * @param bot the Discord Bot that's currently running
   * @param properties contains the rank resource
   * @param basePath the path to the file
   */
  public YamlDatabase(Bot bot, Properties properties, String basePath) {
    super(properties, new YamlFile(basePath, "players"));

    this.bot = bot;

    if (properties.has("rank_resource")) {
      this.rankResource = (RankResource) properties.getObject("rank_resource");
    } else {
      throw new IllegalStateException("This resource needs a rank resource");
    }
  }

  /**
   * Returns the id that represents a player on the other service by Discord identifier
   *
   * @param playerId the Discord identifier
   * @return a CompletableFuture that will be completed whenever the id of the other service has
   * been found
   */
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

  /**
   * Sets the id that represents a player on the other service by their Discord identifier
   *
   * @param playerId the Discord identifier
   * @param uuid the id that represents the player on the other service
   * @return a CompletableFuture that will be completed whenever the id of the other service has
   * been linked to this service
   */
  @Override
  public CompletableFuture<Void> setUuid(String playerId, UUID uuid) {
    CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
      if (uuid != null) {
        super.players.setValue(playerId, uuid.toString());
        super.players.setValue(uuid.toString() + "." + this.bot.getPlatform(), playerId);
      } else {
        String syncedUuid = super.players.getValue(playerId);
        super.players.removeValue(playerId);
        if (syncedUuid != null) {
          super.players.removeValue(syncedUuid + "." + this.bot.getPlatform());
        }
      }
      super.players.save();
      return null;
    });

    completableFuture.exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });
    return completableFuture;
  }

  /**
   * Returns the Discord identifier linked to the UUID
   *
   * @param uuid the id that represents the player on the other service
   * @return the Discord identifier linked to the UUID
   */
  @Override
  public CompletableFuture<String> getPlayerId(UUID uuid) {
    return CompletableFuture
        .supplyAsync(() -> super.players.getValue(uuid.toString() + "." + this.bot.getPlatform()));
  }

  /**
   * Returns the ranks of the rank resource
   *
   * @param uuid the id that represents the player on the other service
   * @return the ranks of the rank resource
   */
  @Override
  public CompletableFuture<Collection<Rank>> getRanks(UUID uuid) {
    return this.rankResource.getRanks(uuid);
  }

  /**
   * Returns if the rank is a valid rank according to the rank resource
   *
   * @param rankName the name of the Rank
   * @return if the rank is a valid rank according to the rank resource
   */
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

  /**
   * Returns all of the ranks the rank resource contains
   */
  @Override
  public Collection<String> getAvailableRanks() {
    return this.rankResource.getAvailableRanks();
  }

  /**
   * Returns if the ranks are case sensitive when they are requested by their name
   */
  @Override
  public boolean hasCaseSensitiveRanks() {
    return this.rankResource.hasCaseSensitiveRanks();
  }
}
