package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@code User} which can have
 * {@code Account} attached to it through several
 * {@code UserLink}.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class User {

  private final List<UserLink> links;

  /**
   * Instantiates a {@code User} based on the {@code UserLink}s
   * which attaches {@code Account}s on {@code Platform}s to
   * this {@code User}.
   *
   * @param links the {@code UserLink}s which attach {@code Account}s
   *              on {@code Platform}s to this {@code User}
   */
  public User(@NotNull List<UserLink> links) {
    this.links = links;
  }

  /**
   * Returns the {@code Account}s which have been linked
   * to the current {@code User} based on the active
   * {@code UserLink}s.
   */
  @Contract(pure = true)
  @NotNull
  public Collection<Account> getAccounts() {
    return Collections.unmodifiableCollection(
        this.links.stream()
            .filter(UserLink::isActive)
            .map(UserLink::getAccount)
            .collect(Collectors.toSet()));
  }

  /**
   * Adds a {@code UserLink} to the current {@code User}
   * which links an {@code Account} to this {@code User}
   * if the {@code UserLink} is currently active. If it
   * is active, the given {@code Reward}s will be issued
   * to the {@code Account}.
   *
   * @param link the {@code UserLink} which should link the
   *             {@code Account} to the {@code User}
   * @param rewards the {@code Reward}s which should be issued
   *                if the link was successful
   * @param <T> the type of the {@code Platform} of the {@code Account}
   * @return true if the {@code UserLink} was active, false
   *         otherwise
   */
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
