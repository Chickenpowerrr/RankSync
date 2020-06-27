package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an {@code Account} on
 * a {@code Platform}.
 *
 * @param <T> the type of the {@code Platform} o
 *            the {@code Account}
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class Account<T extends Platform<T>> {

  private final String identifier;
  private final Platform<T> platform;
  private final List<UserLink<T>> links;

  /**
   * Instantiates an {@code Account} based on
   * the unique identifier and the {@code UserLink}s
   * which connect the {@code Account} to {@code User}s.
   *
   * @param identifier the unique identifier of the
   *                   {@code Account} on the {@code Platform}
   * @param platform the {@code Platform} on which this
   *                 {@code Account} is synced
   * @param links the {@code UserLink}s which connects the
   *              {@code Account} to {@code User}s
   */
  public Account(@NotNull String identifier, @NotNull Platform<T> platform,
      @NotNull List<UserLink<T>> links) {
    this.identifier = identifier;
    this.platform = platform;
    this.links = links;
  }

  /**
   * Returns the unique identifier of the {@code Account}
   * on its {@code Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Returns the name of the {@code Account} on the
   * {@code Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public abstract String getName();

  /**
   * Updates the name of the {@code Account} on the
   * {@code Platform}, if possible.
   *
   * @param name the name of the {@code Account} on
   *             the {@code Platform}
   * @return true if the update was successful, false
   *         otherwise
   */
  @Contract(pure = false)
  public abstract boolean updateName(@NotNull String name);

  /**
   * Format the name of an {@code Account} based on the
   * formats found in the {@code Platform} and the
   * {@code Rank}s.
   *
   * @return the formatted name of the {@code Account}
   */
  @Contract(pure = true)
  @NotNull
  public abstract CompletableFuture<String> formatName();

  /**
   * Returns the {@code Platform} on which this {@code Account}
   * is located.
   */
  @NotNull
  public Platform<T> getPlatform() {
    return this.platform;
  }

  /**
   * Sets the {@link Rank}s of the {@link Account} on the current
   * {@link Platform} to the given {@link Rank}s.
   *
   * @param ranks the new {@link Rank}s of the {@link Account} on
   *              the {@link Platform}
   */
  public abstract void updateRanks(@NotNull List<Rank<T>> ranks);
}
