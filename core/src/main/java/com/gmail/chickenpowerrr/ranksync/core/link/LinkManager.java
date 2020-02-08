package com.gmail.chickenpowerrr.ranksync.core.link;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class LinkManager {

  private final String nameFormat;
  private final boolean sendWarnings;
  private final int updateInterval;
  private final boolean syncNames;

  public LinkManager(@NotNull String nameFormat, boolean sendWarnings, int updateInterval,
      boolean syncNames) {
    this.nameFormat = nameFormat;
    this.sendWarnings = sendWarnings;
    this.updateInterval = updateInterval;
    this.syncNames = syncNames;
  }

  @Contract(pure = true)
  public boolean shouldSendWarnings() {
    return this.sendWarnings;
  }

  @Contract(pure = true)
  public boolean shouldSyncNames() {
    return this.syncNames;
  }
}
