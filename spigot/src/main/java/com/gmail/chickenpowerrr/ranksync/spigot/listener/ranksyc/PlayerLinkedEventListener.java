package com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc;

import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkedEvent;

public class PlayerLinkedEventListener implements Listener<PlayerLinkedEvent> {

  @Override
  public Class<PlayerLinkedEvent> getTarget() {
    return PlayerLinkedEvent.class;
  }

  @Override
  public void onEvent(PlayerLinkedEvent event) {
    event.getPlayer().updateRanks();
  }
}
