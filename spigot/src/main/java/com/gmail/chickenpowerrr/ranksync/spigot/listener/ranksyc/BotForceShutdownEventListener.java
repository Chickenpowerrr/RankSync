package com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc;

import com.gmail.chickenpowerrr.ranksync.api.event.BotForceShutdownEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class shuts the plugin down whenever the Bot shuts down
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class BotForceShutdownEventListener implements Listener<BotForceShutdownEvent> {

  @Override
  public Class<BotForceShutdownEvent> getTarget() {
    return BotForceShutdownEvent.class;
  }

  /**
   * Disables the plugin when the Bot shuts down
   *
   * @param event the event that triggered the method
   */
  @Override
  public void onEvent(BotForceShutdownEvent event) {
    Bukkit.getLogger().severe("Disabling the RankSync plugin: " + event.getReason());
    Bukkit.getPluginManager().disablePlugin(JavaPlugin.getPlugin(RankSyncPlugin.class));
  }
}
