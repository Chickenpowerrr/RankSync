package com.gmail.chickenpowerrr.ranksync.spigot.command;

import com.gmail.chickenpowerrr.ranksync.api.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import com.gmail.chickenpowerrr.ranksync.spigot.link.LinkHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class RankSyncCommandExecutor implements CommandExecutor {

    private final LinkHelper linkHelper = JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper();
    private final String services = JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper().getLinkInfos().stream().sorted().map(LinkInfo::getName).collect(Collectors.joining("/", "<", ">"));

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            LinkInfo linkInfo = args.length > 0 ? this.linkHelper.getLinkInfo(args[0]) : null;
            switch(args.length) {
                case 2:
                    JavaPlugin.getPlugin(RankSyncPlugin.class).getBot(args[0]).getPlayerFactory().getPlayer(((Player) sender).getUniqueId()).thenAccept(player -> {
                        if(player == null) {
                            if(this.linkHelper.isAllowedToLink(sender, ((Player) sender).getUniqueId(), args[0], args[1])) {
                                this.linkHelper.link(((Player) sender).getUniqueId(), args[0], args[1]);
                                sender.sendMessage(ChatColor.DARK_RED + "Your account has been linked!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.DARK_RED + "Your account has already been linked to " + ChatColor.RED + args[0]);
                        }
                    });
                    break;
                case 1:
                    if(linkInfo == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Please use the command as like this: " + ChatColor.RED + "/ranksync " + this.services);
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "You'll have to get a code by " + ChatColor.RED + linkInfo.getLinkExplanation() + ChatColor.DARK_RED + " and link your account by typing " + ChatColor.RED + "/ranksync " + linkInfo.getName() + " THE_CODE_" + linkInfo.getName().toUpperCase() + "_GAVE_YOU" + ChatColor.DARK_RED + " into the Minecraft server");
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.DARK_RED + "Please use the command as like this: " + ChatColor.RED + "/ranksync " + this.services);
                    break;
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You should be a player to execute this command");
        }
        return true;
    }
}
