package com.gmail.chickenpowerrr.ranksync.core.rank;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class retrieves {@link Rank}s for a certain
 * {@link Platform}.
 *
 * @param <T> the type of the {@link Platform} which
 *            depends on this {@link RankResource} to
 *            get its {@link Rank}s
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class RankResource<T extends Platform<T>> {

  private final boolean caseSensitive;

  /**
   * Instantiates a new {@link RankResource} with the
   * information if the resource is case sensitive or
   * not.
   *
   * @param caseSensitive true if the case for the
   *                      {@link Rank}s is relevant,
   *                      false otherwise
   */
  public RankResource(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  /**
   * Returns true if the case for the {@link Rank}s
   * is relevant, false otherwise.
   */
  @Contract(pure = true)
  public boolean isCaseSensitive() {
    return this.caseSensitive;
  }

  /**
   * Returns a {@link CompletableFuture} which will be completed
   * once the {@link RankResource} has determined which {@link Rank}s
   * have been linked on its {@link Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public abstract CompletableFuture<Collection<Rank<T>>> getRanks();

  /**
   * Returns a {@link CompletableFuture} which will be completed
   * once the {@link RankResource} has determined which {@link Rank}s
   * a certain {@link Account} has on a {@link Platform}.
   *
   * @param account the {@link Account} might have {@link Rank}s
   *                on the {@link Platform}
   * @return a {@link CompletableFuture} which will be completed
   *         once the {@link RankResource} has determined which
   *         {@link Rank}s a certain {@link Account} has on a
   *         {@link Platform}
   */
  @Contract(pure = true)
  @NotNull
  public abstract CompletableFuture<Collection<Rank<T>>> getRanks(@NotNull Account<T> account);

  /**
   * Gives all specified {@link Rank}s to the specified
   * {@link Account} on a {@link Platform}.
   *
   * @param account the {@link Account} which will receive
   *                the {@link Rank}s.
   * @param ranks the new {@link Rank}s for the {@link Account}
   */
  public abstract void applyRanks(@NotNull Account<T> account, @NotNull Collection<Rank<T>> ranks);
}
