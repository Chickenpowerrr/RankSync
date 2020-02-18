package com.gmail.chickenpowerrr.ranksync.core.reward;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class Reward<T extends Platform<T>> {

  private final T platform;
  private final String identifier;
  private final int maxIssues;

  public Reward(@NotNull T platform, @NotNull String identifier, int maxIssues) {
    this.platform = platform;
    this.identifier = identifier;
    this.maxIssues = maxIssues;
  }

  @Contract(pure = true)
  @NotNull
  public String getIdentifier() {
    return this.identifier;
  }

  @Contract(pure = true)
  public int getMaxIssues() {
    return this.maxIssues;
  }

  public abstract void apply(@NotNull Account<T> account);

  @Contract(pure = true)
  @NotNull
  protected T getPlatform() {
    return this.platform;
  }
}
