package com.gmail.chickenpowerrr.ranksync.core;

import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.discord.BotFactory;

public class Main {

    private Main() {
        RankSyncApiImpl.start();
        RankSyncApi.getApi().addBotFactory("Discord", BotFactory.getInstance());
    }

    public static void main(String[] args) {
        new Main();

        new Thread(() -> {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
