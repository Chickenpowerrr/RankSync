package com.gmail.chickenpowerrr.ranksync.core.link;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@code Platform} which has
 * {@code Account}s and {@code Rank}s which can be
 * synced with other {@code Platform}s.
 *
 * @param <T> the type of the {@code Platform}
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class Platform<T extends Platform<T>> {

  private final String name;
  private final String baseNameFormat;
  private final boolean canChangeName;
  private final Collection<RankResource<T>> rankResources;

  /**
   * Initializes a new {@code Platform} based on the name
   * of the {@code Platform}, the maximum length of the
   * name of an {@code Account} and if the application
   * is able to change names on this {@code Platform}.
   *
   * @param name the name of this {@code Platform}
   * @param baseNameFormat the default name format for
   *                       the name sync feature if the
   *                       {@code Rank} doesn't override
   *                       it
   * @param canChangeName true if the application is
   *                      able to change the name of an
   *                      {@code Account} on the {@code Platform},
   *                      false otherwise
   */
  public Platform(@NotNull String name, @NotNull String baseNameFormat, boolean canChangeName) {
    this.name = name;
    this.baseNameFormat = baseNameFormat;
    this.canChangeName = canChangeName;
    this.rankResources = new HashSet<>();
  }

  /**
   * Returns the name of the {@code Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public String getName() {
    return this.name;
  }

  /**
   * Returns a {@code CompletableFuture} which will be completed
   * once all {@code RankResource}s have submitted which {@code Rank}s
   * they are syncing. The result will be a {@code Collection} which
   * contains all {@code Rank}s synced by the {@code RankResource}s.
   */
  @Contract(pure = true)
  @NotNull
  public CompletableFuture<Collection<Rank<T>>> getRanks() {
    return getRanks(RankResource::getRanks);
  }

  /**
   * Returns a {@code CompletableFuture} which will be completed
   * once all {@code RankResource}s have submitted which {@code Rank}s
   * a certain {@code Account} has on this {@code Platform}. The result
   * will be a {@code Collection} which contains all {@code Rank}s
   * determined by the {@code RankResource}s.
   *
   * @param account the {@code Account} which {@code Rank}s are requested
   * @return a {@code CompletableFuture} which will be completed
   *         once all {@code RankResource}s have submitted which
   *         {@code Rank}s a certain {@code Account} has on this
   *         {@code Platform}. The result will be a {@code Collection}
   *         which contains all {@code Rank}s determined by the
   *         {@code RankResource}s.
   */
  @Contract(pure = true)
  @NotNull
  public CompletableFuture<Collection<Rank<T>>> getRanks(Account<T> account) {
    return getRanks(rankResource -> rankResource.getRanks(account));
  }

  /**
   * Returns a {@code CompletableFuture} which will be completed
   * once all {@code RankResource}s have submitted which {@code Rank}s
   * they are syncing. The result will be a {@code Collection} which
   * contains all {@code Rank}s synced by the {@code RankResource}s.
   *
   * @param function the {@code Function} which retrieves a
   *                 {@code CompletableFuture} which will be used
   *                 to determine the {@code Rank}s with which the
   *                 result will be completed
   * @return a {@code CompletableFuture} which will be completed
   *        once all {@code RankResource}s have submitted which
   *        {@code Rank}s they are syncing. The result will be
   *        a {@code Collection} which contains all {@code Rank}s
   *        synced by the {@code RankResource}s.
   */
  @Contract(pure = true)
  @NotNull
  private CompletableFuture<Collection<Rank<T>>> getRanks(
      Function<RankResource<T>, CompletableFuture<Collection<Rank<T>>>> function) {
    Collection<Rank<T>> ranks = new ArrayList<>();
    List<CompletableFuture<Collection<Rank<T>>>> completableFutures = new ArrayList<>();

    for (RankResource<T> rankResource : this.rankResources) {
      // Register the resource request
      CompletableFuture<Collection<Rank<T>>> resourceCompletableFuture = function
          .apply(rankResource);
      resourceCompletableFuture
          .thenAccept(ranks::addAll)
          .exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
          });
      completableFutures.add(resourceCompletableFuture);
    }

    // Complete if all resources have been completed
    return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
        .thenApply(a -> ranks);
  }

  /**
   * Returns true if the application is able to change
   * the name of an {@code Account} on the {@code Platform},
   * false otherwise.
   */
  @Contract(pure = true)
  public boolean canChangeName() {
    return this.canChangeName;
  }

  /**
   * Adds a {@code RankResource} which is able to retrieve
   * {@code Rank}s or this {@code Platform}.
   *
   * @param rankResource the {@code RankResource} which is able
   *                     to manage certain {@code Rank}s on
   *                     this {@code Platform}
   */
  @Contract(mutates = "this")
  public void addRankResource(@NotNull RankResource<T> rankResource) {
    this.rankResources.add(rankResource);
  }

  /**
   * Returns the default name format for the name sync
   * feature if the {@code Rank} doesn't override it.
   */
  @Contract(pure = true)
  @NotNull
  public String getBaseNameFormat() {
    return this.baseNameFormat;
  }

  /**
   * Returns if the provided, formatted name is valid on this
   * {@code Platform}.
   *
   * @param name the formatted name which will be validated
   * @return true if the name is valid on this {@code Platform},
   *         false otherwise
   */
  @Contract(pure = true)
  public abstract boolean isValidName(@NotNull String name);

  /**
   * Returns if the provided, format is valid on this
   * {@code Platform}.
   *
   * @param format the format which will be validated
   * @return true if the format is valid on this {@code Platform},
   *         false otherwise
   */
  @Contract(pure = true)
  public abstract boolean isValidFormat(@NotNull String format);
}
