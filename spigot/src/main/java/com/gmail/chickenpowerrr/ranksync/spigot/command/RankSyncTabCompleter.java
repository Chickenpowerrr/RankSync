package com.gmail.chickenpowerrr.ranksync.spigot.command;

import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is allows autocompletion for the /sync and /unsync command
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public class RankSyncTabCompleter extends
    com.gmail.chickenpowerrr.ranksync.server.command.RankSyncTabCompleter implements TabCompleter {

  /**
   * @param linkHelper the link helper
   */
  public RankSyncTabCompleter(LinkHelper linkHelper) {
    super(linkHelper);
  }

  /**
   * Returns all of the possible platforms
   *
   * @param sender the user who wants to tab complete
   * @param command the command the user entered so far
   * @param alias the alias used
   * @param args the args typed so far
   * @return all of the possible platforms
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
      @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    return getOptions(args);
  }
}
