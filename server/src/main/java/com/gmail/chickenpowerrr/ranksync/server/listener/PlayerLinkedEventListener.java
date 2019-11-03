package com.gmail.chickenpowerrr.ranksync.server.listener;

import com.gmail.chickenpowerrr.ranksync.api.event.Listener;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkedEvent;
import com.gmail.chickenpowerrr.ranksync.api.reward.RewardSettings.RewardAction;
import lombok.AllArgsConstructor;

/**
 * This class updates a player's ranks when the account has been linked and gives the rewards if
 * needed
 *
 * @author Chickenpowerrr
 * @since 1.2.0
 */
@AllArgsConstructor
public class PlayerLinkedEventListener implements Listener<PlayerLinkedEvent> {

  private final RewardAction rewardAction;

  @Override
  public Class<PlayerLinkedEvent> getTarget() {
    return PlayerLinkedEvent.class;
  }

  /**
   * Updates a player's ranks when the account has been linked and gives the rewards if needed
   *
   * @param event the event that triggered the method
   */
  @Override
  public void onEvent(PlayerLinkedEvent event) {
    event.getPlayer().update();
    if (this.rewardAction.isEnabled()
        && event.getPlayer().getTimesSynced() <= this.rewardAction.getMax()) {
      this.rewardAction.getCommands().forEach(command -> {/*Execute*/});
    }
  }
}
