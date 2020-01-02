package com.gmail.chickenpowerrr.ranksync.server.listener;

import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerUnlinkedEvent;
import com.gmail.chickenpowerrr.ranksync.api.reward.RewardSettings.RewardAction;
import lombok.AllArgsConstructor;

/**
 * This class gives the unsync rewards if needed
 *
 * @author Chickenpowerrr
 * @since 1.4.0
 */
@AllArgsConstructor
public class PlayerUnlinkedEventListener implements Listener<PlayerUnlinkedEvent> {

  private final RewardAction rewardAction;

  @Override
  public Class<PlayerUnlinkedEvent> getTarget() {
    return PlayerUnlinkedEvent.class;
  }

  /**
   * Executes the unsync commands if needed
   *
   * @param event the event that triggered the method
   */
  @Override
  public void onEvent(PlayerUnlinkedEvent event) {
    if (this.rewardAction.isEnabled()
        && (this.rewardAction.getMax() < 0
        || event.getPlayer().getTimesUnsynced() < this.rewardAction.getMax())) {
      this.rewardAction.executeCommands(event.getPlayer());
    }
  }
}
