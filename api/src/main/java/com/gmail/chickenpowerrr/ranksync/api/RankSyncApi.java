package com.gmail.chickenpowerrr.ranksync.api;

import com.gmail.chickenpowerrr.ranksync.api.event.CancelableEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.Event;
import com.gmail.chickenpowerrr.ranksync.api.event.Listener;

public interface RankSyncApi {

    static RankSyncApi getApi() {
        return ApiManager.getRankSyncApi();
    }

    BotFactory getBotFactory(String resource);

    void addBotFactory(String resource, BotFactory platformBot);

    void registerListener(Listener listener);

    CancelableEvent execute(CancelableEvent event);

    void execute(Event event);
}
