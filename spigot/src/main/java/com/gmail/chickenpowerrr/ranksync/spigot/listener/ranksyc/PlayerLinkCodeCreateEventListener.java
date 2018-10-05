package com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc;

import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkCodeCreateEvent;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerLinkCodeCreateEventListener implements Listener<PlayerLinkCodeCreateEvent> {

    @Override
    public Class<PlayerLinkCodeCreateEvent> getTarget() {
        return PlayerLinkCodeCreateEvent.class;
    }

    @Override
    public void onEvent(PlayerLinkCodeCreateEvent event) {
        JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper().addAuthenticationKey(event.getPlayer(), event.getCode());
    }
}
