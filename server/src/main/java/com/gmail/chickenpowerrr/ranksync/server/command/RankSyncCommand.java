package com.gmail.chickenpowerrr.ranksync.server.command;

import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkedEvent;
import com.gmail.chickenpowerrr.ranksync.api.player.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.api.user.User;
import com.gmail.chickenpowerrr.ranksync.server.language.Translation;
import com.gmail.chickenpowerrr.ranksync.server.link.LinkHelper;
import com.gmail.chickenpowerrr.ranksync.server.plugin.RankSyncServerPlugin;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This class handles any incoming /sync command
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public abstract class RankSyncCommand extends AbstractCommand {

  private final RankSyncServerPlugin rankSyncPlugin;
  private final LinkHelper linkHelper;
  private final String services;

  /**
   * Initializes the command with the relevant information
   *
   * @param rankSyncPlugin the ranksync plugin
   */
  public RankSyncCommand(RankSyncServerPlugin rankSyncPlugin) {
    super("ranksync", "Link your account to another service", "ranksync.command.ranksync", new ArrayList<String>() {{
      add("link");
      add("sync");
      add("synchronize");
      add("syncrank");
      add("synchronizerank");
      add("giverank");
    }});
    this.rankSyncPlugin = rankSyncPlugin;
    this.linkHelper = rankSyncPlugin.getLinkHelper();
    this.services = this.linkHelper.getLinkInfos().stream().sorted().map(LinkInfo::getName)
        .collect(Collectors.joining("/", "<", ">"));
  }

  /**
   * Links a Minecraft account if the player has a valid code
   *
   * @param user the player who whats to sync its account
   * @param label the command label the player used
   * @param args the arguments the player gave with the execution of the command
   */
  @Override
  public void execute(User user, String label, String[] args) {
    if (isValidUser(user)) {
      LinkInfo linkInfo = args.length > 0 ? this.linkHelper.getLinkInfo(args[0]) : null;
      switch (args.length) {
        case 2:
          Bot<?, ?> bot = this.rankSyncPlugin.getBot(args[0]);
          if (bot != null && bot.isEnabled()) {
            bot.getPlayerFactory().getPlayer(user.getUuid()).thenAccept(player -> {
              if (player == null) {
                if (this.linkHelper
                    .isAllowedToLink(user, user.getUuid(),
                        args[0], args[1])) {
                  this.linkHelper.link(user.getUuid(), args[0], args[1]);
                  user.sendMessage(Translation.RANKSYNC_COMMAND_LINKED.getTranslation());
                  bot.getPlayerFactory().getPlayer(user.getUuid())
                      .thenAccept(linkedPlayer -> {
                        RankSyncApi.getApi().execute(new PlayerLinkedEvent(linkedPlayer));
                      });
                }
              } else {
                user.sendMessage(
                    Translation.RANKSYNC_COMMAND_ALREADY_LINKED.getTranslation("service", args[0]));
              }
            });
          } else {
            user.sendMessage(
                Translation.RANKSYNC_COMMAND_USAGE
                    .getTranslation("command", label.toLowerCase(), "services", this.services));
          }
          break;
        case 1:
          if (linkInfo == null) {
            user.sendMessage(
                Translation.RANKSYNC_COMMAND_USAGE
                    .getTranslation("command", label.toLowerCase(), "services", this.services));
          } else {
            user.sendMessage(Translation.RANKSYNC_COMMAND_GET_CODE
                .getTranslation("explanation", linkInfo.getLinkExplanation(), "service",
                    linkInfo.getName(), "SERVICE", linkInfo.getName().toUpperCase()));
          }
          break;
        default:
          user.sendMessage(
              Translation.RANKSYNC_COMMAND_USAGE
                  .getTranslation("command", label.toLowerCase(), "services", this.services));
          break;
      }
    } else {
      user.sendMessage(Translation.COMMAND_PLAYERONLY.getTranslation());
    }
  }

  /**
   * Returns if the user is allowed to use this command
   *
   * @param user the executing user
   * @return if the user is allowed to use this command
   */
  @Override
  protected boolean isValidUser(User user) {
    return false;
  }
}
