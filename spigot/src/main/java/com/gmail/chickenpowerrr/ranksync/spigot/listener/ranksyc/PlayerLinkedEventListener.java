package com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc;

import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkedEvent;

/**
 * This class updates a player's ranks when the account has been linked
 *
 * @author Chickenpowerrr
 * @since 1.2.0
 */
public class PlayerLinkedEventListener implements Listener<PlayerLinkedEvent> {

  @Override
  public Class<PlayerLinkedEvent> getTarget() {
    return PlayerLinkedEvent.class;
  }

  /**
   * Updates a player's ranks when the account has been linked
   *
   * @param event the event that triggered the method
   */
  @Override
  public void onEvent(PlayerLinkedEvent event) {
    event.getPlayer().updateRanks();
  }
}
