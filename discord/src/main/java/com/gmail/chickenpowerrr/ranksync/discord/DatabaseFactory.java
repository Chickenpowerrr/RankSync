package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.Database;
import com.gmail.chickenpowerrr.ranksync.api.Properties;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;

class DatabaseFactory implements com.gmail.chickenpowerrr.ranksync.api.DatabaseFactory {

    @Getter private static final Map<Guild, DatabaseFactory> instances = new HashMap<>();

    private final Map<String, Map<Properties, Database>> databaseConstructorCache = new HashMap<>();
    private final Guild guild;
    @Getter private final Bot bot;

    private DatabaseFactory(Bot bot, Guild guild) {
        this.bot = bot;
        this.guild = guild;
    }

    static DatabaseFactory getInstance(Bot bot, Guild guild) {
        if(!instances.containsKey(guild)) {
            instances.put(guild, new DatabaseFactory(bot, guild));
        }
        return instances.get(guild);
    }

    static DatabaseFactory getInstance(Guild guild) {
        return getInstance(null, guild);
    }

    @Override
    public Database getDatabase(String name, Properties credentials) {
        if(this.databaseConstructorCache.containsKey(name)) {
            if(this.databaseConstructorCache.get(name).containsKey(credentials)) {
                return this.databaseConstructorCache.get(name).get(credentials);
            }
        } else {
            this.databaseConstructorCache.put(name, new HashMap<>());
        }

        Database database;

        if(credentials.has("type")) {
            switch(credentials.getString("type").toLowerCase()) {
                case "sql":
                    database = new SqlDatabase(this.bot, credentials);
                    break;
                default:
                    throw new IllegalStateException("You should add a valid database to the properties");
            }
        } else {
            throw new IllegalStateException("You should add a valid database to the properties");
        }

        this.databaseConstructorCache.get(name).put(credentials, database);
        return database;
    }
}
