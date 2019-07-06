package com.gmail.chickenpowerrr.ranksync.spigot.user;

import com.gmail.chickenpowerrr.ranksync.api.user.User;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class wraps a Spigot player
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
@AllArgsConstructor
public class SpigotUser implements User {

  @Getter
  private final CommandSender player;

  /**
   * Sends the given message to the spigot user
   *
   * @param message the message the spigot user should receive
   */
  @Override
  public void sendMessage(String message) {
    this.player.sendMessage(message);
  }

  /**
   * Returns the UUID of the represented spigot user
   */
  @Override
  public UUID getUuid() {
    return ((Player) this.player).getUniqueId();
  }
}
