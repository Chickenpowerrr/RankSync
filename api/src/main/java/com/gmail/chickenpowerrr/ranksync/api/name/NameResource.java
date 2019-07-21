package com.gmail.chickenpowerrr.ranksync.api.name;

import java.util.UUID;

/**
 * This interface manages the name of user
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public interface NameResource {

  /**
   * Returns the name of an user
   *
   * @param uuid the UUID on the platform the name should be retrieved from
   */
  String getName(UUID uuid);
}
