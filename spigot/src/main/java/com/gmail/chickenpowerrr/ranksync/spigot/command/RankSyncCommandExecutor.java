package com.gmail.chickenpowerrr.ranksync.spigot.command;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.player.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.event.PlayerLinkedEvent;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import com.gmail.chickenpowerrr.ranksync.spigot.language.Translation;
import com.gmail.chickenpowerrr.ranksync.spigot.link.LinkHelper;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RankSyncCommandExecutor implements CommandExecutor {

    private final RankSyncPlugin rankSyncPlugin = JavaPlugin.getPlugin(RankSyncPlugin.class);
    private final LinkHelper linkHelper = JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper();
    private final String services = JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper().getLinkInfos().stream().sorted().map(LinkInfo::getName).collect(Collectors.joining("/", "<", ">"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            LinkInfo linkInfo = args.length > 0 ? this.linkHelper.getLinkInfo(args[0]) : null;
            switch(args.length) {
                case 2:
                    Bot<?,?> bot = this.rankSyncPlugin.getBot(args[0]);
                    if(bot != null) {
                        bot.getPlayerFactory().getPlayer(((Player) sender).getUniqueId()).thenAccept(player -> {
                            if(player == null) {
                                if(this.linkHelper.isAllowedToLink(sender, ((Player) sender).getUniqueId(), args[0], args[1])) {
                                    this.linkHelper.link(((Player) sender).getUniqueId(), args[0], args[1]);
                                    sender.sendMessage(Translation.RANKSYNC_COMMAND_LINKED.getTranslation());
                                    bot.getPlayerFactory().getPlayer(((Player) sender).getUniqueId()).thenAccept(linkedPlayer -> {
                                        RankSyncApi.getApi().execute(new PlayerLinkedEvent(linkedPlayer));
                                    });
                                }
                            } else {
                                sender.sendMessage(Translation.RANKSYNC_COMMAND_ALREADY_LINKED.getTranslation("service", args[0]));
                            }
                        });
                    } else {
                        sender.sendMessage(Translation.RANKSYNC_COMMAND_USAGE.getTranslation("services", this.services));
                    }
                    break;
                case 1:
                    if(linkInfo == null) {
                        sender.sendMessage(Translation.RANKSYNC_COMMAND_USAGE.getTranslation("services", this.services));
                    } else {
                        sender.sendMessage(Translation.RANKSYNC_COMMAND_GET_CODE.getTranslation("explanation", linkInfo.getLinkExplanation(), "service", linkInfo.getName(), "SERVICE", linkInfo.getName().toUpperCase()));
                    }
                    break;
                default:
                    sender.sendMessage(Translation.RANKSYNC_COMMAND_USAGE.getTranslation("services", this.services));
                    break;
            }
        } else {
            sender.sendMessage(Translation.COMMAND_PLAYERONLY.getTranslation());
        }
        return true;
    }
}
