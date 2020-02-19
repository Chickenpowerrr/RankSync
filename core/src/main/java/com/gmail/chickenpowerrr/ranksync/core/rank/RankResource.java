package com.gmail.chickenpowerrr.ranksync.core.rank;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class retrieves {@code Rank}s for a certain
 * {@code Platform}.
 *
 * @param <T> the type of the {@code Platform} which
 *            depends on this {@code RankResource} to
 *            get its {@code Rank}s
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class RankResource<T extends Platform<T>> {

  private final boolean caseSensitive;

  /**
   * Instantiates a new {@code RankResource} with the
   * information if the resource is case sensitive or
   * not.
   *
   * @param caseSensitive true if the case for the
   *                      {@code Rank}s is relevant,
   *                      false otherwise
   */
  public RankResource(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  /**
   * Returns true if the case for the {@code Rank}s
   * is relevant, false otherwise.
   */
  @Contract(pure = true)
  public boolean isCaseSensitive() {
    return this.caseSensitive;
  }

  /**
   * Returns a {@code CompletableFuture} which will be completed
   * once the {@code RankResource} has determined which {@code Rank}s
   * a certain {@code Account} has on a {@code Platform}.
   *
   * @param account the {@code Account} might have {@code Rank}s
   *                on the {@code Platform}
   * @return a {@code CompletableFuture} which will be completed
   *         once the {@code RankResource} has determined which
   *         {@code Rank}s a certain {@code Account} has on a
   *         {@code Platform}
   */
  @Contract(pure = true)
  @NotNull
  public abstract CompletableFuture<Collection<Rank<T>>> getRanks(@NotNull Account<T> account);

  /**
   * Returns a {@code CompletableFuture} which will be completed
   * once the {@code RankResource} has determined which {@code Rank}s
   * have been linked on its {@code Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public abstract CompletableFuture<Collection<Rank<T>>> getRanks();

  /**
   * Gives all specified {@code Rank}s to the specified
   * {@code Account} on a {@code Platform}.
   *
   * @param account the {@code Account} which will receive
   *                the {@code Rank}s.
   * @param ranks the new {@code Rank}s for the {@code Account}
   */
  @Contract(pure = false)
  public abstract void applyRanks(@NotNull Account<T> account, @NotNull Collection<Rank<T>> ranks);
}
