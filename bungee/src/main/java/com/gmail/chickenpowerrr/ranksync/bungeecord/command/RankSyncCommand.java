package com.gmail.chickenpowerrr.ranksync.bungeecord.command;

import com.gmail.chickenpowerrr.ranksync.api.user.User;
import com.gmail.chickenpowerrr.ranksync.bungeecord.user.BungeeUser;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * This class handles any incoming /sync command
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public class RankSyncCommand extends Command {

  private final com.gmail.chickenpowerrr.ranksync.server.command.RankSyncCommand rankSyncCommand;

  /**
   * @param rankSyncPlugin the ranksync plugin
   */
  public RankSyncCommand(RankSyncServerPlugin rankSyncPlugin) {
    super("ranksync", "ranksync.command.ranksync", "link", "sync", "synchronize", "syncrank",
        "synchronizerank", "giverank");
    this.rankSyncCommand = new com.gmail.chickenpowerrr.ranksync.server.command.RankSyncCommand(
        rankSyncPlugin) {
      @Override
      protected boolean isValidUser(User user) {
        return ((BungeeUser) user).getCommandSender() instanceof ProxiedPlayer;
      }
    };
  }

  /**
   * Links a Minecraft account if the player has a valid code
   *
   * @param sender the user that executed the command
   * @param args the given arguments
   */
  @Override
  public void execute(CommandSender sender, String[] args) {
    this.rankSyncCommand.execute(new BungeeUser(sender), "ranksync", args);
  }
}
