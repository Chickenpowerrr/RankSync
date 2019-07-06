package com.gmail.chickenpowerrr.ranksync.server.command;

import com.gmail.chickenpowerrr.ranksync.api.player.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is allows autocompletion for the /sync and /unsync command
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class RankSyncTabCompleter {

  private final List<String> possibilities;

  /**
   * Saves all possible services
   *
   * @param linkHelper the link helper which is aware of all possible services
   */
  public RankSyncTabCompleter(LinkHelper linkHelper) {
    this.possibilities = linkHelper.getLinkInfos().stream().map(LinkInfo::getName)
        .map(String::toLowerCase).sorted().collect(Collectors.toList());
  }

  /**
   * Returns all of the possible platforms
   *
   * @param args the arguments the player entered so far
   * @return all of the possible platforms
   */
  public List<String> getOptions(String[] args) {
    if (args.length == 1) {
      return this.possibilities.stream().filter(name -> name.startsWith(args[0].toLowerCase()))
          .collect(Collectors.toList());
    }
    return null;
  }
}
