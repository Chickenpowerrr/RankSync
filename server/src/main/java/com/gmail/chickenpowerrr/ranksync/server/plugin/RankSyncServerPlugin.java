package com.gmail.chickenpowerrr.ranksync.server.plugin;

import com.gmail.chickenpowerrr.languagehelper.LanguageHelper;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.data.BasicProperties;
import com.gmail.chickenpowerrr.ranksync.api.link.Link;
import com.gmail.chickenpowerrr.ranksync.api.name.NameResource;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankHelper;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.api.reward.RewardSettings;
import com.gmail.chickenpowerrr.ranksync.manager.RankSyncManager;
import com.gmail.chickenpowerrr.ranksync.server.language.Translation;
import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import com.gmail.chickenpowerrr.ranksync.server.listener.BotEnabledEventListener;
import com.gmail.chickenpowerrr.ranksync.server.listener.BotForceShutdownEventListener;
import com.gmail.chickenpowerrr.ranksync.server.listener.PlayerLinkCodeCreateEventListener;
import com.gmail.chickenpowerrr.ranksync.server.listener.PlayerLinkedEventListener;
import com.gmail.chickenpowerrr.ranksync.server.listener.PlayerUnlinkedEventListener;
import com.gmail.chickenpowerrr.ranksync.server.listener.PlayerUpdateOnlineStatusEventListener;
import com.gmail.chickenpowerrr.ranksync.server.reward.RewardSettings.RewardAction;
import com.gmail.chickenpowerrr.ranksync.server.update.UpdateChecker;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This class makes sure a server gets the default functions to use RankSync
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public interface RankSyncServerPlugin {

  /**
   * Returns a Bot by its name
   *
   * @param name the name of the Bot
   * @return the Bot that goes by the given name
   */
  Bot<?, ?> getBot(String name);

  /**
   * Runs a timer given its period and delay
   *
   * @param runnable what should be done
   * @param delay when it will start
   * @param period the delay between every action
   */
  void runTaskTimer(Runnable runnable, long delay, long period);

  /**
   * Sets up the config
   */
  void setupConfig();

  /**
   * Shuts down RankSync
   *
   * @param reason the reason why RankSync should stop
   */
  void shutdown(String... reason);

  /**
   * Returns the data folder of the plugin
   */
  File getDataFolder();

  /**
   * Returns a string from the config
   *
   * @param key the location of the string
   * @return the requested string
   */
  String getConfigString(String key);

  /**
   * Returns a string list from the config
   *
   * @param key the location of the string
   * @return the requested string list
   */
  List<String> getConfigStringList(String key);

  /**
   * Returns a boolean from the config
   *
   * @param key the location of the boolean
   * @return the requested boolean
   */
  boolean getConfigBoolean(String key);

  /**
   * Returns a long from the config
   *
   * @param key the location of the long
   * @return the requested long
   */
  long getConfigLong(String key);

  /**
   * Returns a int from the config
   *
   * @param key the location of the int
   * @return the requested int
   */
  int getConfigInt(String key);

  /**
   * Updates the link helper
   *
   * @param linkHelper the new link helper
   */
  void setLinkHelper(LinkHelper linkHelper);

  /**
   * Load all relevant event listeners
   */
  void registerListeners();

  /**
   * Load all relevant command executors
   */
  void registerCommands();

  /**
   * Returns the link helper
   */
  LinkHelper getLinkHelper();

  /**
   * Returns the rank helper
   */
  RankHelper getRankHelper();

  /**
   * Updates the used instance of the rank helper
   *
   * @param rankHelper the new instance
   */
  void setRankHelper(RankHelper rankHelper);

  /**
   * Creates a new name resource
   */
  NameResource createNameResource();

  /**
   * Validates if it's possible to start with the current dependencies
   */
  RankResource validateDependencies();

  /**
   * Logs an info message
   *
   * @param message the information
   */
  void logInfo(String message);

  /**
   * Logs a warning message
   *
   * @param message the warning
   */
  void logWarning(String message);

  /**
   * Returns the current bots
   */
  Map<String, Bot<?, ?>> getBots();

  /**
   * Returns all of the links given in the config.yml
   */
  List<Link> getSyncedRanks();

  /**
   * Returns if the bot is still running
   */
  boolean isRunning();

  /**
   * Executes a given command on the network as the console
   *
   * @param command the command which needs to be executed
   */
  void executeCommand(String command);

  /**
   * Returns the reward settings as given in the config.yml
   */
  default RewardSettings getRewardSettings() {
    return new com.gmail.chickenpowerrr.ranksync.server.reward.RewardSettings(
        new RewardAction(getConfigInt("reward.max-sync"),
            getConfigBoolean("reward.enabled-sync"),
            getConfigStringList("sync-commands"), this::executeCommand),
        new RewardAction(getConfigInt("reward.max-unsync"),
            getConfigBoolean("reward.enabled-unsync"),
            getConfigStringList("unsync-commands"), this::executeCommand));
  }

  /**
   * Enables the plugin
   */
  default void enable() {
    new UpdateChecker().check();
    setupConfig();

    long time = System.currentTimeMillis();
    LanguageHelper languageHelper = new LanguageHelper(getDataFolder());
    Translation.setLanguageHelper(languageHelper);
    String language = getConfigString("language");
    if (language == null) {
      language = "english";
      logWarning("The config.yml doesn't contain a language field, so it's set to English");
    }
    Translation.setLanguage(language);
    logInfo(Translation.STARTUP_TRANSLATIONS
        .getTranslation("time", Long.toString(System.currentTimeMillis() - time)));
    time = System.currentTimeMillis();

    NameResource nameResource = createNameResource();
    RankResource rankResource = validateDependencies();

    if (rankResource != null) {
      RankSyncApi.getApi().registerListener(new BotForceShutdownEventListener(this));

      setLinkHelper(new LinkHelper(this));
      RankSyncManager.getInstance().setup();
      RankSyncApi.getApi().getBotFactory("Discord");

      registerCommands();

      getBots()
          .put("discord", RankSyncApi.getApi().getBotFactory("Discord").getBot(new BasicProperties()
              .addProperty("token", getConfigString("discord.token"))
              .addProperty("guild_id", getConfigLong("discord.guild-id"))
              .addProperty("update_non_synced", getConfigBoolean("discord.update-non-synced"))
              .addProperty("sync_names", getConfigBoolean("discord.sync-names"))
              .addProperty("name_format", getConfigString("discord.name-format"))
              .addProperty("permission_warnings", getConfigBoolean("discord.permission-warnings"))
              .addProperty("delete_timer", getConfigInt("discord.delete-timer"))
              .addProperty("type", getConfigString("database.type"))
              .addProperty("max_pool_size", getConfigInt("database.sql.max-pool-size"))
              .addProperty("host", getConfigString("database.sql.host"))
              .addProperty("port", getConfigInt("database.sql.port"))
              .addProperty("database", getConfigString("database.sql.database"))
              .addProperty("username", getConfigString("database.sql.user"))
              .addProperty("password", getConfigString("database.sql.password"))
              .addProperty("base_path", getDataFolder() + "/data/")
              .addProperty("name_resource", nameResource)
              .addProperty("rank_resource", rankResource)
              .addProperty("language", language)
              .addProperty("language_helper", languageHelper)));

      Bot discordBot = getBot("discord");
      rankResource.setBot(discordBot);

      setRankHelper(new com.gmail.chickenpowerrr.ranksync.server.rank.RankHelper(getSyncedRanks()));

      if (isRunning()) {
        RewardSettings rewardSettings = getRewardSettings();
        RankSyncApi.getApi().registerListener(new PlayerUpdateOnlineStatusEventListener());
        RankSyncApi.getApi()
            .registerListener(new PlayerLinkCodeCreateEventListener(getLinkHelper()));
        RankSyncApi.getApi().registerListener(new BotEnabledEventListener(getRankHelper()));
        RankSyncApi.getApi()
            .registerListener(new PlayerLinkedEventListener(rewardSettings.getSyncAction()));
        RankSyncApi.getApi()
            .registerListener(new PlayerUnlinkedEventListener(rewardSettings.getUnsyncAction()));

        registerListeners();
        logInfo(Translation.STARTUP_RANKS
            .getTranslation("time", Long.toString(System.currentTimeMillis() - time)));
      }
    }
  }
}
