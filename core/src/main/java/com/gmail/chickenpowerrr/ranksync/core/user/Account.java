package com.gmail.chickenpowerrr.ranksync.core.user;

import com.gmail.chickenpowerrr.ranksync.core.link.Platform;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
  private final List<UserLink> links;

  /**
   * Instantiates an {@code Account} based on
   * the unique identifier and the {@code UserLink}s
   * which connect the {@code Account} to {@code User}s.
   *
   * @param identifier the unique identifier of the
   *                   {@code Account} on the {@code Platform}
   * @param links the {@code UserLink}s which connects the
   *              {@code Account} to {@code User}s
   */
  public Account(@NotNull String identifier, @NotNull List<UserLink> links) {
    this.identifier = identifier;
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
   *             the {@code Platorm}
   * @return true if the update was successful, false
   *         otherwise
   */
  @Contract(pure = false)
  public abstract boolean updateName(@NotNull String name);
}
