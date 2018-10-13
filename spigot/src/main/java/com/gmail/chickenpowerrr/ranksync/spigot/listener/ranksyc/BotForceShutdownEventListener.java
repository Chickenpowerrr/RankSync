package com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc;

import com.gmail.chickenpowerrr.ranksync.api.event.BotForceShutdownEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BotForceShutdownEventListener implements Listener<BotForceShutdownEvent> {

    @Override
    public Class<BotForceShutdownEvent> getTarget() {
        return BotForceShutdownEvent.class;
    }

    @Override
    public void onEvent(BotForceShutdownEvent event) {
        Bukkit.getLogger().severe("Disabling the RankSync plugin: " + event.getReason());
        Bukkit.getPluginManager().disablePlugin(JavaPlugin.getPlugin(RankSyncPlugin.class));
    }
}
