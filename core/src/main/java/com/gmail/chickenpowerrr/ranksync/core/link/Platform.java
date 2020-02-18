package com.gmail.chickenpowerrr.ranksync.core.link;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public abstract class Platform<T extends Platform<T>> {

  private final String name;
  private final int maxNameLength;
  private final boolean canChangeName;
  private final Collection<RankResource> rankResources;

  public Platform(@NotNull String name, @Range(from = 1, to = Integer.MAX_VALUE) int maxNameLength,
      boolean canChangeName) {
    this.name = name;
    this.maxNameLength = maxNameLength;
    this.canChangeName = canChangeName;
    this.rankResources = new HashSet<>();
  }

  @Contract(pure = true)
  @NotNull
  public String getName() {
    return this.name;
  }

  @Contract(pure = true)
  @NotNull
  @SuppressWarnings("unchecked")
  public CompletableFuture<Collection<Rank<T>>> getRanks() {
    Collection<Rank<T>> ranks = new HashSet<>();
    List<CompletableFuture<Collection<Rank<T>>>> completableFutures = new ArrayList<>();

    for (RankResource<T> rankResource : this.rankResources) {
      // Register the resource request
      CompletableFuture<Collection<Rank<T>>> resourceCompletableFuture = rankResource.getRanks();
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
        .thenApply(aVoid -> ranks);
  }

  @Contract(pure = true)
  public boolean canChangeName() {
    return this.canChangeName;
  }

  @Contract(mutates = "this")
  public void addRankResource(@NotNull RankResource rankResource) {
    this.rankResources.add(rankResource);
  }

  @Contract(pure = true)
  @Nullable
  public abstract String formatName(@NotNull String name, @NotNull String format);

  @Contract(pure = true)
  public abstract boolean isValidName(@NotNull String name);

  @Contract(pure = true)
  public abstract boolean isValidFormat(@NotNull String format);
}
