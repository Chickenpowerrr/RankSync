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

final class RankSyncApiImpl implements RankSyncApi {

    @Getter(value = AccessLevel.MODULE) private static final RankSyncApi instance = new RankSyncApiImpl();

    private final Map<String, BotFactory> botCache = new HashMap<>();
    private final Map<Class<? extends Event>, Collection<Listener>> listeners = new HashMap<>();

    private RankSyncApiImpl(){}

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
