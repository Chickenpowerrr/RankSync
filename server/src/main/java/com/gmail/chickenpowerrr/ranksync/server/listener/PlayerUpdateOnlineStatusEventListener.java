package com.gmail.chickenpowerrr.ranksync.server.listener;

import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerUpdateOnlineStatusEvent;

/**
 * This class updates a player's ranks when its online status changes
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class PlayerUpdateOnlineStatusEventListener implements
    Listener<PlayerUpdateOnlineStatusEvent> {

  @Override
  public Class<PlayerUpdateOnlineStatusEvent> getTarget() {
    return PlayerUpdateOnlineStatusEvent.class;
  }

  /**
   * Updates a player's ranks when its online status changes
   *
   * @param event the event that triggered the method
   */
  @SuppressWarnings("unchecked")
  @Override
  public void onEvent(PlayerUpdateOnlineStatusEvent event) {
    event.getPlayer().update();
  }
}
