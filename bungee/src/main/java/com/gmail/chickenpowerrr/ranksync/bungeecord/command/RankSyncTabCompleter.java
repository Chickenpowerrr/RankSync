package com.gmail.chickenpowerrr.ranksync.bungeecord.command;

import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * This class is allows autocompletion for the /sync and /unsync command
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public class RankSyncTabCompleter extends
    com.gmail.chickenpowerrr.ranksync.server.command.RankSyncTabCompleter implements Listener {

  /**
   * @param linkHelper the link helper
   */
  public RankSyncTabCompleter(LinkHelper linkHelper) {
    super(linkHelper);
  }

  /**
   * Completes all of the possible platforms
   *
   * @param event the event that triggered the function
   * @return all of the possible platforms
   */
  @EventHandler
  public void onTabComplete(TabCompleteEvent event) {
    switch (event.getCursor().replaceAll(" .+", "").toLowerCase()) {
      case "ranksync":
      case "link":
      case "sync":
      case "synchronize":
      case "syncrank":
      case "synchronizerank":
      case "giverank":
        // Fall through
      case "unsync":
      case "unlink":
      case "unranksync":
      case "unsynchronize":
      case "unsyncrank":
      case "unsynchronizerank":
      case "revertrank":
        event.getSuggestions()
            .addAll(getOptions(event.getCursor().replaceFirst(".+? ", "").split(" ")));
        break;
      default:
        break;
    }
  }
}
