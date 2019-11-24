package com.gmail.chickenpowerrr.ranksync.bungeecord;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.link.Link;
import com.gmail.chickenpowerrr.ranksync.api.name.NameResource;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankHelper;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.bungeecord.command.RankSyncCommand;
import com.gmail.chickenpowerrr.ranksync.bungeecord.command.RankSyncTabCompleter;
import com.gmail.chickenpowerrr.ranksync.bungeecord.command.UnSyncCommand;
import com.gmail.chickenpowerrr.ranksync.bungeecord.listener.PlayerDisconnectEventListener;
import com.gmail.chickenpowerrr.ranksync.bungeecord.listener.ServerConnectEventListener;
import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import com.gmail.chickenpowerrr.ranksync.server.roleresource.LuckPermsRankResource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import me.lucko.luckperms.LuckPerms;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;

/**
 * This class starts all of the parts needed to sync Ranks with the given platforms
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public final class RankSyncPlugin extends Plugin implements RankSyncServerPlugin {

  @Getter
  @Setter
  private LinkHelper linkHelper;

  @Getter
  private Map<String, Bot<?, ?>> bots = new HashMap<>();

  @Getter
  @Setter
  private RankHelper rankHelper;

  private Configuration configuration;

  /**
   * Enables the important features in order to synchronize ranks
   */
  @Override
  public void onEnable() {
    enable();
    Metrics metrics = new Metrics(this);
    metrics.addCustomChart(
        new Metrics.SimplePie("used_storage", () -> getConfigString("database.type")));
    metrics.addCustomChart(
        new Metrics.SimplePie("used_language", () -> getConfigString("language")));
  }

  /**
   * Returns a Bot by its name
   *
   * @param name the name of the Bot
   * @return the Bot that goes by the given name
   */
  @Override
  public Bot<?, ?> getBot(String name) {
    return this.bots.get(name.toLowerCase());
  }

  /**
   * Runs a timer given its period and delay
   *
   * @param runnable what should be done
   * @param delay when it will start
   * @param period the delay between every action
   */
  @Override
  public void runTaskTimer(Runnable runnable, long delay, long period) {
    getProxy().getScheduler()
        .schedule(this, runnable, delay * 50, period * 50, TimeUnit.MILLISECONDS);
  }

  /**
   * Calls the onDisable and unregisters all open features
   *
   * @param reason the reason why RankSync should stop
   */
  @Override
  public void shutdown(String... reason) {
    getLogger().severe("Disabling the RankSync plugin: ");
    for (String reasonString : reason) {
      getLogger().warning(reasonString);
    }

    onDisable();

    getProxy().getPluginManager().unregisterCommands(this);
    getProxy().getPluginManager().unregisterCommands(this);
    getProxy().getScheduler().cancel(this);
  }

  /**
   * Returns a string from the config
   *
   * @param key the location of the string
   * @return the requested string
   */
  @Override
  public String getConfigString(String key) {
    if (this.configuration.contains(key)) {
      return this.configuration.getString(key);
    } else if (key.equals("database.type")) {
      return "yaml";
    } else {
      return "";
    }
  }

  /**
   * Returns a long from the config
   *
   * @param key the location of the long
   * @return the requested long
   */
  @Override
  public long getConfigLong(String key) {
    return this.configuration.getLong(key);
  }

  /**
   * Returns a string list from the config
   *
   * @param key the location of the string list
   * @return the requested string list
   */
  @Override
  public List<String> getConfigStringList(String key) {
    return this.configuration.getStringList(key);
  }

  /**
   * Returns a int from the config
   *
   * @param key the location of the int
   * @return the requested int
   */
  @Override
  public int getConfigInt(String key) {
    return this.configuration.getInt(key);
  }

  /**
   * Returns a boolean from the config
   *
   * @param key the location of the boolean
   * @return the requested boolean
   */
  @Override
  public boolean getConfigBoolean(String key) {
    if (this.configuration.contains(key)) {
      return this.configuration.getBoolean(key);
    } else {
      switch (key) {
        case "update_non_synced":
        case "discord.permission-warnings":
          return true;
        case "sync_names":
        default:
          return false;
      }
    }
  }

  /**
   * Registers the commands
   */
  @Override
  public void registerCommands() {
    getProxy().getPluginManager().registerCommand(this, new RankSyncCommand(this));
    getProxy().getPluginManager().registerCommand(this, new UnSyncCommand(this));
  }

  /**
   * Registers the listeners
   */
  @Override
  public void registerListeners() {
    getProxy().getPluginManager().registerListener(this, new RankSyncTabCompleter(this.linkHelper));
    getProxy().getPluginManager()
        .registerListener(this, new PlayerDisconnectEventListener(this.linkHelper));
    getProxy().getPluginManager()
        .registerListener(this, new ServerConnectEventListener(this.linkHelper));
  }

  /**
   * Logs an info message
   *
   * @param message the info
   */
  @Override
  public void logInfo(String message) {
    getLogger().info(message);
  }

  /**
   * Logs a warning message
   *
   * @param message the warning
   */
  @Override
  public void logWarning(String message) {
    getLogger().warning(message);
  }

  /**
   * Validates if it's possible to start with the current dependencies
   */
  @Override
  public RankResource validateDependencies() {
    if (getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
      return new LuckPermsRankResource(this, LuckPerms.getApi());
    } else {
      shutdown("You should use LuckPerms to work with RankSync");
      return null;
    }

    //TODO add Vault
  }

  /**
   * Creates a new name resource
   */
  @Override
  public NameResource createNameResource() {
    return new com.gmail.chickenpowerrr.ranksync.bungeecord.name.NameResource(this);
  }

  /**
   * Executes a given command on the server as the console
   *
   * @param command the command which needs to be executed
   */
  @Override
  public void executeCommand(String command) {
    ProxyServer.getInstance().getPluginManager()
        .dispatchCommand(ProxyServer.getInstance().getConsole(), command);
  }

  /**
   * Returns all of the links given in the config.yml
   */
  @Override
  public List<Link> getSyncedRanks() {
    List<Link> syncedRanks = new ArrayList<>();

    getBots()
        .forEach((botName, bot) -> this.configuration.getSection("ranks.discord").getKeys().stream()
            .map(section -> this.configuration.getSection("ranks.discord." + section))
            .forEach(section -> {
              String minecraftRank = section.getString("minecraft");
              List<String> platformRanks = section.getStringList(botName);
              if (platformRanks.isEmpty()) {
                platformRanks.add(section.getString(botName));
              }

              syncedRanks.add(new com.gmail.chickenpowerrr.ranksync.server.link.Link(
                  Collections.singletonList(minecraftRank), platformRanks,
                  Optional.ofNullable(section.getString("name-format"))
                      .orElse(this.configuration.getString("discord.name-format")), bot));
            }));

    return syncedRanks;
  }

  /**
   * Sets up the config
   */
  @SuppressWarnings("unchecked")
  @Override
  public void setupConfig() {
    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();
    }

    File file = new File(getDataFolder(), "config.yml");

    if (!file.exists()) {
      try (InputStream in = getResourceAsStream("config.yml")) {
        Files.copy(in, file.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      this.configuration = ConfigurationProvider
          .getProvider(YamlConfiguration.class)
          .load(new File(getDataFolder(), "config.yml"),
              ConfigurationProvider.getProvider(YamlConfiguration.class)
                  .load(getResourceAsStream("config.yml")));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      Field defaultField = this.configuration.getClass().getDeclaredField("defaults");
      defaultField.setAccessible(true);
      Configuration defaults = (Configuration) defaultField.get(this.configuration);
      Field selfField = this.configuration.getClass().getDeclaredField("self");
      selfField.setAccessible(true);
      System.out.println(defaults);
      Map<String, Object> self = (Map<String, Object>) selfField.get(defaults);
      self.remove("ranks");
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    try {
      ConfigurationProvider.getProvider(YamlConfiguration.class)
          .save(this.configuration, new File(getDataFolder(), "config.yml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns if the bot is still running
   */
  @Override
  public boolean isRunning() {
    return ProxyServer.getInstance().getPluginManager().getPlugin("RankSync") != null;
  }
}
