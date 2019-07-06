package com.gmail.chickenpowerrr.ranksync.server.link;

import com.gmail.chickenpowerrr.ranksync.server.language.Translation;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import com.gmail.chickenpowerrr.ranksync.api.user.User;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class can be used to check if a player doesn't perform too many requests
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
class RequestLimitCheckMiddleware extends AbstractMiddleware {

  private final RankSyncServerPlugin rankSyncPlugin;
  private final Map<UUID, Long> timeOuts = new HashMap<>();

  /**
   * Starts the cleanup of the previous requests
   *
   * @param linkHelper the helper to create a complete check
   */
  RequestLimitCheckMiddleware(RankSyncServerPlugin rankSyncPlugin, LinkHelper linkHelper) {
    super(linkHelper);
    this.rankSyncPlugin = rankSyncPlugin;
    this.rankSyncPlugin.runTaskTimer(this.timeOuts::clear, 20 * 30, 20 * 30);
  }

  /**
   * Returns if the player doesn't send more than one request per two seconds
   *
   * @param user the player that invokes the middleware
   * @param uuid the UUID of the player that invokes the middleware
   * @param service the service the middleware has been invoked from
   * @param key the key used to link the player
   * @return if the player doesn't send more than one request per two seconds
   */
  @Override
  protected boolean check(User user, UUID uuid, String service, String key) {
    if (this.timeOuts.containsKey(uuid) && this.timeOuts.get(uuid) + 1000 * 2 >= System
        .currentTimeMillis()) {
      user.sendMessage(Translation.RANKSYNC_COMMAND_REQUEST_LIMIT.getTranslation());
      return false;
    } else {
      this.timeOuts.put(uuid, System.currentTimeMillis());
      return true;
    }
  }
}
