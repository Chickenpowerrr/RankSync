package com.gmail.chickenpowerrr.ranksync.core.link;

import com.gmail.chickenpowerrr.ranksync.core.rank.Rank;
import com.gmail.chickenpowerrr.ranksync.core.user.User;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class manages all {@code Link}s and syncs for
 * the registered {@code User}s.
 *
 * @author Mark van Wijk
 * @since 2.0.0
 */
public class LinkManager {

  private final boolean sendWarnings;
  private final int updateInterval;
  private final boolean syncNames;
  private final Map<Platform<?>, Map<String, User>> cachedUsers;

  /**
   * Instantiates a new manager, based on  if it should send
   * warnings when it does not have enough permissions to perform a
   * certain action, the interval in which it should regularly
   * update the {@code Rank}s and if the name sync feature has
   * been enabled.
   *
   * @param sendWarnings true if the program should print warnings
   *                     when it does not have sufficient permissions
   *                     to perform a certain action
   * @param updateInterval the interval in seconds in which the program
   *                       will automatically check if all assets have
   *                       been synced correctly, -1 if this feature
   *                       should be disabled
   * @param syncNames true if the name sync feature is enabled,
   */
  public LinkManager(boolean sendWarnings, int updateInterval, boolean syncNames) {
    this.sendWarnings = sendWarnings;
    this.updateInterval = updateInterval;
    this.syncNames = syncNames;
    this.cachedUsers = new ConcurrentHashMap<>();
  }

  /**
   * Returns true if the program should print warnings when it
   * does not have sufficient permissions to perform a certain action.
   */
  @Contract(pure = true)
  public boolean shouldSendWarnings() {
    return this.sendWarnings;
  }

  /**
   * Returns true if the name sync feature is enabled, false otherwise.
   */
  @Contract(pure = true)
  public boolean shouldSyncNames() {
    return this.syncNames;
  }

  /**
   * Caches a {@link User} such that the {@link #sync()}
   * method will update the {@link Rank}s for the given
   * {@link User}.
   *
   * @param user the {@link User} whose {@link Rank}s should
   *             be synchronized
   */
  @Contract(pure = false)
  public void cacheUser(@NotNull User user) {
    user.getAccounts().forEach(account -> {
      this.cachedUsers.computeIfAbsent(account.getPlatform(), p -> new ConcurrentHashMap<>());
      this.cachedUsers.get(account.getPlatform()).put(account.getIdentifier(), user);
    });
  }

  /**
   * Un caches a {@link User} if the have been cached through
   * a {@link #cacheUser(User)} call, based on only one of their
   * unique identifiers, to make sure the {@link #sync()} method
   * won't update them anymore.
   *
   * @param platform the {@link Platform} on which the provided
   *                 identifier is valid
   * @param identifier the unique identifier of the {@link User}
   *                   on the given {@link Platform}
   */
  @Contract(pure = false)
  public void unCacheUser(@NotNull Platform<?> platform, @NotNull String identifier) {
    Map<String, User> users = this.cachedUsers.get(platform);
    if (users != null) {
      User user = users.get(identifier);
      if (user != null) {
        user.getAccounts().forEach(account -> {
          Map<String, User> targetUsers = this.cachedUsers.get(account.getPlatform());
          if (targetUsers != null) {
            targetUsers.remove(account.getIdentifier());
          }
        });
      }
    }
  }

  /**
   * Updates all {@link User}s in the cache, which have been added
   * through a {@link #cacheUser(User)} without being deleted by a
   * {@link #unCacheUser(Platform, String)} before the execution
   * of this method.
   */
  @Contract(pure = false)
  public void sync() {

  }

  /**
   * Returns all {@link User}s who have been cached through a
   * {@link #cacheUser(User)} call, without being removed through
   * a {@link #unCacheUser(Platform, String)} call.
   */
  @NotNull
  public Collection<User> getCachedUsers() {
    return this.cachedUsers.values().stream()
        .map(Map::values)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }
}
