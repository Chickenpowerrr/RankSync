package com.gmail.chickenpowerrr.ranksync.discord.data;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.data.Database;
import com.gmail.chickenpowerrr.ranksync.api.data.Properties;
import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import com.gmail.chickenpowerrr.ranksync.api.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.discord.language.Translation;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class uses a SQL server to save the synchronization data
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@Slf4j
public class SqlDatabase implements Database {

  private final HikariDataSource dataSource;
  private final RankResource rankResource;
  private final Bot<?, ?> bot;

  /**
   * Updates the database tables and sets up the connection
   *
   * @param bot the Discord bot
   * @param properties the credentials for the SQL server
   */
  SqlDatabase(Bot bot, Properties properties) {
    this.bot = bot;

    if (properties.has("max_pool_size", "host", "port", "database", "username", "password")) {
      this.dataSource = new HikariDataSource();

      this.dataSource.setMaximumPoolSize(properties.getInt("max_pool_size"));
      this.dataSource.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
      this.dataSource.addDataSourceProperty("serverName", properties.getString("host"));
      this.dataSource.addDataSourceProperty("port", properties.getInt("port"));
      this.dataSource.addDataSourceProperty("databaseName", properties.getString("database"));
      this.dataSource.addDataSourceProperty("user", properties.getString("username"));
      this.dataSource.addDataSourceProperty("password", properties.getString("password"));
      this.dataSource.addDataSourceProperty("useSSL", false);
      this.dataSource.addDataSourceProperty("serverTimezone", "UTC");

      this.dataSource.setConnectionTimeout(TimeUnit.SECONDS.toMillis(10));
    } else {
      throw new IllegalStateException(
          "Not all of the required properties for an SQL database have been entered");
    }

    if (properties.has("rank_resource")) {
      this.rankResource = (RankResource) properties.getObject("rank_resource");
    } else {
      throw new IllegalStateException("This resource needs a rank resource");
    }

    try (Connection connection = this.dataSource.getConnection();
        PreparedStatement preparedStatement = connection
            .prepareStatement("INSERT IGNORE bot (platform) VALUES (?);")) {
      DatabaseUpdater databaseUpdater = new DatabaseUpdater(connection);
      databaseUpdater.update(databaseUpdater.getVersion());

      preparedStatement.setString(1, this.bot.getPlatform());
      preparedStatement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
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
   * Returns the Player that represents the link between the two platforms
   *
   * @param uuid the id that represents the player on the other service
   * @param constructor the player constructor based on the retrieved data
   * @return the Player linked to the UUID
   */
  @Override
  public CompletableFuture<Player> getPlayer(UUID uuid, PlayerConstructor<Player> constructor) {
    return CompletableFuture.supplyAsync(() -> {
      try (Connection connection = this.dataSource.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(
              "SELECT sp.identifier, p.sync_rewards, p.unsync_rewards "
                  + "FROM player p "
                  + "LEFT JOIN synced_players sp ON p.id = sp.player_id "
                  + "JOIN bot b ON sp.bot_id = b.id "
                  + "WHERE p.uuid = ? AND b.platform = ?;")) {
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, this.bot.getPlatform());
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
            return constructor.apply(uuid, resultSet.getString("identifier"),
                resultSet.getInt("sync_rewards"), resultSet.getInt("unsync_rewards"));
          } else {
            return constructor.apply(uuid, null, 0, 0);
          }
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }).exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });
  }

  /**
   * Returns the Player that represents the link between the two platforms
   *
   * @param playerId the Discord identifier
   * @param constructor the player constructor based on the retrieved data
   * @return a CompletableFuture that will be completed whenever the link has been found
   */
  @Override
  public CompletableFuture<Player> getPlayer(String playerId,
      PlayerConstructor<Player> constructor) {
    return CompletableFuture.supplyAsync(() -> {
      try (Connection connection = this.dataSource.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(
              "SELECT p.uuid, p.sync_rewards, p.unsync_rewards "
                  + "FROM player p "
                  + "LEFT JOIN synced_players sp ON p.id = sp.player_id "
                  + "JOIN bot b ON sp.bot_id = b.id "
                  + "WHERE sp.identifier = ? "
                  + "AND sp.bot_id = (SELECT id FROM bot WHERE b.platform = ?);")) {
        preparedStatement.setString(1, playerId);
        preparedStatement.setString(2, this.bot.getPlatform());
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          if (resultSet.next()) {
            return constructor.apply(UUID.fromString(resultSet.getString("uuid")), playerId,
                resultSet.getInt("sync_rewards"), resultSet.getInt("unsync_rewards"));
          } else {
            return constructor.apply(null, playerId, 0, 0);
          }
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
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
    return this.bot.getPlayerFactory().setUuid(playerId, uuid).thenAcceptAsync(a -> {
      if (uuid != null) {
        try (Connection connection = this.dataSource.getConnection();
            PreparedStatement createPlayer = connection
                .prepareStatement("INSERT INTO player (uuid, sync_rewards, unsync_rewards) "
                    + "VALUES (?, 0, 0) ON DUPLICATE KEY UPDATE sync_rewards = sync_rewards + 1;");
            PreparedStatement saveUuid = connection.prepareStatement(
                "INSERT INTO synced_players (bot_id, identifier, player_id) "
                    + "VALUES ((SELECT id FROM bot WHERE platform = ?), ?, "
                    + "(SELECT id FROM player WHERE uuid = ?));")) {
          createPlayer.setString(1, uuid.toString());
          createPlayer.execute();

          saveUuid.setString(1, this.bot.getPlatform());
          saveUuid.setString(2, playerId);
          saveUuid.setString(3, uuid.toString());
          saveUuid.execute();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      } else {
        try (Connection connection = this.dataSource.getConnection();
            PreparedStatement unLink = connection.prepareStatement(
                "DELETE FROM synced_players "
                    + "WHERE identifier = ? "
                    + "AND bot_id = (SELECT id FROM bot WHERE platform = ?);");
            PreparedStatement update = connection.prepareStatement("UPDATE player "
                + "SET unsync_rewards = unsync_rewards + 1 "
                + "WHERE id = (SELECT player_id FROM synced_players "
                + "WHERE identifier = ? "
                + "AND bot_id = (SELECT id FROM bot WHERE platform = ?));")) {
          update.setString(1, playerId);
          update.setString(2, this.bot.getPlatform());
          update.execute();

          unLink.setString(1, playerId);
          unLink.setString(2, this.bot.getPlatform());
          unLink.execute();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }).exceptionally(throwable -> {
      throwable.printStackTrace();
      return null;
    });
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
   * This class updates the database to the right version
   *
   * @author Chickenpowerrr
   * @since 1.2.0
   */
  @AllArgsConstructor
  private class DatabaseUpdater {

    private final Connection connection;

    /**
     * Updates the database to the current version of data storage
     *
     * @param version the version the database has been designed for
     */
    private void update(String version) {
      try (Statement statement = this.connection.createStatement()) {
        if (version != null) {
          switch (version) {
            case "1.1.0":
              statement
                  .execute("CREATE TABLE IF NOT EXISTS version (version VARCHAR(10) NOT NULL);");
              statement.execute("ALTER TABLE bot RENAME COLUMN name TO platform;");
              statement.execute("UPDATE bot SET platform = 'Discord';");
              // Fall through
            case "1.2.0":
              statement.execute("ALTER TABLE player "
                  + "ADD COLUMN sync_rewards INT UNSIGNED NOT NULL DEFAULT 0;");
              statement.execute("ALTER TABLE player "
                  + "ADD COLUMN unsync_rewards INT UNSIGNED NOT NULL DEFAULT 0;");

              statement
                  .execute("create table if not exists version (version varchar(10) not null);");
              statement.execute("DELETE FROM version");
              statement.execute("INSERT INTO version (version) VALUES ('1.4.0');");

              generateDefaultTables();
              break;
            case "1.4.0":
              // Up to date!
              generateDefaultTables();
              break;
            default:
              log.error(Translation.DATABASE_VERSION_UNKNOWN.getTranslation("version", version));
              break;
          }
        } else {
          generateDefaultTables();
          statement.execute("INSERT INTO version (version) VALUES ('1.4.0');");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    /**
     * Generates the default tables
     */
    private void generateDefaultTables() {
      try (Statement statement = this.connection.createStatement()) {
        statement.execute("create table if not exists  bot" +
            "(" +
            "  id   int unsigned auto_increment" +
            "    primary key," +
            "  platform varchar(64) not null," +
            "  constraint bot_name_uindex" +
            "  unique (platform)" +
            ");");

        statement.execute("create table if not exists player" +
            "(" +
            "  id   int unsigned auto_increment" +
            "    primary key," +
            "  uuid           char(36) not null," +
            "  sync_rewards   int unsigned not null default 0," +
            "  unsync_rewards int unsigned not null default 0," +
            "  constraint player_uuid_uindex" +
            "  unique (uuid)" +
            ");");

        statement.execute("create table if not exists synced_players" +
            "(" +
            "  id         int unsigned auto_increment" +
            "    primary key," +
            "  bot_id     int unsigned not null," +
            "  identifier tinytext     not null," +
            "  player_id  int unsigned not null," +
            "  constraint synced_players_bot_id_fk" +
            "  foreign key (bot_id) references bot (id)" +
            "    on update cascade" +
            "    on delete cascade," +
            "  constraint synced_players_player_id_fk" +
            "  foreign key (player_id) references player (id)" +
            "    on update cascade" +
            "    on delete cascade" +
            ");");

        statement
            .execute("create table if not exists version (version varchar(10) not null);");
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    /**
     * Returns the version the database has been designed for
     */
    private String getVersion() {
      try (Statement statement = this.connection.createStatement();
          ResultSet versionTable = statement.executeQuery("SHOW TABLES LIKE 'version';")) {

        if (versionTable.next()) {
          try (ResultSet version = statement.executeQuery("SELECT version FROM version;")) {
            if (version.next()) {
              return version.getString("version");
            } else {
              return "1.2.0";
            }
          }
        } else {
          try (ResultSet botTable = statement.executeQuery("SHOW TABLES LIKE 'bot';")) {
            if (botTable.next()) {
              return "1.1.0";
            } else {
              return null;
            }
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
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
