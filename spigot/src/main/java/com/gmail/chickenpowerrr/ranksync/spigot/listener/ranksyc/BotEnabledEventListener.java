package com.gmail.chickenpowerrr.ranksync.spigot.listener.ranksyc;

import com.gmail.chickenpowerrr.ranksync.api.event.BotEnabledEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.spigot.roleresource.RankHelper;
import lombok.AllArgsConstructor;

/**
 * This class validates all ranks when the Bot enables
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
@AllArgsConstructor
public class BotEnabledEventListener implements Listener<BotEnabledEvent> {

  private final RankHelper rankHelper;

  @Override
  public Class<BotEnabledEvent> getTarget() {
    return BotEnabledEvent.class;
  }

  /**
   * Validates the ranks used by this Bot
   *
   * @param event the event that triggered the method
   */
  @Override
  public void onEvent(BotEnabledEvent event) {
    this.rankHelper.validateRanks();
  }
}
