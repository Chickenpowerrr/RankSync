package com.gmail.chickenpowerrr.ranksync.spigot.link;

import com.gmail.chickenpowerrr.ranksync.api.BasicLinkInfo;
import com.gmail.chickenpowerrr.ranksync.api.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.api.Player;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class LinkHelper {

    private final AbstractMiddleware middleware = new RequestLimitCheckMiddleware(this);
    private final Map<String, Map.Entry<Long, Player>> authenticationKeys = new HashMap<>();
    private final Map<LinkInfo, Map<UUID, Player>> linkInfos = new HashMap<LinkInfo, Map<UUID, Player>>() {{
        put(new BasicLinkInfo("Discord", "type !link in the Bot channel on Discord"), new HashMap<>());
    }};

    public LinkHelper() {
        this.middleware.setNext(new ValidServiceCheckMiddleware(this))
                .setNext(new NotLinkedCheckMiddleware(this))
                .setNext(new ValidIdCheckMiddleware(this));

        startAuthCleanup();
    }

    public void addAuthenticationKey(Player player, String key) {
        this.authenticationKeys.put(key, new HashMap.SimpleEntry<>(System.currentTimeMillis(), player));
    }

    public boolean isAllowedToLink(CommandSender commandSender, UUID uuid, String service, String key) {
        return this.middleware.allowed(commandSender, uuid, service, key);
    }

    boolean isValidAuthenticationKey(String string) {
        return this.authenticationKeys.containsKey(string);
    }

    Player getLink(String service, UUID uuid) {
        return this.linkInfos.get(getLinkInfo(service)).get(uuid);
    }

    private void startAuthCleanup() {
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(RankSyncPlugin.class), () -> {
            this.authenticationKeys.entrySet().stream().filter(entry -> entry.getValue().getKey() + 1000 * 60 * 5 < System.currentTimeMillis()).map(Map.Entry::getKey).collect(Collectors.toSet()).forEach(this.authenticationKeys::remove);
        }, 20 * 30, 20 * 30);
    }

    public Collection<LinkInfo> getLinkInfos() {
        return Collections.unmodifiableCollection(this.linkInfos.keySet());
    }

    public LinkInfo getLinkInfo(String name) {
        return this.linkInfos.keySet().stream().filter(linkInfo -> linkInfo.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void link(UUID uuid, String service, String key) {
        Map.Entry<Long, Player> authInfo = this.authenticationKeys.get(key);
        this.linkInfos.get(getLinkInfo(service)).put(uuid, authInfo.getValue());
        JavaPlugin.getPlugin(RankSyncPlugin.class).getBot(service).getEffectiveDatabase().setUuid(authInfo.getValue().getPersonalId(), uuid);
        this.authenticationKeys.remove(key);
    }

    public void updateRanks(UUID uuid) {
        this.linkInfos.values().stream().filter(entry -> entry.containsKey(uuid)).map(map -> map.get(uuid)).forEach(Player::updateRanks);
    }
}
