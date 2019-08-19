package com.gmail.chickenpowerrr.ranksync.discord.data;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.data.Database;
import com.gmail.chickenpowerrr.ranksync.api.data.Properties;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;

/**
 * This class can be used to get a data storage for the ranks
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class DatabaseFactory implements com.gmail.chickenpowerrr.ranksync.api.data.DatabaseFactory {

  @Getter private static final Map<Guild, DatabaseFactory> instances = new HashMap<>();

  private final Map<String, Map<Properties, Database>> databaseConstructorCache = new HashMap<>();
  private final Guild guild;
  @Getter private final Bot bot;

  private DatabaseFactory(Bot bot, Guild guild) {
    this.bot = bot;
    this.guild = guild;
  }

  /**
   * Lazily returns an instance based on the Guild the Bot is running on
   *
   * @param bot the Bot that is running
   * @param guild the Guild the Bot is running on
   * @return an instance based on the Guild the Bot is running on
   */
  public static DatabaseFactory getInstance(Bot bot, Guild guild) {
    if (!instances.containsKey(guild)) {
      instances.put(guild, new DatabaseFactory(bot, guild));
    }
    return instances.get(guild);
  }

  /**
   * Lazily returns an instance based on the Guild the Bot is running on
   *
   * @param guild the Guild the Bot is running on
   * @return an instance based on the Guild the Bot is running on
   */
  static DatabaseFactory getInstance(Guild guild) {
    return getInstance(null, guild);
  }

  /**
   * Returns the requested database
   *
   * @param name the name of the database
   * @param credentials the information that is needed to establish a connection with the database
   * @return the requested database
   */
  @Override
  public Database getDatabase(String name, Properties credentials) {
    if (this.databaseConstructorCache.containsKey(name)) {
      if (this.databaseConstructorCache.get(name).containsKey(credentials)) {
        return this.databaseConstructorCache.get(name).get(credentials);
      }
    } else {
      this.databaseConstructorCache.put(name, new HashMap<>());
    }

    Database database;

    if (credentials.has("type")) {
      switch (credentials.getString("type").toLowerCase()) {
        case "sql":
        case "sequel":
        case "mariadb":
        case "mysql":
          database = new SqlDatabase(this.bot, credentials);
          break;
        case "yaml":
        case "file":
        case "yml":
          String basePath = credentials.getString("base_path");
          if (basePath != null) {
            database = new YamlDatabase(this.bot, credentials, basePath);
          } else {
            throw new IllegalStateException("The Yaml resource needs a path");
          }
          break;
        default:
          throw new IllegalStateException("You should add a valid database to the properties");
      }
    } else {
      throw new IllegalStateException("You should add a valid database to the properties");
    }

    this.databaseConstructorCache.get(name).put(credentials, database);
    return database;
  }
}
