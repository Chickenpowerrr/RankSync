package com.gmail.chickenpowerrr.ranksync.core.rank;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class RankLink<T extends Platform<T>, U extends Platform<U>> {

  private final String nameFormat;
  private final Rank<T> source;
  private final Rank<U> destination;

  public RankLink(@NotNull String nameFormat, @NotNull Rank<T> source, @NotNull Rank<U> destination) {
    this.nameFormat = nameFormat;
    this.source = source;
    this.destination = destination;
  }

  @Range(from = 0, to = Integer.MAX_VALUE)
  @Contract(pure = true)
  public int getPriority() {
    // TODO implement
    return 0;
  }

  @Contract(pure = true)
  @NotNull
  public Rank<T> getSource() {
    return this.source;
  }

  @Contract(pure = true)
  @NotNull
  public Rank<U> getDestination() {
    return this.destination;
  }

  @Contract(pure = true)
  @NotNull
  public String getNameFormat() {
    return this.nameFormat;
  }
}
