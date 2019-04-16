package com.gmail.chickenpowerrr.ranksync.api;

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
   * Get the singleton instance of the API
   *
   * @return the singleton instance
   */
  static RankSyncApi getApi() {
    return RankSyncApiImpl.getInstance();
  }

  /**
   * Get the instance of a {@code BotFactory} based on its name
   *
   * @param resource the name of the {@code BotFactory}
   * @return the {@code BotFactory} that matches the name given when the {@code BotFactory} got
   * added by invoking the {@code #addBotFactory(String, BotFactory)} method
   */
  BotFactory getBotFactory(String resource);

  /**
   * Add a {@code BotFactory} to be able to create an instance of a bot made for a specific
   * platform
   *
   * @param resource the name that will be used to get this instance while invoking {@code
   * #getBotFactory(String)}
   * @param platformBot the factory that will be used to create new instances for a specific
   * platform
   */
  void addBotFactory(String resource, BotFactory platformBot);

  /**
   * Register a {@code Listener} to make sure it will get invoked when the right event gets called
   *
   * @param listener the object that will handle incoming events
   */
  void registerListener(Listener listener);

  /**
   * Invoke all the registered Listeners
   *
   * @param event the event that should be passed into all the listeners that are listening for this
   * type of events
   * @return the event that was passed into all of the listeners
   */
  CancelableEvent execute(CancelableEvent event);

  /**
   * Invoke all the registered Listeners
   *
   * @param event the event that should be passed into all the listeners that are listening for this
   * type of events
   */
  void execute(Event event);
}
