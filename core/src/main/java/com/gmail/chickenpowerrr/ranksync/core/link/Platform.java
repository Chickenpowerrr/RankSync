package com.gmail.chickenpowerrr.ranksync.core.link;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import java.util.Collection;
import java.util.HashSet;
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
  public CompletableFuture<Collection<Rank>> getRanks() {
    // TODO implement
    return null;
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
