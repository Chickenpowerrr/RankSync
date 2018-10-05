package com.gmail.chickenpowerrr.ranksync.spigot;

import com.gmail.chickenpowerrr.ranksync.api.BasicProperties;
import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.RankResource;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.core.Main;
import com.gmail.chickenpowerrr.ranksync.spigot.command.RankSyncCommandExecutor;
import com.gmail.chickenpowerrr.ranksync.spigot.command.RankSyncTabCompleter;
import com.gmail.chickenpowerrr.ranksync.spigot.link.LinkHelper;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc.PlayerLinkCodeCreateEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc.PlayerUpdateOnlineStatusEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.spigot.PlayerJoinEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.roleresource.LuckPermsRankResource;
import com.gmail.chickenpowerrr.ranksync.spigot.roleresource.RankHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class RankSyncPlugin extends JavaPlugin {

    @Getter private LinkHelper linkHelper;
    private Map<String, Bot> bots = new HashMap<>();
    @Getter private RankHelper rankHelper;

    @SuppressWarnings("unchecked")
    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.linkHelper = new LinkHelper();
        Main.main(new String[]{});
        RankSyncApi.getApi().getBotFactory("Discord");
        PluginCommand command = getCommand("ranksync");
        command.setExecutor(new RankSyncCommandExecutor());
        command.setTabCompleter(new RankSyncTabCompleter());

        RankResource rankResource = new LuckPermsRankResource();

        this.bots.put("discord", RankSyncApi.getApi().getBotFactory("Discord").getBot(new BasicProperties()
                .addProperty("name", getConfig().getString("discord.name"))
                .addProperty("token", getConfig().getString("discord.token"))
                .addProperty("guild_id", getConfig().getLong("discord.guild-id"))
                .addProperty("type", getConfig().getString("database.type"))
                .addProperty("max_pool_size", getConfig().getInt("database.sql.max-pool-size"))
                .addProperty("host", getConfig().getString("database.sql.host"))
                .addProperty("port", getConfig().getInt("database.sql.port"))
                .addProperty("database", getConfig().getString("database.sql.database"))
                .addProperty("username", getConfig().getString("database.sql.user"))
                .addProperty("password", getConfig().getString("database.sql.password"))
                .addProperty("rank_resource", rankResource)));
        rankResource.setBot(this.bots.get("discord"));

        Map<String, Map<Bot, String>> syncedRanks = new HashMap<>();

        try {
            ConfigurationSection configurationSection = getConfig().getConfigurationSection("ranks");
            Field field = configurationSection.getClass().getDeclaredField("map");
            field.setAccessible(true);

            this.bots.forEach((botName, bot) -> {
                ConfigurationSection aliases = configurationSection.getConfigurationSection(botName);
                try {
                    Map<String, Object> ranks = (Map<String, Object>) field.get(aliases);
                    ranks.values().forEach(object -> {
                        ConfigurationSection rankInfo = (ConfigurationSection) object;
                        String minecraftRank = rankInfo.getString("minecraft");
                        String platformRank = rankInfo.getString(botName);
                        if(!syncedRanks.containsKey(minecraftRank)) {
                            syncedRanks.put(minecraftRank, new HashMap<>());
                        }
                        syncedRanks.get(minecraftRank).put(bot, platformRank);
                    });
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.rankHelper = new RankHelper(syncedRanks);

        RankSyncApi.getApi().registerListener(new PlayerUpdateOnlineStatusEventListener());
        RankSyncApi.getApi().registerListener(new PlayerLinkCodeCreateEventListener());

        Bukkit.getPluginManager().registerEvents(new PlayerJoinEventListener(), this);
    }

    public Bot getBot(String name) {
        return this.bots.get(name.toLowerCase());
    }
}
