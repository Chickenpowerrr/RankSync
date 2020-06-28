package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.LinkManager;
import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankLink;
import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

  private final LinkManager linkManager;
  private final List<UserLink<?>> links;

  /**
   * Instantiates a {@code User} based on the {@code UserLink}s
   * which attaches {@code Account}s on {@code Platform}s to
   * this {@code User}.
   *
   * @param linkManager the {@link LinkManager} which which {@link Rank}s
   *                    have been linked to other {@link Rank}s
   * @param links the {@code UserLink}s which attach {@code Account}s
   *              on {@code Platform}s to this {@code User}
   */
  public User(@NotNull LinkManager linkManager, @NotNull List<UserLink<?>> links) {
    this.links = links;
    this.linkManager = linkManager;
  }

  /**
   * Returns the {@code Account}s which have been linked
   * to the current {@code User} based on the active
   * {@code UserLink}s.
   */
  @Contract(pure = true)
  @NotNull
  public Collection<Account<?>> getAccounts() {
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
      Collection<Account<?>> accounts = getAccounts();
      if (!accounts.contains(link.getAccount())) {
        rewards.forEach(reward -> reward.apply(link.getAccount()));
        this.links.add(link);
        return true;
      }
    }
    return false;
  }

  /**
   * Synchronizes the current {@link Rank}s over the {@link Platform}s
   * which have been synced to the {@link User}.
   */
  @Contract(pure = false)
  public void updateRanks() {
    // TODO update the ranks
  }

  /**
   * Synchronizes the current names over the {@link Platform}s
   * which have been synced to the {@link User}.
   */
  @Contract(pure = false)
  public void updateNames() {
    // TODO update the names
  }

  /**
   * Returns all {@link RankLink}s with the combined {@link Rank}s.
   */
  @Contract(pure = true)
  @NotNull
  public Map<Rank<?>, Collection<RankLink>> getLinks() {
    return this.linkManager.getMappedRankLinks();
  }
}
