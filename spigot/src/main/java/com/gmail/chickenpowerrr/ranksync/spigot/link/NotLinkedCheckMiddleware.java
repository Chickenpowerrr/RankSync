package com.gmail.chickenpowerrr.ranksync.spigot.link;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

class NotLinkedCheckMiddleware extends AbstractMiddleware {

    NotLinkedCheckMiddleware(LinkHelper linkHelper) {
        super(linkHelper);
    }

    @Override
    protected boolean check(CommandSender commandSender, UUID uuid, String service, String key) {
        if(super.linkHelper.getLink(service, uuid) == null) {
            return true;
        } else {
            commandSender.sendMessage(ChatColor.DARK_RED + "Your account has already been linked to " + ChatColor.RED + service);
            return false;
        }
    }
}
