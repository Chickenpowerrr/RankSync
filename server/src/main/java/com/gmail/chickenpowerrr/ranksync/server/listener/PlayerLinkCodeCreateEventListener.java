package com.gmail.chickenpowerrr.ranksync.server.listener;

import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkCodeCreateEvent;
import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import lombok.AllArgsConstructor;

/**
 * This class notifies the LinkHelper when a code gets created
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@AllArgsConstructor
public class PlayerLinkCodeCreateEventListener implements Listener<PlayerLinkCodeCreateEvent> {

  private final LinkHelper linkHelper;

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
    this.linkHelper.addAuthenticationKey(event.getPlayer(), event.getCode());
  }
}
