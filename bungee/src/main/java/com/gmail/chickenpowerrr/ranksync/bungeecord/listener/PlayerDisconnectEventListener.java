package com.gmail.chickenpowerrr.ranksync.bungeecord.listener;

import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * This class updates a player's ranks when they disconnect from the network
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
@AllArgsConstructor
public class PlayerDisconnectEventListener implements Listener {

  private final LinkHelper linkHelper;

  /**
   * Updates a player's ranks when they disconnect from the network
   *
   * @param event the event that triggered the method
   */
  @EventHandler
  public void onDisconnect(PlayerDisconnectEvent event) {
    this.linkHelper.updateRanks(event.getPlayer().getUniqueId());
  }
}
