package com.gmail.chickenpowerrr.ranksync.discord.data;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.data.AbstractFileDatabase;
import com.gmail.chickenpowerrr.ranksync.api.data.Properties;
import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import java.util.Collection;
import java.util.List;
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

  @Getter
  private final RankResource rankResource;
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
   * Returns the Player that represents the link between the two platforms
   *
   * @param playerId the id that represents the player on this service
   * @param constructor the player constructor based on the retrieved data
   * @return a CompletableFuture that will be completed whenever the link has been found
   */
  @Override
  public CompletableFuture<Player> getPlayer(String playerId,
      PlayerConstructor<Player> constructor) {
    return CompletableFuture.supplyAsync(() -> {
      String uuid = super.players.getValue(playerId);
      if (uuid != null) {
        Integer syncRewards = super.players.getValue(uuid + "." + "sync-rewards");
        Integer unsyncRewards = super.players.getValue(uuid + "." + "unsync-rewards");
        return constructor
            .apply(UUID.fromString(uuid), playerId, syncRewards != null ? syncRewards : 0,
                unsyncRewards != null ? unsyncRewards : 0);
      } else {
        return constructor.apply(null, playerId, 0, 0);
      }
    }).exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });
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
        Integer syncRewards = super.players.getValue(uuid.toString() + "." + "sync-rewards");
        super.players.setValue(uuid.toString() + "." + "sync-rewards",
            syncRewards != null ? syncRewards + 1 : 1);
      } else {
        String syncedUuid = super.players.getValue(playerId);
        super.players.removeValue(playerId);
        if (syncedUuid != null) {
          super.players.removeValue(syncedUuid + "." + this.bot.getPlatform());
          Integer unsyncRewards = super.players.getValue(syncedUuid + "." + "unsync-rewards");
          super.players.setValue(syncedUuid + "." + "unsync-rewards",
              unsyncRewards != null ? unsyncRewards + 1 : 1);
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
   * Returns the Player that represents the link between the two platforms
   *
   * @param uuid the id that represents the player on the other service
   * @param constructor the player constructor based on the retrieved data
   * @return a CompletableFuture that will be completed whenever the link has been found
   */
  @Override
  public CompletableFuture<Player> getPlayer(UUID uuid, PlayerConstructor<Player> constructor) {
    return CompletableFuture.supplyAsync(() -> {
      Integer syncRewards = super.players.getValue(uuid + "." + "sync-rewards");
      Integer unsyncRewards = super.players.getValue(uuid + "." + "unsync-rewards");
      return constructor
          .apply(uuid, super.players.getValue(uuid.toString() + "." + this.bot.getPlatform()),
              syncRewards != null ? syncRewards : 0, unsyncRewards != null ? unsyncRewards : 0);
    });
  }

  /**
   * Returns the ranks of the rank resource
   *
   * @param uuid the id that represents the player on the other service
   * @return the ranks of the rank resource
   */
  @Override
  public CompletableFuture<List<Rank>> getRanks(UUID uuid) {
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
