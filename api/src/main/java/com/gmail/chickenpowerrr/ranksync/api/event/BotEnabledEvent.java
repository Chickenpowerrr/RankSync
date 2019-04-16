package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event will get called when a bot gets enabled and is ready to do this things it has been
 * created for
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public class BotEnabledEvent implements Event {

  private final Bot bot;
}
