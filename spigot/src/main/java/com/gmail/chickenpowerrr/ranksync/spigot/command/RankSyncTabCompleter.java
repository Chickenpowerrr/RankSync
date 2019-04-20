package com.gmail.chickenpowerrr.ranksync.spigot.command;

import com.gmail.chickenpowerrr.ranksync.api.player.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is allows autocompletion for the /sync and /unsync command
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class RankSyncTabCompleter implements TabCompleter {

  private final List<String> possibilities = JavaPlugin.getPlugin(RankSyncPlugin.class)
      .getLinkHelper().getLinkInfos().stream().map(LinkInfo::getName).map(String::toLowerCase)
      .sorted().collect(Collectors.toList());

  /**
   * Returns all of the possible platforms
   *
   * @param sender the player that wants to use the command
   * @param command the concept of the command the player is going to execute
   * @param alias the alias the player uses to execute the command
   * @param args the arguments the player entered so far
   * @return all of the possible platforms
   */
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias,
      String[] args) {
    if (args.length == 1) {
      return this.possibilities.stream().filter(name -> name.startsWith(args[0].toLowerCase()))
          .collect(Collectors.toList());
    }
    return null;
  }
}
