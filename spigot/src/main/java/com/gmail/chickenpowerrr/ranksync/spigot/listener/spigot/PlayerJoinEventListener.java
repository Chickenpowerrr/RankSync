package com.gmail.chickenpowerrr.ranksync.spigot.listener.spigot;

import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerJoinEventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper().updateRanks(event.getPlayer().getUniqueId());
    }
}
