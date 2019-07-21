package com.gmail.chickenpowerrr.ranksync.spigot.listener;

import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * This class updates a player's ranks when it joins the server
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@AllArgsConstructor
public class AsyncPlayerPreLoginEventListener implements Listener {

  private final LinkHelper linkHelper;

  /**
   * Updates a player's ranks when it joins the server
   *
   * @param event the event that triggered the method
   */
  @EventHandler
  public void onJoin(AsyncPlayerPreLoginEvent event) {
    this.linkHelper.updateRanks(event.getUniqueId());
  }
}
