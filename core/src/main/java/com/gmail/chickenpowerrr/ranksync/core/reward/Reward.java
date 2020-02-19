package com.gmail.chickenpowerrr.ranksync.core.reward;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@code Reward} which
 * can be issue to an {@code Account}.
 *
 * @param <T> the {@code Platform} on which the
 *            {@code Reward} can be issued
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class Reward<T extends Platform<T>> {

  private final T platform;
  private final String identifier;
  private final int maxIssues;

  /**
   * Initializes a new {@code Reward} based on the
   * {@code Platform} on which this {@code Reward}
   * will be issued, the identifier of this {@code Reward}
   * and the maximum times the {@code Reward} can be issued
   * to a certain {@code Account}.
   *
   * @param platform the {@code Platform} on which the
   *                 {@code Reward} will be issued
   * @param identifier the identifier of this {@code Reward}
   * @param maxIssues 0 if this {@code Reward} cannot be issued,
   *                  a negative number if it can be issued
   *                  unlimited times and a positive number if
   *                  the {@code Reward} can be issued a limited
   *                  amount of times
   */
  public Reward(@NotNull T platform, @NotNull String identifier, int maxIssues) {
    this.platform = platform;
    this.identifier = identifier;
    this.maxIssues = maxIssues;
  }

  /**
   * Returns the identifier of this {@code Reward}.
   */
  @Contract(pure = true)
  @NotNull
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Returns 0 if this {@code Reward} cannot be issued,
   * a negative number if it can be issued unlimited
   * times and a positive number if the {@code Reward}
   * can be issued a limited amount of times.
   */
  @Contract(pure = true)
  public int getMaxIssues() {
    return this.maxIssues;
  }

  /**
   * Issues the {@code Reward} to a specified {@code Account}
   * when possible.
   *
   * @param account the {@code Account} which will receive the
   *                {@code Reward}
   * @return true if the {@code Reward} could be issued to the
   *         specified {@code Account}, false otherwise
   */
  public abstract boolean apply(@NotNull Account<T> account);

  /**
   * Returns the {@code Platform} on which this {@code Reward}
   * will be issued.
   */
  @Contract(pure = true)
  @NotNull
  protected T getPlatform() {
    return this.platform;
  }
}
