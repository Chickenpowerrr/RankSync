package com.gmail.chickenpowerrr.ranksync.discord.data;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.data.Database;
import com.gmail.chickenpowerrr.ranksync.api.data.Properties;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class DatabaseFactory implements com.gmail.chickenpowerrr.ranksync.api.data.DatabaseFactory {

  @Getter private static final Map<Guild, DatabaseFactory> instances = new HashMap<>();

  private final Map<String, Map<Properties, Database>> databaseConstructorCache = new HashMap<>();
  private final Guild guild;
  @Getter private final Bot bot;

  private DatabaseFactory(Bot bot, Guild guild) {
    this.bot = bot;
    this.guild = guild;
  }

  public static DatabaseFactory getInstance(Bot bot, Guild guild) {
    if (!instances.containsKey(guild)) {
      instances.put(guild, new DatabaseFactory(bot, guild));
    }
    return instances.get(guild);
  }

  static DatabaseFactory getInstance(Guild guild) {
    return getInstance(null, guild);
  }

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
          database = new SqlDatabase(this.bot, credentials);
          break;
        case "yaml":
          String basePath = credentials.getString("base_path");
          if (basePath != null) {
            database = new YamlDatabase(credentials, basePath);
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
