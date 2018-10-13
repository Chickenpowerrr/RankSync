package com.gmail.chickenpowerrr.ranksync.api;

import com.gmail.chickenpowerrr.ranksync.api.event.CancelableEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.Event;
import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * This class is the implementation of the RankSyncApi
 * interface. The singleton instance of this class can
 * be accessed by invoking <code>com.gmail.chickenpowerrr.ranksync.api.RankSyncApi.getApi()</code>
 * please DON'T invoke this class directly, just use {@code RankSyncApi#getApi}
 *
 * @author Chickenpowerrr
 * @since  1.0.0
 */
final class RankSyncApiImpl implements RankSyncApi {

    @Getter(value = AccessLevel.MODULE) private static final RankSyncApi instance = new RankSyncApiImpl();

    private final Map<String, BotFactory> botCache = new HashMap<>();
    private final Map<Class<? extends Event>, Collection<Listener>> listeners = new HashMap<>();

    private RankSyncApiImpl(){}

    /**
     * Get the instance of a {@code BotFactory} based on its name
     *
     * @param resource the name of the {@code BotFactory}
     * @return the {@code BotFactory} that matches the name given
     *         when the {@code BotFactory} got added by invoking the
     *         {@code #addBotFactory(String, BotFactory)} method
     */
    @Override
    public BotFactory getBotFactory(String resource) {
        return this.botCache.get(resource);
    }

    /**
     * Add a {@code BotFactory} to be able to create an instance of
     * a bot made for a specific platform
     *
     * @param resource    the name that will be used to get this instance
     *                    while invoking {@code #getBotFactory(String)}
     * @param platformBot the factory that will be used to create new
     *                    instances for a specific platform
     */
    @Override
    public void addBotFactory(String resource, BotFactory platformBot) {
        this.botCache.put(resource, platformBot);
    }

    /**
     * Register a {@code Listener} to make sure it will get invoked
     * when the right event gets called
     *
     * @param listener the object that will handle incoming events
     */
    @SuppressWarnings("unchecked")
    @Override
    public void registerListener(Listener listener) {

        if(!this.listeners.containsKey(listener.getTarget())) {
            this.listeners.put(listener.getTarget(), new HashSet<>());
        }
        this.listeners.get(listener.getTarget()).add(listener);
    }

    /**
     * Invoke all the registered Listeners
     *
     * @param event the event that should be passed into
     *              all the listeners that are listening for
     *              this type of events
     * @return the event that was passed into all of the listeners
     */
    @Override
    public CancelableEvent execute(CancelableEvent event) {
        execute((Event) event);
        return event;
    }

    /**
     * Invoke all the registered Listeners
     *
     * @param event the event that should be passed into
     *              all the listeners that are listening for
     *              this type of events
     */
    @SuppressWarnings("unchecked")
    @Override
    public void execute(Event event) {
        this.listeners.entrySet().stream().filter(entry -> event.getClass().isAssignableFrom(entry.getKey())).map(Map.Entry::getValue).flatMap(Collection::stream).forEach(listener ->
            listener.onEvent(event));
    }
}
