package com.gmail.chickenpowerrr.ranksync.spigot.command;

import com.gmail.chickenpowerrr.ranksync.api.player.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class RankSyncTabCompleter implements TabCompleter {

    private final List<String> possibilities = JavaPlugin.getPlugin(RankSyncPlugin.class).getLinkHelper().getLinkInfos().stream().map(LinkInfo::getName).map(String::toLowerCase).sorted().collect(Collectors.toList());

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1) {
            return this.possibilities.stream().filter(name -> name.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        return null;
    }
}
