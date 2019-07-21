package com.gmail.chickenpowerrr.ranksync.api.user;

import java.util.UUID;

/**
 * This class represents a RankSync user
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public interface User {

  /**
   * Sends a given message to the user
   *
   * @param message the message the user should receive
   */
  void sendMessage(String message);

  /**
   * Returns the UUID of the represented user
   */
  UUID getUuid();
}
