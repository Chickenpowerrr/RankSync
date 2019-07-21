package com.gmail.chickenpowerrr.ranksync.bungeecord.command;

import com.gmail.chickenpowerrr.ranksync.api.user.User;
import com.gmail.chickenpowerrr.ranksync.bungeecord.user.BungeeUser;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


/**
 * This class handles any incoming /unsync command
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public class UnSyncCommand extends Command {

  private final com.gmail.chickenpowerrr.ranksync.server.command.UnSyncCommand unSyncCommand;

  /**
   * @param rankSyncPlugin the ranksync plugin
   */
  public UnSyncCommand(RankSyncServerPlugin rankSyncPlugin) {
    super("unsync", "ranksync.command.unsync", "unlink", "unranksync", "unsynchronize",
        "unsyncrank", "unsynchronizerank", "revertrank");
    this.unSyncCommand = new com.gmail.chickenpowerrr.ranksync.server.command.UnSyncCommand(
        rankSyncPlugin) {
      @Override
      protected boolean isValidUser(User user) {
        return ((BungeeUser) user).getCommandSender() instanceof ProxiedPlayer;
      }
    };
  }

  /**
   * Unlinks a Minecraft account if the account has been linked
   *
   * @param sender the player who whats to sync its account
   * @param args the arguments the player gave with the execution of the command
   */
  @Override
  public void execute(CommandSender sender, String[] args) {
    this.unSyncCommand.execute(new BungeeUser(sender), "unsync", args);
  }
}
