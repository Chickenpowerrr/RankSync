package com.gmail.chickenpowerrr.ranksync.spigot.command;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
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

public class UnSyncCommandExecutor implements CommandExecutor {

    private final LinkHelper linkHelper = JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper();
    private final String services = JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper().getLinkInfos().stream().sorted().map(LinkInfo::getName).collect(Collectors.joining("/", "<", ">"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length == 1) {
                Bot<?,?> bot = JavaPlugin.getPlugin(RankSyncPlugin.class).getBot(args[0]);

                if(bot != null) {
                    bot.getPlayerFactory().getPlayer(((Player) sender).getUniqueId()).thenAccept(player -> {
                        if(player != null) {
                            if(this.linkHelper.isAllowedToUnlink(sender, ((Player) sender).getUniqueId(), args[0])) {
                                bot.getEffectiveDatabase().setUuid(player.getPersonalId(), null);
                                sender.sendMessage(ChatColor.GREEN + "Your account has been unlinked!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.DARK_RED + "Your account hasn't been linked to: " + ChatColor.RED + args[0]);
                        }
                    });
                } else {
                    sender.sendMessage(ChatColor.RED + args[0] + ChatColor.DARK_RED + " isn't a valid platform, please use one of those: " + ChatColor.RED + services);
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "Please use the command as like this: " + ChatColor.RED + "/unsync " + this.services);
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You should be a player to execute this command");
        }
        return true;
    }
}
