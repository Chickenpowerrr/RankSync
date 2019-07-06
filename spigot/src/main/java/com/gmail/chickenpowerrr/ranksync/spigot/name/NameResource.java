package com.gmail.chickenpowerrr.ranksync.spigot.name;

import java.util.UUID;
import org.bukkit.Bukkit;

/**
 * This class looks up the last known name of a user by their UUID
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public class NameResource implements com.gmail.chickenpowerrr.ranksync.api.name.NameResource {

  /**
   * Returns the name of an user
   *
   * @param uuid the UUID on the platform the name should be retrieved from
   */
  @Override
  public String getName(UUID uuid) {
    return Bukkit.getOfflinePlayer(uuid).getName();
  }
}
