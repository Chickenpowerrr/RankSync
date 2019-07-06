package com.gmail.chickenpowerrr.ranksync.server.link;

import com.gmail.chickenpowerrr.ranksync.api.user.User;
import java.util.UUID;

/**
 * This class can be used add a check to an action
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
abstract class AbstractMiddleware {

  private AbstractMiddleware next;
  protected final LinkHelper linkHelper;

  /**
   * @param linkHelper the helper to create a complete check
   */
  public AbstractMiddleware(LinkHelper linkHelper) {
    this.linkHelper = linkHelper;
  }

  /**
   * Sets the next part of the check
   *
   * @param next the next in the series
   * @return the next part
   */
  public AbstractMiddleware setNext(AbstractMiddleware next) {
    this.next = next;
    return next;
  }

  /**
   * Returns if the complete check is true
   *
   * @param user the player that invokes the middleware
   * @param uuid the UUID of the player that invokes the middleware
   * @param service the service the middleware has been invoked from
   * @param key the key used to link the player
   * @return if the complete check is true
   */
  final boolean allowed(User user, UUID uuid, String service, String key) {
    if (next == null) {
      return check(user, uuid, service, key);
    } else {
      return check(user, uuid, service, key) && this.next
          .allowed(user, uuid, service, key);
    }
  }

  /**
   * Returns if this middleware is true
   *
   * @param user the player that invokes the middleware
   * @param uuid the UUID of the player that invokes the middleware
   * @param service the service the middleware has been invoked from
   * @param key the key used to link the player
   * @return if this check is true
   */
  protected abstract boolean check(User user, UUID uuid, String service,
      String key);
}
