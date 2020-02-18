package com.gmail.chickenpowerrr.ranksync.core.rank;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.user.Account;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class RankResource<T extends Platform<T>> {

  private final boolean caseSensitive;

  public RankResource(boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  @Contract(pure = true)
  public boolean isCaseSensitive() {
    return this.caseSensitive;
  }

  @Contract(pure = true)
  @NotNull
  public abstract CompletableFuture<Collection<Rank<T>>> getRanks(@NotNull Account<T> account);

  @Contract(pure = true)
  @NotNull
  public abstract CompletableFuture<Collection<Rank<T>>> getRanks();

  @Contract(pure = false)
  public abstract void applyRanks(@NotNull Account<T> account, @NotNull Collection<Rank<T>> ranks);
}
