package com.gmail.chickenpowerrr.ranksync.core.user;

import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class User {

  private final List<UserLink> links;

  public User(@NotNull List<UserLink> links) {
    this.links = links;
  }

  @Contract(pure = true)
  @NotNull
  public Collection<Account> getAccounts() {
    // TODO implement
    return null;
  }

  @Contract(mutates = "this")
  public boolean addLink(@NotNull UserLink link) {
    // TODO implement
    return true;
  }
}
