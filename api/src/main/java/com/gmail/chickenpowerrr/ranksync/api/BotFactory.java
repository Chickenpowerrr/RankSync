package com.gmail.chickenpowerrr.ranksync.api;

public interface BotFactory {

    Bot getBot();

    Bot getBot(Properties properties);
}
