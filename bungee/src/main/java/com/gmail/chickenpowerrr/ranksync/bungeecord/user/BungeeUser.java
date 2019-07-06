package com.gmail.chickenpowerrr.ranksync.bungeecord.user;

import com.gmail.chickenpowerrr.ranksync.api.user.User;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * This class wraps a bungee player
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
@AllArgsConstructor
public class BungeeUser implements User {

  @Getter
  private final CommandSender commandSender;

  /**
   * Sends the given message to the bungee user
   *
   * @param message the message the bungee user should receive
   */
  @Override
  public void sendMessage(String message) {
    this.commandSender.sendMessage(TextComponent.fromLegacyText(message));
  }

  /**
   * Returns the UUID of the represented bungee user
   */
  @Override
  public UUID getUuid() {
    return ((ProxiedPlayer) this.commandSender).getUniqueId();
  }
}
