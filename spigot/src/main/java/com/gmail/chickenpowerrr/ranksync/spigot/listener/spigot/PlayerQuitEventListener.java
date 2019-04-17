package com.gmail.chickenpowerrr.ranksync.spigot.listener.spigot;

import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerQuitEventListener implements Listener {

  private final RankSyncPlugin rankSyncPlugin = JavaPlugin.getPlugin(RankSyncPlugin.class);

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    this.rankSyncPlugin.getLinkHelper().updateRanks(event.getPlayer().getUniqueId());
  }
}
