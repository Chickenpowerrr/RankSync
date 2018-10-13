package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.Rank;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class RankFactory implements com.gmail.chickenpowerrr.ranksync.api.RankFactory<Role> {

    private static final Map<Guild, RankFactory> instances = new HashMap<>();

    private final Map<Role, Rank> ranks = new HashMap<>();
    private final Map<String, Role> roles = new HashMap<>();
    private final Guild guild;
    @Getter private final Bot<?, Role> bot;

    private RankFactory(Bot<?, Role> bot, Guild guild) {
        this.bot = bot;
        this.guild = guild;
    }

    static RankFactory getInstance(Bot<?, Role> bot, Guild guild) {
        if(!instances.containsKey(guild)) {
            if(guild != null) {
                instances.put(guild, new RankFactory(bot, guild));
            }
        }
        return instances.get(guild);
    }

    static RankFactory getInstance(Guild guild) {
        return getInstance(null, guild);
    }

    @Override
    public Rank getRankFromRole(Role role) {
        if(role != null) {
            if(!this.ranks.containsKey(role)) {
                this.ranks.put(role, new com.gmail.chickenpowerrr.ranksync.discord.Rank(role));
            }
            return this.ranks.get(role);
        } else {
            return null;
        }
    }

    @Override
    public Collection<Rank> getRanksFromRoles(Collection<Role> roles) {
        return roles.stream().map(this::getRankFromRole).collect(Collectors.toSet());
    }

    @Override
    public Role getRoleFromName(String string) {
        if(string != null) {
            if(!this.roles.containsKey(string)) {
                List<Role> roles = this.guild.getRolesByName(string, true);
                if(!roles.isEmpty()) {
                    this.roles.put(string, roles.get(0));
                } else {
                    return null;
                }
            }
            return this.roles.get(string);
        } else {
            return null;
        }
    }

    @Override
    public Collection<Role> getRolesFromNames(Collection<String> strings) {
        return strings.stream().map(this::getRoleFromName).collect(Collectors.toSet());
    }

    @Override
    public Role getRoleFromRank(Rank rank) {
        if(rank instanceof com.gmail.chickenpowerrr.ranksync.discord.Rank) {
            return ((com.gmail.chickenpowerrr.ranksync.discord.Rank) rank).getRole();
        } else {
            return getRoleFromName(rank.getName());
        }
    }

    @Override
    public Collection<Role> getRolesFromRanks(Collection<Rank> ranks) {
        return ranks.stream().map(this::getRoleFromRank).collect(Collectors.toSet());
    }
}
