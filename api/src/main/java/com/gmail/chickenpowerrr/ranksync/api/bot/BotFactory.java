package com.gmail.chickenpowerrr.ranksync.api.bot;

import com.gmail.chickenpowerrr.ranksync.api.data.Properties;

/**
 * This interface can be used to create instances for a bot on any platform this factory supports
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface BotFactory {

  /**
   * Returns the instance of a basic bot
   */
  Bot getBot();

  /**
   * Creates an instance if this bot doesn't exists, if it does exist, you'll get the cached
   * instance
   *
   * @param properties the information that the Bot needs in order to set up properly
   * @return an instance based on the given properties
   */
  Bot getBot(Properties properties);
}
