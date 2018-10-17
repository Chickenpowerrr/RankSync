package com.gmail.chickenpowerrr.ranksync.spigot.link;

import com.gmail.chickenpowerrr.ranksync.api.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.spigot.language.Translation;
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
            commandSender.sendMessage(Translation.RANKSYNC_COMMAND_USAGE.getTranslation("services", this.services));
            //commandSender.sendMessage(ChatColor.DARK_RED + "Please use the command as like this: " + ChatColor.RED + "/ranksync " + this.services + " <code>");
            return false;
        }
        return true;
    }
}
