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
 * This class represents a link between a {@code User}
 * and an {@code Account}.
 *
 * @param <T> the type of the {@code Platform} of the
 *            {@code Account} which is linked to the
 *            {@code User}
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
   * Instantiates a new link, based on which {@code Account}
   * should be linked to which {@code User} and the lifetime
   * of this link.
   *
   * @param account the {@code Account} which will be linked
   *                to the {@code User}
   * @param user the {@code User} which gets an {@code Account}
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
   * Instantiates a new link, based on which {@code Account}
   * should be linked to which {@code User} and the start time
   * of this link. It won't have an end time.
   *
   * @param account the {@code Account} which will be linked
   *                to the {@code User}
   * @param user the {@code User} which gets an {@code Account}
   *             linked to it
   * @param start the start time of the link
   */
  public UserLink(@NotNull Account<T> account, @NotNull User user, @NotNull Date start) {
    this(account, user, start, null);
  }

  /**
   * Instantiates a new link, based on which {@code Account}
   * should be linked to which {@code User}. The start time will
   * be the moment on which the constructor has been called and
   * the end time won't yet have been specified.
   *
   * @param account the {@code Account} which will be linked
   *                to the {@code User}
   * @param user the {@code User} which gets an {@code Account}
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
   * Unlinks an {@code Account} from a {@code User} if the
   * {@code UserLink} is still valid. If it was still valid,
   * the {@code UserLink} will be deactivated and the
   * {@code Reward}s will be issued to the {@code Account}.
   *
   * @param rewards the {@code Reward}s which the {@code Account}
   *                will receive for a valid unlink
   * @return true if the {@code UserLink} is active, false
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
   * Returns the {@code User} which has an {@code Account} attached
   * to it based on the current {@code UserLink}.
   */
  @Contract(pure = true)
  @NotNull
  public User getUser() {
    return this.user;
  }

  /**
   * Returns the {@code Account} which has been attached to the
   * {@code User} by the current {@code UserLink}.
   */
  @Contract(pure = true)
  @NotNull
  public Account<T> getAccount() {
    return this.account;
  }
}
