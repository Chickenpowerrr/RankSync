package com.gmail.chickenpowerrr.ranksync.discord.bot;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.data.Properties;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class can be used to create a Discord bot
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class BotFactory implements com.gmail.chickenpowerrr.ranksync.api.bot.BotFactory {

  @Getter private static final BotFactory instance = new BotFactory();

  private final Map<Properties, Bot> botCache = new HashMap<>();

  private BotFactory() {
  }

  /**
   * Returns null since a Discord Bot needs Properties
   */
  @Override
  public Bot getBot() {
    return null;
  }

  /**
   * Returns an instance of the Discord Bot. The following Property fields are required:
   *
   * - language_helper (the instance of the language helper)
   * - language (the language of the messages)
   * - token (the private token of the Discord Bot)
   * - guild_id (the Discord guild id)
   * - type (the way the data should be stored)
   *
   * when SQL is used, the following are required:
   *
   * - max_pool_size (the maximum poolsize for SQL)
   * - host (the SQL host)
   * - port (the SQL port)
   * - database (the SQL database)
   * - username (the SQL username)
   * - password (the SQL password)
   *
   * for most of the databases, a rankresource is also required:
   * - rank_resource
   *
   * @param properties the information that the Bot needs in order to set up properly
   * @return an instance of the Discord Bot
   */
  @Override
  public Bot getBot(Properties properties) {
    if (this.botCache.containsKey(properties)) {
      return this.botCache.get(properties);
    } else {
      Bot bot = new DiscordBot(properties);
      this.botCache.put(properties, bot);
      return bot;
    }
  }
}
