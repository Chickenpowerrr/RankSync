package com.gmail.chickenpowerrr.ranksync.spigot;

import com.gmail.chickenpowerrr.languagehelper.LanguageHelper;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.data.BasicProperties;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.manager.RankSyncManager;
import com.gmail.chickenpowerrr.ranksync.spigot.command.RankSyncCommandExecutor;
import com.gmail.chickenpowerrr.ranksync.spigot.command.RankSyncTabCompleter;
import com.gmail.chickenpowerrr.ranksync.spigot.command.UnSyncCommandExecutor;
import com.gmail.chickenpowerrr.ranksync.spigot.language.Translation;
import com.gmail.chickenpowerrr.ranksync.spigot.link.LinkHelper;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc.BotEnabledEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc.BotForceShutdownEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc.PlayerLinkCodeCreateEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc.PlayerLinkedEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc.PlayerUpdateOnlineStatusEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.spigot.AsyncPlayerPreLoginEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.spigot.PlayerQuitEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.name.NameResource;
import com.gmail.chickenpowerrr.ranksync.spigot.roleresource.LuckPermsRankResource;
import com.gmail.chickenpowerrr.ranksync.spigot.roleresource.RankHelper;
import com.gmail.chickenpowerrr.ranksync.spigot.roleresource.VaultRankResource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import me.lucko.luckperms.LuckPerms;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class starts all of the parts needed to sync Ranks with the given platforms
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public final class RankSyncPlugin extends JavaPlugin {

  @Getter
  private LinkHelper linkHelper;
  private Map<String, Bot<?, ?>> bots = new HashMap<>();
  @Getter
  private RankHelper rankHelper;

  /**
   * Reads the plugin.yml and starts the Objects required to synchronize the ranks
   */
  @Override
  public void onEnable() {
    saveDefaultConfig();

    long time = System.currentTimeMillis();
    LanguageHelper languageHelper = new LanguageHelper(getDataFolder());
    Translation.setLanguageHelper(languageHelper);
    String language = getConfig().getString("language");
    if (language == null) {
      language = "english";
      getLogger()
          .warning("The config.yml doesn't contain a language field, so it's set to English");
    }
    Translation.setLanguage(language);
    getLogger().info(Translation.STARTUP_TRANSLATIONS
        .getTranslation("time", Long.toString(System.currentTimeMillis() - time)));
    time = System.currentTimeMillis();

    NameResource nameResource = new NameResource();
    RankResource rankResource;

    if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
      rankResource = new LuckPermsRankResource(LuckPerms.getApi());
    } else if (Bukkit.getPluginManager().getPlugin("Vault") != null
        && getServer().getServicesManager().getRegistration(Permission.class) != null) {
      rankResource = new VaultRankResource(
          getServer().getServicesManager().getRegistration(Permission.class).getProvider());
    } else {
      Bukkit.getLogger().severe("You should use either LuckPerms of Vault to work with RankSync");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    this.linkHelper = new LinkHelper();
    RankSyncManager.getInstance().setup();
    RankSyncApi.getApi().getBotFactory("Discord");

    PluginCommand rankSyncCommand = getCommand("ranksync");
    rankSyncCommand.setExecutor(new RankSyncCommandExecutor());
    rankSyncCommand.setTabCompleter(new RankSyncTabCompleter());

    PluginCommand unSyncCommand = getCommand("unsync");
    unSyncCommand.setExecutor(new UnSyncCommandExecutor());
    unSyncCommand.setTabCompleter(new RankSyncTabCompleter());

    this.bots
        .put("discord", RankSyncApi.getApi().getBotFactory("Discord").getBot(new BasicProperties()
            .addProperty("token", getConfig().getString("discord.token"))
            .addProperty("guild_id", getConfig().getLong("discord.guild-id"))
            .addProperty("update_non_synced", getConfig().getBoolean("discord.update-non-synced"))
            .addProperty("sync_names", getConfig().getBoolean("discord.sync-names"))
            .addProperty("type", getConfig().getString("database.type"))
            .addProperty("max_pool_size", getConfig().getInt("database.sql.max-pool-size"))
            .addProperty("host", getConfig().getString("database.sql.host"))
            .addProperty("port", getConfig().getInt("database.sql.port"))
            .addProperty("database", getConfig().getString("database.sql.database"))
            .addProperty("username", getConfig().getString("database.sql.user"))
            .addProperty("password", getConfig().getString("database.sql.password"))
            .addProperty("base_path", getDataFolder() + "/data/")
            .addProperty("name_resource", nameResource)
            .addProperty("rank_resource", rankResource)
            .addProperty("language", language)
            .addProperty("language_helper", languageHelper)));

    Bot discordBot = getBot("discord");
    rankResource.setBot(discordBot);

    Map<String, Map<Bot<?, ?>, Collection<String>>> syncedRanks = new HashMap<>();

    this.bots.forEach((botName, bot) -> {
      Map<String, Object> ranks = getConfig().getConfigurationSection("ranks." + botName)
          .getValues(false);
      ranks.values().forEach(object -> {
        ConfigurationSection rankInfo = (ConfigurationSection) object;
        String minecraftRank = rankInfo.getString("minecraft");
        Collection<String> platformRanks = rankInfo.getStringList(botName);

        if (platformRanks.isEmpty()) {
          platformRanks.add(rankInfo.getString(botName));
        }

        if (!syncedRanks.containsKey(minecraftRank)) {
          syncedRanks.put(minecraftRank, new HashMap<>());
        }
        syncedRanks.get(minecraftRank).put(bot, platformRanks);
      });
    });

    this.rankHelper = new RankHelper(syncedRanks);

    RankSyncApi.getApi().registerListener(new PlayerUpdateOnlineStatusEventListener());
    RankSyncApi.getApi().registerListener(new PlayerLinkCodeCreateEventListener());
    RankSyncApi.getApi().registerListener(new BotEnabledEventListener(this.rankHelper));
    RankSyncApi.getApi().registerListener(new BotForceShutdownEventListener());
    RankSyncApi.getApi().registerListener(new PlayerLinkedEventListener());

    Bukkit.getPluginManager().registerEvents(new AsyncPlayerPreLoginEventListener(), this);
    Bukkit.getPluginManager().registerEvents(new PlayerQuitEventListener(), this);
    getLogger().info(Translation.STARTUP_RANKS
        .getTranslation("time", Long.toString(System.currentTimeMillis() - time)));
  }

  /**
   * Returns a Bot by its name
   *
   * @param name the name of the Bot
   * @return the Bot that goes by the given name
   */
  public Bot<?, ?> getBot(String name) {
    return this.bots.get(name.toLowerCase());
  }

  /**
   * Updates the defaults of the config to make sure everything can still be used when it's not in
   * the config.yml
   */
  private void setConfigDefaults() {
    getConfig().addDefault("update_non_synced", true);
    getConfig().addDefault("sync_names", false);
    getConfig().addDefault("database.type", "yaml");
  }
}
