package com.gmail.chickenpowerrr.ranksync.core.link;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankResource;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Platform<T extends Platform<T>> {

  private final String name;
  private final int maxNameLength;
  private final boolean canChangeName;
  private final Collection<Rank> ranks;
  private final Collection<RankResource> rankResources;

  public Platform(@NotNull String name, int maxNameLength, boolean canChangeName) {
    this.name = name;
    this.maxNameLength = maxNameLength;
    this.canChangeName = canChangeName;
    this.ranks = new HashSet<>();
    this.rankResources = new HashSet<>();
  }

  @NotNull
  public String getName() {
    return this.name;
  }

  @NotNull
  public Collection<Rank> getRanks() {
    return Collections.unmodifiableCollection(this.ranks);
  }

  public boolean isCanChangeName() {
    return this.canChangeName;
  }

  @Contract(mutates = "this")
  public void addRankResource(@NotNull RankResource rankResource) {
    this.rankResources.add(rankResource);
  }

  @Contract(pure = true)
  public void formatName(@NotNull String name, @NotNull String format) {
    // TODO implement
  }

  @Contract(pure = true)
  public boolean isValidName(@NotNull String name) {
    // TODO implement
    return true;
  }

  @Contract(pure = true)
  public boolean isValidFormat(@NotNull String format) {
    // TODO implement
    return true;
  }
}
