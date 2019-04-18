package com.gmail.chickenpowerrr.ranksync.spigot.link;

import com.gmail.chickenpowerrr.ranksync.spigot.language.Translation;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * This class check if the code is valid
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
class ValidIdCheckMiddleware extends AbstractMiddleware {

  /**
   * @param linkHelper the helper to create a complete check
   */
  ValidIdCheckMiddleware(LinkHelper linkHelper) {
    super(linkHelper);
  }

  /**
   * Returns if the code is valid
   *
   * @param commandSender the player that invokes the middleware
   * @param uuid the UUID of the player that invokes the middleware
   * @param service the service the middleware has been invoked from
   * @param key the key used to link the player
   * @return if the code is valid
   */
  @Override
  protected boolean check(CommandSender commandSender, UUID uuid, String service, String key) {
    if (super.linkHelper.isValidAuthenticationKey(key)) {
      return true;
    } else {
      commandSender.sendMessage(Translation.INVALID_CODE.getTranslation());
      return false;
    }
  }
}
