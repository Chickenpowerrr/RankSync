package com.gmail.chickenpowerrr.ranksync.spigot.listener.spigot;

import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerQuitEventListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper().updateRanks(event.getPlayer().getUniqueId());
    }
}
