package com.gmail.chickenpowerrr.ranksync.server.link;

import com.gmail.chickenpowerrr.ranksync.server.language.Translation;
import com.gmail.chickenpowerrr.ranksync.api.user.User;
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
   * @param user the player that invokes the middleware
   * @param uuid the UUID of the player that invokes the middleware
   * @param service the service the middleware has been invoked from
   * @param key the key used to link the player
   * @return if the code is valid
   */
  @Override
  protected boolean check(User user, UUID uuid, String service, String key) {
    if (super.linkHelper.isValidAuthenticationKey(key)) {
      return true;
    } else {
      user.sendMessage(Translation.INVALID_CODE.getTranslation());
      return false;
    }
  }
}
