package com.gmail.chickenpowerrr.ranksync.core.reward;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link Reward} which
 * can be issue to an {@link Account}.
 *
 * @param <T> the {@link Platform} on which the
 *            {@link Reward} can be issued
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class Reward<T extends Platform<T>> {

  private final T platform;
  private final String identifier;
  private final int maxIssues;

  /**
   * Initializes a new {@link Reward} based on the
   * {@link Platform} on which this {@link Reward}
   * will be issued, the identifier of this {@link Reward}
   * and the maximum times the {@link Reward} can be issued
   * to a certain {@link Account}.
   *
   * @param platform the {@link Platform} on which the
   *                 {@link Reward} will be issued
   * @param identifier the identifier of this {@link Reward}
   * @param maxIssues 0 if this {@link Reward} cannot be issued,
   *                  a negative number if it can be issued
   *                  unlimited times and a positive number if
   *                  the {@link Reward} can be issued a limited
   *                  amount of times
   */
  public Reward(@NotNull T platform, @NotNull String identifier, int maxIssues) {
    this.platform = platform;
    this.identifier = identifier;
    this.maxIssues = maxIssues;
  }

  /**
   * Returns the identifier of this {@link Reward}.
   */
  @Contract(pure = true)
  @NotNull
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Returns 0 if this {@link Reward} cannot be issued,
   * a negative number if it can be issued unlimited
   * times and a positive number if the {@link Reward}
   * can be issued a limited amount of times.
   */
  @Contract(pure = true)
  public int getMaxIssues() {
    return this.maxIssues;
  }

  /**
   * Issues the {@link Reward} to a specified {@link Account}
   * when possible.
   *
   * @param account the {@link Account} which will receive the
   *                {@link Reward}
   * @return true if the {@link Reward} could be issued to the
   *         specified {@link Account}, false otherwise
   */
  public abstract boolean apply(@NotNull Account<T> account);

  /**
   * Returns the {@link Platform} on which this {@link Reward}
   * will be issued.
   */
  @Contract(pure = true)
  @NotNull
  protected T getPlatform() {
    return this.platform;
  }
}
