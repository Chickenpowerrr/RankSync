package com.gmail.chickenpowerrr.ranksync.spigot;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.name.NameResource;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankHelper;
import com.gmail.chickenpowerrr.ranksync.api.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import com.gmail.chickenpowerrr.ranksync.server.roleresource.LuckPermsRankResource;
import com.gmail.chickenpowerrr.ranksync.spigot.command.RankSyncCommand;
import com.gmail.chickenpowerrr.ranksync.spigot.command.RankSyncTabCompleter;
import com.gmail.chickenpowerrr.ranksync.spigot.command.UnSyncCommand;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.AsyncPlayerPreLoginEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.listener.PlayerQuitEventListener;
import com.gmail.chickenpowerrr.ranksync.spigot.roleresource.VaultRankResource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import me.lucko.luckperms.LuckPerms;
import net.milkbowl.vault.permission.Permission;
import org.bstats.bukkit.Metrics;
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
public final class RankSyncPlugin extends JavaPlugin implements RankSyncServerPlugin {

  @Getter
  @Setter
  private LinkHelper linkHelper;

  @Getter
  private Map<String, Bot<?, ?>> bots = new HashMap<>();

  @Getter
  @Setter
  private RankHelper rankHelper;

  /**
   * Reads the plugin.yml and starts the Objects required to synchronize the ranks
   */
  @Override
  public void onEnable() {
    enable();
    Metrics metrics = new Metrics(this);
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
    Bukkit.getScheduler().runTaskTimer(this, runnable, delay, period);
  }

  /**
   * Shuts down RankSync
   *
   * @param reason the reason why RankSync should stop
   */
  @Override
  public void shutdown(String... reason) {
    getLogger().severe("Disabling the RankSync plugin: ");
    for (String reasonString : reason) {
      getLogger().warning(reasonString);
    }
    Bukkit.getPluginManager().disablePlugin(JavaPlugin.getPlugin(RankSyncPlugin.class));
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

  /**
   * Registers the listeners
   */
  @Override
  public void registerListeners() {
    Bukkit.getPluginManager()
        .registerEvents(new AsyncPlayerPreLoginEventListener(this.linkHelper), this);
    Bukkit.getPluginManager().registerEvents(new PlayerQuitEventListener(this.linkHelper), this);
  }

  /**
   * Validates if it's possible to start with the current dependencies
   */
  @Override
  public RankResource validateDependencies() {
    if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
      return new LuckPermsRankResource(this, LuckPerms.getApi());
    } else if (Bukkit.getPluginManager().getPlugin("Vault") != null
        && getServer().getServicesManager().getRegistration(Permission.class) != null) {
      return new VaultRankResource(
          getServer().getServicesManager().getRegistration(Permission.class).getProvider());
    } else {
      shutdown("You should use either LuckPerms or Vault to work with RankSync");
      return null;
    }
  }

  /**
   * Returns a boolean from the config
   *
   * @param key the location of the boolean
   * @return the requested boolean
   */
  @Override
  public boolean getConfigBoolean(String key) {
    return getConfig().getBoolean(key);
  }

  /**
   * Returns a int from the config
   *
   * @param key the location of the int
   * @return the requested int
   */
  @Override
  public int getConfigInt(String key) {
    return getConfig().getInt(key);
  }

  /**
   * Returns a string list from the config
   *
   * @param key the location of the string
   * @return the requested string list
   */
  @Override
  public List<String> getConfigStringList(String key) {
    return getConfig().getStringList(key);
  }

  /**
   * Returns a long from the config
   *
   * @param key the location of the long
   * @return the requested long
   */
  @Override
  public long getConfigLong(String key) {
    return getConfig().getLong(key);
  }

  /**
   * Returns a string from the config
   *
   * @param key the location of the string
   * @return the requested string
   */
  @Override
  public String getConfigString(String key) {
    return getConfig().getString(key);
  }

  /**
   * Logs an info message
   *
   * @param message the information
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
   * Registers the commands
   */
  @Override
  public void registerCommands() {
    PluginCommand rankSyncCommand = getCommand("ranksync");
    rankSyncCommand.setExecutor(new RankSyncCommand(this));
    rankSyncCommand.setTabCompleter(new RankSyncTabCompleter(this.linkHelper));

    PluginCommand unSyncCommand = getCommand("unsync");
    unSyncCommand.setExecutor(new UnSyncCommand(this));
    unSyncCommand.setTabCompleter(new RankSyncTabCompleter(this.linkHelper));
  }

  /**
   * Creates a new name resource
   */
  @Override
  public NameResource createNameResource() {
    return new com.gmail.chickenpowerrr.ranksync.spigot.name.NameResource();
  }

  /**
   * Returns all of the links given in the config.yml
   */
  @Override
  public Map<String, Map<Bot<?, ?>, Collection<String>>> getSyncedRanks() {
    Map<String, Map<Bot<?, ?>, Collection<String>>> syncedRanks = new HashMap<>();

    getBots().forEach((botName, bot) -> {
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

    return syncedRanks;
  }

  /**
   * Sets up the config
   */
  @Override
  public void setupConfig() {
    saveDefaultConfig();
    setConfigDefaults();
  }
}
