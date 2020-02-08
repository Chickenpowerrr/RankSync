package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import java.util.Collection;
import java.util.Date;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class UserLink<T extends Platform<T>> {

  private final Date start;
  private final Date end;

  public UserLink(@NotNull Date start, @NotNull Date end) {
    this.start = start;
    this.end = end;
  }

  @Contract(pure = true)
  public boolean isActive() {
    // TODO implement
    return true;
  }

  @Contract(mutates = "this")
  public boolean unlink(@NotNull Collection<Reward<T>> rewards) {
    // TODO implement
    return true;
  }
}
