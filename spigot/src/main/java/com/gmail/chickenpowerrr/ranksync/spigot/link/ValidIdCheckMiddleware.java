package com.gmail.chickenpowerrr.ranksync.spigot.link;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

class ValidIdCheckMiddleware extends AbstractMiddleware {

    ValidIdCheckMiddleware(LinkHelper linkHelper) {
        super(linkHelper);
    }

    @Override
    protected boolean check(CommandSender commandSender, UUID uuid, String service, String key) {
        if(super.linkHelper.isValidAuthenticationKey(key)) {
            return true;
        } else {
            commandSender.sendMessage(ChatColor.DARK_RED + "This is not a valid code. Please note those codes are case-sensitive. It could also have been expired (codes expire after 5 minutes).");
            return false;
        }
    }
}
