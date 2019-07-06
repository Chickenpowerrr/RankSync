package com.gmail.chickenpowerrr.ranksync.spigot.command;

import com.gmail.chickenpowerrr.ranksync.api.user.User;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import com.gmail.chickenpowerrr.ranksync.spigot.user.SpigotUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class handles any incoming /unsync command
 *
 * @author Chickenpowerrr
 * @since 1.3.0
 */
public class UnSyncCommand extends
    com.gmail.chickenpowerrr.ranksync.server.command.UnSyncCommand implements CommandExecutor {

  /**
   * @param rankSyncPlugin the ranksync plugin
   */
  public UnSyncCommand(RankSyncServerPlugin rankSyncPlugin) {
    super(rankSyncPlugin);
  }

  /**
   * Unlinks a Minecraft account if the account has been linked
   *
   * @param sender the player who whats to sync its account
   * @param command the executed command
   * @param label the command label the player used
   * @param args the arguments the player gave with the execution of the command
   * @return true
   */
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    execute(new SpigotUser(sender), label, args);
    return true;
  }

  /**
   * Returns if the user is allowed to use this command
   *
   * @param user the executing user
   * @return if the user is allowed to use this command
   */
  @Override
  protected boolean isValidUser(User user) {
    return ((SpigotUser) user).getPlayer() instanceof Player;
  }
}
