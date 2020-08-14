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
 * This class represents a {@link Platform} which has
 * {@link Account}s and {@link Rank}s which can be
 * synced with other {@link Platform}s.
 *
 * @param <T> the type of the {@link Platform}
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class Platform<T extends Platform<T>> {

  private final String name;
  private final String baseNameFormat;
  private final boolean canChangeName;
  private final boolean isSourcePlatform;
  private final Collection<RankResource<T>> rankResources;

  /**
   * Initializes a new {@link Platform} based on the name
   * of the {@link Platform}, the maximum length of the
   * name of an {@link Account} and if the application
   * is able to change names on this {@link Platform}.
   *
   * @param name the name of this {@link Platform}
   * @param baseNameFormat the default name format for
   *                       the name sync feature if the
   *                       {@link Rank} doesn't override
   *                       it
   * @param canChangeName true if the application is
   *                      able to change the name of an
   *                      {@link Account} on the {@link Platform},
   *                      false otherwise
   * @param isSourcePlatform {@code true} if the {@link Platform}
   *                         should be treated as the source of
   *                         the {@link Rank}s, {@code false} otherwise
   */
  public Platform(@NotNull String name, @NotNull String baseNameFormat, boolean canChangeName,
      boolean isSourcePlatform) {
    this.name = name;
    this.baseNameFormat = baseNameFormat;
    this.canChangeName = canChangeName;
    this.isSourcePlatform = isSourcePlatform;
    this.rankResources = new HashSet<>();
  }

  /**
   * Returns the name of the {@link Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public String getName() {
    return this.name;
  }

  /**
   * Returns a {@link CompletableFuture} which will be completed
   * once all {@link RankResource}s have submitted which {@link Rank}s
   * they are syncing. The result will be a {@link List} which
   * contains all {@link Rank}s synced by the {@link RankResource}s.
   */
  @Contract(pure = true)
  @NotNull
  public CompletableFuture<List<Rank<T>>> getRanks() {
    return getRanks(RankResource::getRanks);
  }

  /**
   * Returns a {@link CompletableFuture} which will be completed
   * once all {@link RankResource}s have submitted which {@link Rank}s
   * a certain {@link Account} has on this {@link Platform}. The result
   * will be a {@link List} which contains all {@link Rank}s
   * determined by the {@link RankResource}s.
   *
   * @param account the {@link Account} which {@link Rank}s are requested
   * @return a {@link CompletableFuture} which will be completed
   *         once all {@link RankResource}s have submitted which
   *         {@link Rank}s a certain {@link Account} has on this
   *         {@link Platform}. The result will be a {@link Collection}
   *         which contains all {@link Rank}s determined by the
   *         {@link RankResource}s.
   */
  @Contract(pure = true)
  @NotNull
  public CompletableFuture<List<Rank<T>>> getRanks(Account<T> account) {
    return getRanks(rankResource -> rankResource.getRanks(account));
  }

  /**
   * Returns a {@link CompletableFuture} which will be completed
   * once all {@link RankResource}s have submitted which {@link Rank}s
   * they are syncing. The result will be a {@link List} which
   * contains all {@link Rank}s synced by the {@link RankResource}s.
   *
   * @param function the {@link Function} which retrieves a
   *                 {@link CompletableFuture} which will be used
   *                 to determine the {@link Rank}s with which the
   *                 result will be completed
   * @return a {@link CompletableFuture} which will be completed
   *        once all {@link RankResource}s have submitted which
   *        {@link Rank}s they are syncing. The result will be
   *        a {@link Collection} which contains all {@link Rank}s
   *        synced by the {@link RankResource}s.
   */
  @Contract(pure = true)
  @NotNull
  private CompletableFuture<List<Rank<T>>> getRanks(
      Function<RankResource<T>, CompletableFuture<Collection<Rank<T>>>> function) {
    List<Rank<T>> ranks = new ArrayList<>();
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
   * the name of an {@link Account} on the {@link Platform},
   * false otherwise.
   */
  @Contract(pure = true)
  public boolean canChangeName() {
    return this.canChangeName;
  }

  /**
   * Adds a {@link RankResource} which is able to retrieve
   * {@link Rank}s or this {@link Platform}.
   *
   * @param rankResource the {@link RankResource} which is able
   *                     to manage certain {@link Rank}s on
   *                     this {@link Platform}
   */
  @Contract(mutates = "this")
  public void addRankResource(@NotNull RankResource<T> rankResource) {
    this.rankResources.add(rankResource);
  }

  /**
   * Returns the default name format for the name sync
   * feature if the {@link Rank} doesn't override it.
   */
  @Contract(pure = true)
  @NotNull
  public String getBaseNameFormat() {
    return this.baseNameFormat;
  }

  /**
   * Returns if the provided, formatted name is valid on this
   * {@link Platform}.
   *
   * @param name the formatted name which will be validated
   * @return true if the name is valid on this {@link Platform},
   *         false otherwise
   */
  @Contract(pure = true)
  public abstract boolean isValidName(@NotNull String name);

  /**
   * Returns if the provided, format is valid on this
   * {@link Platform}.
   *
   * @param format the format which will be validated
   * @return true if the format is valid on this {@link Platform},
   *         false otherwise
   */
  @Contract(pure = true)
  public abstract boolean isValidFormat(@NotNull String format);

  /**
   * Returns {@code true} if the {@link Platform} should be treated as
   * the source of the {@link Rank}s, {@code false} otherwise.
   */
  public boolean isSourcePlatform() {
    return this.isSourcePlatform;
  }
}
