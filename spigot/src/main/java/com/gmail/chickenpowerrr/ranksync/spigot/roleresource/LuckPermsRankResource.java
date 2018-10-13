package com.gmail.chickenpowerrr.ranksync.spigot.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.Rank;
import com.gmail.chickenpowerrr.ranksync.api.RankResource;
import com.gmail.chickenpowerrr.ranksync.spigot.RankSyncPlugin;
import lombok.Setter;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LuckPermsRankResource implements RankResource {

    private final LuckPermsApi api;
    @Setter private Bot bot;
    private RankHelper rankHelper = null;

    public LuckPermsRankResource(LuckPermsApi luckPermsApi, Bot bot) {
        this.api = luckPermsApi;
        this.bot = bot;
    }

    public LuckPermsRankResource(LuckPermsApi luckPermsApi) {
        this(luckPermsApi, null);
    }

    @Override
    public CompletableFuture<Collection<Rank>> getRanks(UUID uuid) {
        CompletableFuture<Collection<Rank>> completableFuture = new CompletableFuture<>();

        if(this.rankHelper == null) {
            this.rankHelper = JavaPlugin.getPlugin(RankSyncPlugin.class).getRankHelper();
        }

        api.getUserManager().loadUser(uuid).thenAcceptAsync(user ->
            completableFuture.complete(user.getOwnNodes().stream().filter(Node::isGroupNode).map(Node::getGroupName).map(groupName -> this.rankHelper.getRank(this.bot, groupName)).filter(Objects::nonNull).collect(Collectors.toSet())));

        completableFuture.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

        return completableFuture;
    }

    @Override
    public boolean isValidRank(String name) {
        return this.api.getGroup(name) != null;
    }
}
