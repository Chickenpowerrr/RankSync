package com.gmail.chickenpowerrr.ranksync.core.rank;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class Rank<T extends Platform<T>> {

  private final String identifier;
  private final int priority;
  private final String name;

  public Rank(@NotNull String identifier, int priority, @NotNull String name) {
    this.identifier = identifier;
    this.priority = priority;
    this.name = name;
  }

  @Contract(pure = true)
  @NotNull
  public String getIdentifier() {
    return this.identifier;
  }

  @Range(from = 0, to = Integer.MAX_VALUE)
  @Contract(pure = true)
  public int getPriority() {
    return this.priority;
  }

  @Contract(pure = true)
  @NotNull
  public String getName() {
    return this.name;
  }
}
