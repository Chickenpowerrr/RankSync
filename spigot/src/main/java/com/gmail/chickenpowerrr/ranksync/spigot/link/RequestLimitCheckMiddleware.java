package com.gmail.chickenpowerrr.ranksync.spigot.link;

import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import com.gmail.chickenpowerrr.ranksync.spigot.language.Translation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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

  private final RankSyncPlugin rankSyncPlugin = JavaPlugin.getPlugin(RankSyncPlugin.class);
  private final Map<UUID, Long> timeOuts = new HashMap<>();

  /**
   * Starts the cleanup of the previous requests
   *
   * @param linkHelper the helper to create a complete check
   */
  RequestLimitCheckMiddleware(LinkHelper linkHelper) {
    super(linkHelper);
    Bukkit.getScheduler().runTaskTimer(this.rankSyncPlugin, this.timeOuts::clear, 20 * 30, 20 * 30);
  }

  /**
   * Returns if the player doesn't send more than one request per two seconds
   *
   * @param commandSender the player that invokes the middleware
   * @param uuid the UUID of the player that invokes the middleware
   * @param service the service the middleware has been invoked from
   * @param key the key used to link the player
   * @return if the player doesn't send more than one request per two seconds
   */
  @Override
  protected boolean check(CommandSender commandSender, UUID uuid, String service, String key) {
    if (this.timeOuts.containsKey(uuid) && this.timeOuts.get(uuid) + 1000 * 2 >= System
        .currentTimeMillis()) {
      commandSender.sendMessage(Translation.RANKSYNC_COMMAND_REQUEST_LIMIT.getTranslation());
      return false;
    } else {
      this.timeOuts.put(uuid, System.currentTimeMillis());
      return true;
    }
  }
}
