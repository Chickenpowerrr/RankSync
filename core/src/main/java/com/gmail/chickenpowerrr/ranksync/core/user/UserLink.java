package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserLink<T extends Platform<T>> {

  private final Account<T> account;
  private final User user;
  private final Date start;
  private Date end;

  public UserLink(@NotNull Account<T> account, @NotNull User user, @NotNull Date start,
      @Nullable Date end) {
    this.account = account;
    this.user = user;
    this.start = start;
    this.end = end;
  }

  public UserLink(@NotNull Account<T> account, @NotNull User user, @NotNull Date start) {
    this(account, user, start, null);
  }

  public UserLink(@NotNull Account<T> account, @NotNull User user) {
    this(account, user, Date.from(Instant.now()));
  }

  @Contract(pure = true)
  public boolean isActive() {
    return (this.end == null || this.end.after(Date.from(Instant.now()))
        && this.start.before(Date.from(Instant.now())));
  }

  @Contract(mutates = "this")
  public boolean unlink(@NotNull Collection<Reward<T>> rewards) {
    if (isActive()) {
      this.end = Date.from(Instant.now());
      rewards.forEach(reward -> reward.apply(this.account));
      return true;
    } else {
      return false;
    }
  }

  @Contract(pure = true)
  @NotNull
  public User getUser() {
    return this.user;
  }

  @Contract(pure = true)
  @NotNull
  public Account<T> getAccount() {
    return this.account;
  }
}
