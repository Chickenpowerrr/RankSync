package com.gmail.chickenpowerrr.ranksync.api;

import com.gmail.chickenpowerrr.ranksync.api.bot.BotFactory;
import com.gmail.chickenpowerrr.ranksync.api.event.CancelableEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.Event;
import com.gmail.chickenpowerrr.ranksync.api.event.Listener;

/**
 * This Api contains almost all of the methods you'll need to start with the RankSync project. Right
 * here you're able to create a BotFactory or create a new Bot instance
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface RankSyncApi {

  /**
   * Returns the singleton instance of the API
   */
  static RankSyncApi getApi() {
    return RankSyncApiImpl.getInstance();
  }

  /**
   * Returns the instance of a BotFactory based on its name
   *
   * @param resource the name of the BotFactory
   * @return the BotFactory that matches the name given when the BotFactory got added by invoking
   * the #addBotFactory(String, BotFactory) method
   */
  BotFactory getBotFactory(String resource);

  /**
   * Adds a BotFactory to be able to create an instance of a bot made for a specific platform
   *
   * @param resource the name that will be used to get this instance while invoking
   * #getBotFactory(String)
   * @param platformBot the factory that will be used to create new instances for a specific
   * platform
   */
  void addBotFactory(String resource, BotFactory platformBot);

  /**
   * Registers a Listener to make sure it will get invoked when the right event gets called
   *
   * @param listener the object that will handle incoming events
   */
  void registerListener(Listener listener);

  /**
   * Invokes all the registered Listeners
   *
   * @param event the event that should be passed into all the listeners that are listening for this
   * type of events
   * @return the event that was passed into all of the listeners
   */
  CancelableEvent execute(CancelableEvent event);

  /**
   * Invokes all the registered Listeners
   *
   * @param event the event that should be passed into all the listeners that are listening for this
   * type of events
   */
  void execute(Event event);
}
