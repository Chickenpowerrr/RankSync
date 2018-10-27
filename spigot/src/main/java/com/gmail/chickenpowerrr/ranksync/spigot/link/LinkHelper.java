package com.gmail.chickenpowerrr.ranksync.spigot.link;

import com.gmail.chickenpowerrr.ranksync.api.BasicLinkInfo;
import com.gmail.chickenpowerrr.ranksync.api.LinkInfo;
import com.gmail.chickenpowerrr.ranksync.api.Player;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import com.gmail.chickenpowerrr.ranksync.spigot.language.Translation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class LinkHelper {

    private final RankSyncPlugin rankSyncPlugin = JavaPlugin.getPlugin(RankSyncPlugin.class);
    private final AbstractMiddleware linkMiddleware = new RequestLimitCheckMiddleware(this);
    private final AbstractMiddleware unlinkMiddleware = new RequestLimitCheckMiddleware(this);
    private final Map<String, Map.Entry<Long, Player>> authenticationKeys = new HashMap<>();
    private final Map<LinkInfo, Map<UUID, Player>> linkInfos = new HashMap<LinkInfo, Map<UUID, Player>>() {{
        put(new BasicLinkInfo("Discord", Translation.DISCORD_LINKINFO.getTranslation()), new HashMap<>());
    }};

    public LinkHelper() {
        this.linkMiddleware.setNext(new ValidServiceCheckMiddleware(this))
                .setNext(new ValidIdCheckMiddleware(this));
        this.unlinkMiddleware.setNext(new ValidServiceCheckMiddleware(this));

        startAuthCleanup();
    }

    public void addAuthenticationKey(Player player, String key) {
        this.authenticationKeys.put(key, new HashMap.SimpleEntry<>(System.currentTimeMillis(), player));
    }

    public boolean isAllowedToLink(CommandSender commandSender, UUID uuid, String service, String key) {
        return this.linkMiddleware.allowed(commandSender, uuid, service, key);
    }

    public boolean isAllowedToUnlink(CommandSender commandSender, UUID uuid, String service) {
        return this.unlinkMiddleware.allowed(commandSender, uuid, service, null);
    }

    boolean isValidAuthenticationKey(String string) {
        return this.authenticationKeys.containsKey(string);
    }

    Player getLink(String service, UUID uuid) {
        return this.linkInfos.get(getLinkInfo(service)).get(uuid);
    }

    private void startAuthCleanup() {
        Bukkit.getScheduler().runTaskTimer(this.rankSyncPlugin, () -> {
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
        this.rankSyncPlugin.getBot(service).getEffectiveDatabase().setUuid(authInfo.getValue().getPersonalId(), uuid);
        this.authenticationKeys.remove(key);
    }

    public void updateRanks(UUID uuid) {
        this.rankSyncPlugin.getBot("discord").getPlayerFactory().getPlayer(uuid).thenAccept(player -> {
            if(player != null) {
                player.updateRanks();
            }
        });
    }
}
