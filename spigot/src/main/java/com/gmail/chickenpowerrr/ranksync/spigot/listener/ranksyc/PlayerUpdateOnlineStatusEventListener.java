package com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc;

import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerUpdateOnlineStatusEvent;

public class PlayerUpdateOnlineStatusEventListener implements
    Listener<PlayerUpdateOnlineStatusEvent> {

  @Override
  public Class<PlayerUpdateOnlineStatusEvent> getTarget() {
    return PlayerUpdateOnlineStatusEvent.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onEvent(PlayerUpdateOnlineStatusEvent event) {
    event.getPlayer().updateRanks();
  }
}
