package com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc;

import com.gmail.chickenpowerrr.ranksync.api.event.BotEnabledEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.spigot.roleresource.RankHelper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BotEnabledEventListener implements Listener<BotEnabledEvent> {

  private final RankHelper rankHelper;

  @Override
  public Class<BotEnabledEvent> getTarget() {
    return BotEnabledEvent.class;
  }

  @Override
  public void onEvent(BotEnabledEvent event) {
    rankHelper.validateRanks(event.getBot());
  }
}
