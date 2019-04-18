package com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc;

import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkCodeCreateEvent;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class notifies the LinkHelper when a code gets created
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class PlayerLinkCodeCreateEventListener implements Listener<PlayerLinkCodeCreateEvent> {

  private final RankSyncPlugin rankSyncPlugin = JavaPlugin.getPlugin(RankSyncPlugin.class);

  @Override
  public Class<PlayerLinkCodeCreateEvent> getTarget() {
    return PlayerLinkCodeCreateEvent.class;
  }

  /**
   * Notifies the LinkHelper when a code gets created
   *
   * @param event the event that triggered the method
   */
  @Override
  public void onEvent(PlayerLinkCodeCreateEvent event) {
    this.rankSyncPlugin.getLinkHelper().addAuthenticationKey(event.getPlayer(), event.getCode());
  }
}
