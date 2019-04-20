package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event will get called when a bot is going to shutdown because something went wrong
 *
 * @author Chickenpowerrr
 * @since 1.0.2
 */
@Getter
@AllArgsConstructor
public class BotForceShutdownEvent implements BotEvent {

  private final Bot bot;
  private final String reason;
}
