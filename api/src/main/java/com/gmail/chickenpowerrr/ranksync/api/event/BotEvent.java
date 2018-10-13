package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Bot;

/**
 * This interface will be implemented by events that
 * are invoked because the Bot is doing things
 *
 * @author Chickenpowerrr
 * @since  1.0.0
 */
public interface BotEvent extends Event {

    /**
     * Get the bot that did something
     *
     * @return the bot that did something
     */
    Bot getBot();
}
