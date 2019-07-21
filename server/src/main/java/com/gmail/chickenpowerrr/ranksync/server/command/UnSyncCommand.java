package com.gmail.chickenpowerrr.ranksync.server.command;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.player.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.api.user.User;
import com.gmail.chickenpowerrr.ranksync.server.language.Translation;
import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This class handles any incoming /unsync command
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public abstract class UnSyncCommand extends AbstractCommand {

  private final RankSyncServerPlugin rankSyncPlugin;
  private final LinkHelper linkHelper;
  private final String services;

  /**
   * Initializes the command with the relevant information
   *
   * @param rankSyncPlugin the ranksync plugin
   */
  public UnSyncCommand(RankSyncServerPlugin rankSyncPlugin) {
    super("unsync", "Unlink your account from another service", "ranksync.command.unsync",
        new ArrayList<String>() {{
          add("unlink");
          add("unranksync");
          add("unsynchronize");
          add("unsyncrank");
          add("unsynchronizerank");
          add("revertrank");
        }});
    this.rankSyncPlugin = rankSyncPlugin;
    this.linkHelper = rankSyncPlugin.getLinkHelper();
    this.services = this.linkHelper.getLinkInfos().stream().sorted().map(LinkInfo::getName)
        .collect(Collectors.joining("/", "<", ">"));
  }

  /**
   * Unlinks a Minecraft account if the account has been linked
   *
   * @param user the player who whats to sync its account
   * @param label the command label the player used
   * @param args the arguments the player gave with the execution of the command
   */
  @Override
  public void execute(User user, String label, String[] args) {
    if (isValidUser(user)) {
      if (args.length == 1) {
        Bot<?, ?> bot = this.rankSyncPlugin.getBot(args[0]);

        if (bot != null) {
          bot.getPlayerFactory().getPlayer(user.getUuid()).thenAccept(player -> {
            if (player != null) {
              if (this.linkHelper.isAllowedToUnlink(user, user.getUuid(), args[0])) {
                bot.getEffectiveDatabase().setUuid(player.getPersonalId(), null);
                user.sendMessage(Translation.UNSYNC_COMMAND_UNLINKED.getTranslation());
              }
            } else {
              user.sendMessage(
                  Translation.UNSYNC_COMMAND_NOT_LINKED.getTranslation("service", args[0]));
            }
          });
        } else {
          user.sendMessage(Translation.UNSYNC_COMMAND_INVALID_SERVICE
              .getTranslation("service", args[0], "services", services));
        }
      } else {
        user.sendMessage(
            Translation.UNSYNC_COMMAND_USAGE
                .getTranslation("command", label.toLowerCase(), "services", this.services));
      }
    } else {
      user.sendMessage(Translation.COMMAND_PLAYERONLY.getTranslation());
    }
  }
}
