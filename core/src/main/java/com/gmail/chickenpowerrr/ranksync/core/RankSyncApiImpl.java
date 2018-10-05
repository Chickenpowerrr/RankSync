package com.gmail.chickenpowerrr.ranksync.core;

import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.ApiManager;
import com.gmail.chickenpowerrr.ranksync.api.BotFactory;
import com.gmail.chickenpowerrr.ranksync.api.event.CancelableEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.Event;
import com.gmail.chickenpowerrr.ranksync.api.event.Listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

//DON'T USE THIS CLASS DIRECTLY
final class RankSyncApiImpl implements RankSyncApi {

    private final Map<String, BotFactory> botCache = new HashMap<>();
    private final Map<Class<? extends Event>, Collection<Listener>> listeners = new HashMap<>();

    private RankSyncApiImpl(){}

    static void start() {
        ApiManager.setInstance(new RankSyncApiImpl());
    }

    public BotFactory getBotFactory(String resource) {
        return this.botCache.get(resource);
    }

    public void addBotFactory(String resource, BotFactory platformBot) {
        this.botCache.put(resource, platformBot);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerListener(Listener listener) {

        if(!this.listeners.containsKey(listener.getTarget())) {
            this.listeners.put(listener.getTarget(), new HashSet<>());
        }
        this.listeners.get(listener.getTarget()).add(listener);
    }

    @Override
    public CancelableEvent execute(CancelableEvent event) {
        execute((Event) event);
        return event;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Event event) {
        this.listeners.entrySet().stream().filter(entry -> event.getClass().isAssignableFrom(entry.getKey())).map(Map.Entry::getValue).flatMap(Collection::stream).forEach(listener ->
            listener.onEvent(event));
    }
}
