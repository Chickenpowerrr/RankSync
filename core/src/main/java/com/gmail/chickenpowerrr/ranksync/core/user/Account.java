package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.rank.RankLink;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents an {@code Account} on
 * a {@code Platform}.
 *
 * @param <T> the type of the {@code Platform} o
 *            the {@code Account}
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public abstract class Account<T extends Platform<T>> {

  private final String identifier;
  private final Platform<T> platform;
  private final List<UserLink<T>> links;

  /**
   * Instantiates an {@code Account} based on
   * the unique identifier and the {@code UserLink}s
   * which connect the {@code Account} to {@code User}s.
   *
   * @param identifier the unique identifier of the
   *                   {@code Account} on the {@code Platform}
   * @param platform the {@code Platform} on which this
   *                 {@code Account} is synced
   * @param links the {@code UserLink}s which connects the
   *              {@code Account} to {@code User}s
   */
  public Account(@NotNull String identifier, @NotNull Platform<T> platform,
      @NotNull List<UserLink<T>> links) {
    this.identifier = identifier;
    this.platform = platform;
    this.links = links;
  }

  /**
   * Returns the unique identifier of the {@code Account}
   * on its {@code Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Returns the name of the {@code Account} on the
   * {@code Platform}.
   */
  @Contract(pure = true)
  @NotNull
  public abstract String getName();

  /**
   * Updates the name of the {@code Account} on the
   * {@code Platform}, if possible.
   *
   * @param name the name of the {@code Account} on
   *             the {@code Platform}
   * @return true if the update was successful, false
   *         otherwise
   */
  @Contract(pure = false)
  public abstract boolean updateName(@NotNull String name);

  /**
   * Format the name of an {@code Account} based on the
   * formats found in the {@code Platform} and the
   * {@code Rank}s.
   *
   * @return the formatted name of the {@code Account}
   */
  @NotNull
  public CompletableFuture<String> formatName() {
    Optional<User> optionalUser = getUser();
    if (!optionalUser.isPresent()) {
      return CompletableFuture.completedFuture(
          this.platform.getBaseNameFormat().replace("%name%", getName()));
    }

    User user = optionalUser.get();
    Optional<Account<?>> optionalAccount = user.getAccounts().stream()
        .filter(account -> !account.equals(this)).sorted().findFirst();

    if (!optionalAccount.isPresent()) {
      return CompletableFuture.completedFuture(
          this.platform.getBaseNameFormat().replace("%name%", getName()));
    }

    Account<?> account = optionalAccount.get();

    String name = account.getName();
    return getPlatform().getRanks(this).thenApply(ranks -> {
      Optional<Rank<T>> rank = ranks.stream().sorted().findAny();
      if (!rank.isPresent()) {
        return getName();
      } else {
        String format = getFormat(user, rank.get());
        return format != null ? format.replace("%name%", name) : getName();
      }
    });
  }

  /**
   * Returns the {@code Platform} on which this {@code Account}
   * is located.
   */
  @NotNull
  public Platform<T> getPlatform() {
    return this.platform;
  }

  /**
   * Sets the {@link Rank}s of the {@link Account} on the current
   * {@link Platform} to the given {@link Rank}s.
   *
   * @param ranks the new {@link Rank}s of the {@link Account} on
   *              the {@link Platform}
   */
  public abstract void updateRanks(@NotNull List<Rank<T>> ranks);

  /**
   * Returns the {@link User} linked to this {@link Account}.
   */
  @NotNull
  public Optional<User> getUser() {
    return this.links.stream().filter(UserLink::isActive).map(UserLink::getUser).findAny();
  }

  /**
   * Returns the format which will be used to update names for a certain {@link Account}.
   *
   * @param user the {@link User} of the {@link Account}
   * @param platformRank the primary {@link Rank} of the {@link Account}
   * @return the format which will be used to update names for a certain {@link Account}
   */
  @Nullable
  private String getFormat(@NotNull User user, @NotNull Rank<T> platformRank) {
    Collection<RankLink> rankLinks = user.getLinks().get(platformRank);

    if (rankLinks == null || rankLinks.isEmpty()) {
      return null;
    }

    RankLink rankLink = rankLinks.stream().sorted().findFirst().get();
    return rankLink.getNameFormat();
  }
}
