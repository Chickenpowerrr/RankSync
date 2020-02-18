package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class Account<T extends Platform<T>> {

  private final String identifier;
  private final List<UserLink> links;

  public Account(@NotNull String identifier, @NotNull List<UserLink> links) {
    this.identifier = identifier;
    this.links = links;
  }

  @Contract(pure = true)
  @NotNull
  public String getIdentifier() {
    return this.identifier;
  }

  @Contract(pure = true)
  @NotNull
  public abstract String getName();

  @Contract(pure = false)
  public abstract boolean updateName(@NotNull String name);
}
