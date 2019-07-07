package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import lombok.Getter;

/**
 * This event will get called when a bot is going to shutdown because something went wrong
 *
 * @author Chickenpowerrr
 * @since 1.0.2
 */
@Getter
public class BotForceShutdownEvent implements BotEvent {

  private final Bot bot;
  private final String[] reason;

  /**
   * @param bot the bot which gets shutdown
   * @param reason the reason why the bot needs to stop
   */
  public BotForceShutdownEvent(Bot bot, String... reason) {
    this.bot = bot;
    this.reason = reason;
  }
}
