package com.gmail.chickenpowerrr.ranksync.spigot.link;

import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class RequestLimitCheckMiddleware extends AbstractMiddleware {

    private final RankSyncPlugin rankSyncPlugin = JavaPlugin.getPlugin(RankSyncPlugin.class);
    private final Map<UUID, Long> timeOuts = new HashMap<>();

    RequestLimitCheckMiddleware(LinkHelper linkHelper) {
        super(linkHelper);
        Bukkit.getScheduler().runTaskTimer(this.rankSyncPlugin, this.timeOuts::clear, 20 * 30, 20 * 30);
    }

    @Override
    protected boolean check(CommandSender commandSender, UUID uuid, String service, String key) {
        if(this.timeOuts.containsKey(uuid) && this.timeOuts.get(uuid) + 1000 * 2 >= System.currentTimeMillis()) {
            commandSender.sendMessage(ChatColor.DARK_RED + "Slow down brother, you're only allowed to do this every " + ChatColor.RED + 2 + ChatColor.DARK_RED + " seconds");
            return false;
        } else {
            this.timeOuts.put(uuid, System.currentTimeMillis());
            return true;
        }
    }
}
