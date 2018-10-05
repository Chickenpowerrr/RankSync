package com.gmail.chickenpowerrr.ranksync.core;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.discord.BotFactory;

public class Main {

    private Bot discord;

    private Main() {
        RankSyncApiImpl.start();
        RankSyncApi.getApi().addBotFactory("Discord", BotFactory.getInstance());

        /*
        RankSyncApi.getApi().getBotFactory("Discord").getBot(new BasicProperties()
                .addProperty("name", "SuperKoeleBot")
                .addProperty("token", "NDkzODA0MDgzMTExNjU3NDcz.DoqSmw.6N-54Ufy6ZA583RznOOEZ0OmLUs")
                .addProperty("guild_id", 493791190844571658L)
                .addProperty("type", "SQL")
                .addProperty("max_pool_size", 10)
                .addProperty("host", "127.0.0.1")
                .addProperty("port", 3306)
                .addProperty("database", "ranksync")
                .addProperty("username", "root")
                .addProperty("password", "walrus")
                .addProperty("rank_resource", null));
                */
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
