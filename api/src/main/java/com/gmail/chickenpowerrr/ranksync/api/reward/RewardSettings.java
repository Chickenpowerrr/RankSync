package com.gmail.chickenpowerrr.ranksync.api.reward;

import java.util.List;

/**
 * This interface contains the settings for a reward system
 *
 * @author Chickenpowerrr
 * @since 1.4.0
 */
public interface RewardSettings {

  /**
   * Returns the settings for what should happen on a sync
   */
  RewardAction getSyncAction();

  /**
   * Returns the settings for what should happen on an unsync
   */
  RewardAction getUnsyncAction();

  /**
   * This interface contains all settings for a reward action
   *
   * @author Chickenpowerrr
   * @since 1.4.0
   */
  interface RewardAction {

    /**
     * Returns the maximum times a user can receive the action commands can be executed for the
     * given player, -1 if it should be executed every time
     */
    int getMaxEnabled();

    /**
     * Returns if the action commands should be executed
     */
    boolean isEnabled();

    /**
     * Returns the commands that should be executed when someone syncs their account, %player%
     * is the placeholder for the player who synced their account
     */
    List<String> getCommands();
  }
}
