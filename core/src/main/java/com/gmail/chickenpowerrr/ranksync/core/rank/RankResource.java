package com.gmail.chickenpowerrr.ranksync.core.rank;

import org.jetbrains.annotations.Contract;

public class RankResource {

  private final boolean caseSensitive;

  public RankResource(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  @Contract(pure = true)
  public boolean isCaseSensitive() {
    return this.caseSensitive;
  }
}
