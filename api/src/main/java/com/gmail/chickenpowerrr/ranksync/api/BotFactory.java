package com.gmail.chickenpowerrr.ranksync.api;

/**
 * This interface can be used to create instances for
 * a bot on any platform this factory supports
 *
 * @author Chickenpowerrr
 * @since  1.0.0
 */
public interface BotFactory {

    /**
     * Get the instance of a basic bot
     *
     * @return the instance of a normal bot
     */
    Bot getBot();

    /**
     * Create an instance if this bot doesn't exists, if it does exist,
     * you'll get the cached instance
     *
     * @param properties the information that the Bot needs in order
     *                   to set up properly
     * @return an instance based on the given properties
     */
    Bot getBot(Properties properties);
}
