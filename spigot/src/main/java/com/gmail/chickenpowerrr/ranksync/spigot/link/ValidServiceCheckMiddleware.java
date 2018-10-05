package com.gmail.chickenpowerrr.ranksync.spigot.link;

import com.gmail.chickenpowerrr.ranksync.api.LinkInfo;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;
import java.util.stream.Collectors;

public class ValidServiceCheckMiddleware extends AbstractMiddleware {

    private final String services;

    ValidServiceCheckMiddleware(LinkHelper linkHelper) {
        super(linkHelper);
        this.services = super.linkHelper.getLinkInfos().stream().sorted().map(LinkInfo::getName).collect(Collectors.joining("/", "<", ">"));
    }

    @Override
    protected boolean check(CommandSender commandSender, UUID uuid, String service, String key) {
        if(super.linkHelper.getLinkInfo(service) == null) {
            commandSender.sendMessage(ChatColor.DARK_RED + "Please use the command as like this: " + ChatColor.RED + "/ranksync " + this.services);
            return false;
        }
        return true;
    }
}
