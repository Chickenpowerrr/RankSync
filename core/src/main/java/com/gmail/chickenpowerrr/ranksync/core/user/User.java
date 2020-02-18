package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
    return Collections.unmodifiableCollection(
        this.links.stream()
            .filter(UserLink::isActive)
            .map(UserLink::getAccount)
            .collect(Collectors.toSet()));
  }

  @Contract(mutates = "this")
  public <T extends Platform<T>> boolean addLink(@NotNull UserLink<T> link,
      @NotNull Collection<Reward<T>> rewards) {
    if (link.isActive()) {
      Collection<Account> accounts = getAccounts();
      if (!accounts.contains(link.getAccount())) {
        rewards.forEach(reward -> reward.apply(link.getAccount()));
        this.links.add(link);
        return true;
      }
    }
    return false;
  }
}
