package com.gmail.chickenpowerrr.ranksync.spigot.listener.spigot;

import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AsyncPlayerPreLoginEventListener implements Listener {

    private final RankSyncPlugin rankSyncPlugin = JavaPlugin.getPlugin(RankSyncPlugin.class);

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
       this.rankSyncPlugin.getLinkHelper().updateRanks(event.getUniqueId());
    }
}
