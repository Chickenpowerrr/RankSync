package com.gmail.chickenpowerrr.ranksync.spigot.command;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.player.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import com.gmail.chickenpowerrr.ranksync.spigot.language.Translation;
import com.gmail.chickenpowerrr.ranksync.spigot.link.LinkHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

/**
 * This class handles any incoming /unsync command
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public class UnSyncCommandExecutor implements CommandExecutor {

  private final RankSyncPlugin rankSyncPlugin = JavaPlugin.getPlugin(RankSyncPlugin.class);
  private final LinkHelper linkHelper = JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper();
  private final String services = JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper()
      .getLinkInfos().stream().sorted().map(LinkInfo::getName)
      .collect(Collectors.joining("/", "<", ">"));

  /**
   * Unlinks a Minecraft account if the account has been linked
   *
   * @param sender the player who whats to sync its account
   * @param command the command the player executed
   * @param label the command label the player used
   * @param args the arguments the player gave with the execution of the command
   * @return true
   */
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      if (args.length == 1) {
        Bot<?, ?> bot = this.rankSyncPlugin.getBot(args[0]);

        if (bot != null) {
          bot.getPlayerFactory().getPlayer(((Player) sender).getUniqueId()).thenAccept(player -> {
            if (player != null) {
              if (this.linkHelper
                  .isAllowedToUnlink(sender, ((Player) sender).getUniqueId(), args[0])) {
                bot.getEffectiveDatabase().setUuid(player.getPersonalId(), null);
                sender.sendMessage(Translation.UNSYNC_COMMAND_UNLINKED.getTranslation());
              }
            } else {
              sender.sendMessage(
                  Translation.UNSYNC_COMMAND_NOT_LINKED.getTranslation("service", args[0]));
            }
          });
        } else {
          sender.sendMessage(Translation.UNSYNC_COMMAND_INVALID_SERVICE
              .getTranslation("service", args[0], "services", services));
        }
      } else {
        sender.sendMessage(
            Translation.UNSYNC_COMMAND_USAGE.getTranslation("services", this.services));
      }
    } else {
      sender.sendMessage(Translation.COMMAND_PLAYERONLY.getTranslation());
    }
    return true;
  }
}
