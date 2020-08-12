package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import com.gmail.chickenpowerrr.ranksync.core.reward.Reward;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a link between a {@link User}
 * and an {@link Account}.
 *
 * @param <T> the type of the {@link Platform} of the
 *            {@link Account} which is linked to the
 *            {@link User}
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class UserLink<T extends Platform<T>> {

  private final Account<T> account;
  private final User user;
  private final Date start;
  private Date end;

  /**
   * Instantiates a new link, based on which {@link Account}
   * should be linked to which {@link User} and the lifetime
   * of this link.
   *
   * @param account the {@link Account} which will be linked
   *                to the {@link User}
   * @param user the {@link User} which gets an {@link Account}
   *             linked to it
   * @param start the start time of the link
   * @param end the end time of the link, null if it hasn't
   *            ended yet
   */
  public UserLink(@NotNull Account<T> account, @NotNull User user, @NotNull Date start,
      @Nullable Date end) {
    this.account = account;
    this.user = user;
    this.start = start;
    this.end = end;
  }

  /**
   * Instantiates a new link, based on which {@link Account}
   * should be linked to which {@link User} and the start time
   * of this link. It won't have an end time.
   *
   * @param account the {@link Account} which will be linked
   *                to the {@link User}
   * @param user the {@link User} which gets an {@link Account}
   *             linked to it
   * @param start the start time of the link
   */
  public UserLink(@NotNull Account<T> account, @NotNull User user, @NotNull Date start) {
    this(account, user, start, null);
  }

  /**
   * Instantiates a new link, based on which {@link Account}
   * should be linked to which {@link User}. The start time will
   * be the moment on which the constructor has been called and
   * the end time won't yet have been specified.
   *
   * @param account the {@link Account} which will be linked
   *                to the {@link User}
   * @param user the {@link User} which gets an {@link Account}
   *             linked to it
   */
  public UserLink(@NotNull Account<T> account, @NotNull User user) {
    this(account, user, Date.from(Instant.now()));
  }

  /**
   * Returns true if the current link is active, false
   * if the link has been expired or if it hasn't started
   * just yet.
   */
  @Contract(pure = true)
  public boolean isActive() {
    return (this.end == null || this.end.after(Date.from(Instant.now()))
        && this.start.before(Date.from(Instant.now())));
  }

  /**
   * Unlinks an {@link Account} from a {@link User} if the
   * {@link UserLink} is still valid. If it was still valid,
   * the {@link UserLink} will be deactivated and the
   * {@link Reward}s will be issued to the {@link Account}.
   *
   * @param rewards the {@link Reward}s which the {@link Account}
   *                will receive for a valid unlink
   * @return true if the {@link UserLink} is active, false
   *         otherwise
   */
  @Contract(mutates = "this")
  public boolean unlink(@NotNull Collection<Reward<T>> rewards) {
    if (isActive()) {
      this.end = Date.from(Instant.now());
      rewards.forEach(reward -> reward.apply(this.account));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns the {@link User} which has an {@link Account} attached
   * to it based on the current {@link UserLink}.
   */
  @Contract(pure = true)
  @NotNull
  public User getUser() {
    return this.user;
  }

  /**
   * Returns the {@link Account} which has been attached to the
   * {@link User} by the current {@link UserLink}.
   */
  @Contract(pure = true)
  @NotNull
  public Account<T> getAccount() {
    return this.account;
  }
}
