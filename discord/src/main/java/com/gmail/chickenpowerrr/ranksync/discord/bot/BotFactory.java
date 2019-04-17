package com.gmail.chickenpowerrr.ranksync.discord.bot;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.data.Properties;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class BotFactory implements com.gmail.chickenpowerrr.ranksync.api.bot.BotFactory {

    @Getter private static final BotFactory instance = new BotFactory();

    private final Map<Properties, Bot> botCache = new HashMap<>();

    private BotFactory(){}

    @Override
    public Bot getBot() {
        return null;
    }

    @Override
    public Bot getBot(Properties properties) {
        if(this.botCache.containsKey(properties)) {
            return this.botCache.get(properties);
        } else {
            Bot bot = new DiscordBot(properties);
            this.botCache.put(properties, bot);
            return bot;
        }
    }
}
