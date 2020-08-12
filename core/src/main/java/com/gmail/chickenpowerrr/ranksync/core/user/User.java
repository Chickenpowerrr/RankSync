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
import sun.awt.image.ImageWatched.Link;

/**
 * This class represents a {@link User} which can have
 * {@link Account} attached to it through several
 * {@link UserLink}.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class User {

  private final LinkManager linkManager;
  private final List<UserLink<?>> links;

  /**
   * Instantiates a {@link User} based on the {@link UserLink}s
   * which attaches {@link Account}s on {@link Platform}s to
   * this {@link User}.
   *
   * @param linkManager the {@link LinkManager} which which {@link Rank}s
   *                    have been linked to other {@link Rank}s
   * @param links the {@link UserLink}s which attach {@link Account}s
   *              on {@link Platform}s to this {@link User}
   */
  public User(@NotNull LinkManager linkManager, @NotNull List<UserLink<?>> links) {
    this.links = links;
    this.linkManager = linkManager;
  }

  /**
   * Returns the {@link Account}s which have been linked
   * to the current {@link User} based on the active
   * {@link UserLink}s.
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
   * Adds a {@link UserLink} to the current {@link User}
   * which links an {@link Account} to this {@link User}
   * if the {@link UserLink} is currently active. If it
   * is active, the given {@link Link}s will be issued
   * to the {@link Account}.
   *
   * @param link the {@link UserLink} which should link the
   *             {@link Account} to the {@link User}
   * @param rewards the {@link Reward}s which should be issued
   *                if the link was successful
   * @param <T> the type of the {@link Platform} of the {@link Account}
   * @return true if the {@link UserLink} was active, false
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
  public void updateRanks() {
    // TODO update the ranks
  }

  /**
   * Synchronizes the current names over the {@link Platform}s
   * which have been synced to the {@link User}.
   */
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
