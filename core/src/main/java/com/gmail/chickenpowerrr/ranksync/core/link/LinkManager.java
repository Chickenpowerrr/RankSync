package com.gmail.chickenpowerrr.ranksync.core.link;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class manages all {@code Link}s and syncs for
 * the registered {@code User}s.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class LinkManager {

  private final String nameFormat;
  private final boolean sendWarnings;
  private final int updateInterval;
  private final boolean syncNames;

  /**
   * Instantiates a new manager, based on the format used
   * for the name synchronization, if it should send warnings
   * when it does not have enough permissions to perform a
   * certain action, the interval in which it should regularly
   * update the {@code Rank}s and if the name sync feature has
   * been enabled.
   *
   * @param nameFormat the format used for the name sync feature
   * @param sendWarnings true if the program should print warnings
   *                     when it does not have sufficient permissions
   *                     to perform a certain action
   * @param updateInterval the interval in seconds in which the program
   *                       will automatically check if all assets have
   *                       been synced correctly, -1 if this feature
   *                       should be disabled
   * @param syncNames true if the name sync feature is enabled,
   *                  false otherwise
   */
  public LinkManager(@NotNull String nameFormat, boolean sendWarnings, int updateInterval,
      boolean syncNames) {
    this.nameFormat = nameFormat;
    this.sendWarnings = sendWarnings;
    this.updateInterval = updateInterval;
    this.syncNames = syncNames;
  }

  /**
   * Returns true if the program should print warnings when it
   * does not have sufficient permissions to perform a certain action.
   */
  @Contract(pure = true)
  public boolean shouldSendWarnings() {
    return this.sendWarnings;
  }

  /**
   * Returns true if the name sync feature is enabled, false otherwise.
   */
  @Contract(pure = true)
  public boolean shouldSyncNames() {
    return this.syncNames;
  }
}
