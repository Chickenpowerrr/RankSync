package com.gmail.chickenpowerrr.ranksync.discord.player;

import com.gmail.chickenpowerrr.ranksync.api.bot.Bot;
import com.gmail.chickenpowerrr.ranksync.api.player.Player;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerFactory implements com.gmail.chickenpowerrr.ranksync.api.player.PlayerFactory<Member> {

    private static final Map<Guild, PlayerFactory> instances = new HashMap<>();

    private final Map<UUID, Player> players = new HashMap<>();
    private final Guild guild;
    @Getter private final Bot<Member, ?> bot;

    private PlayerFactory(Bot<Member, ?> bot, Guild guild) {
        this.bot = bot;
        this.guild = guild;
    }

    public static PlayerFactory getInstance(Bot<Member, ?> bot, Guild guild) {
        if(!instances.containsKey(guild)) {
            instances.put(guild, new PlayerFactory(bot, guild));
        }
        return instances.get(guild);
    }

    static PlayerFactory getInstance(Guild guild) {
        return getInstance(null, guild);
    }

    @Override
    public CompletableFuture<Void> setUuid(String playerId, UUID uuid) {
        return getPlayer(this.guild.getMemberById(playerId)).thenAccept(player -> {
            if(uuid == null) {
                this.players.remove(player.getUuid());
                player.setUuid(null);
            } else {
                this.players.remove(player.getUuid());
                this.players.put(uuid, player);
                player.setUuid(uuid);
            }
        });
    }

    @Override
    public CompletableFuture<Player> getPlayer(Member member) {
        CompletableFuture<Player> completableFuture = new CompletableFuture<>();
        this.bot.getEffectiveDatabase().getUuid(member.getUser().getId()).thenAccept(uuid -> {
            Player player;
            if(this.players.containsKey(uuid)) {
                player = this.players.get(uuid);
            } else {
                player = new com.gmail.chickenpowerrr.ranksync.discord.player.Player(uuid, member, this.bot);
                if(uuid != null) {
                    this.players.put(uuid, player);
                }
            }
            completableFuture.complete(player);
        });

        completableFuture.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture<Player> getPlayer(UUID uuid) {
        CompletableFuture<Player> completableFuture = new CompletableFuture<>();

        this.bot.getEffectiveDatabase().getPlayerId(uuid).thenAccept(identifier -> {
            if(identifier != null) {
                completableFuture.complete(new com.gmail.chickenpowerrr.ranksync.discord.player.Player(uuid, this.guild.getMemberById(identifier), this.bot));
            } else {
                completableFuture.complete(null);
            }
        });

        completableFuture.exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

        return completableFuture;
    }
}
