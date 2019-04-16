package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.RankSyncApi;
import com.gmail.chickenpowerrr.ranksync.api.Rank;
import com.gmail.chickenpowerrr.ranksync.api.event.BotPlayerRanksUpdateEvent;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

class Player implements com.gmail.chickenpowerrr.ranksync.api.Player {

    @Getter @Setter private UUID uuid;
    private final Member member;
    private final RankFactory rankFactory;
    @Getter private final Bot bot;

    Player(UUID uuid, Member member, Bot<Member, ?> bot) {
        this.uuid = uuid;
        this.member = member;
        this.rankFactory = RankFactory.getInstance(member.getGuild());
        this.bot = bot;
    }

    @Override
    public String getFancyName() {
        return this.member.getEffectiveName();
    }

    @Override
    public Collection<Rank> getRanks() {
        return this.member.getGuild().getRoles().stream().map(this.rankFactory::getRankFromRole).collect(Collectors.toSet());
    }

    @Override
    public String getPersonalId() {
        return this.member.getUser().getId();
    }

    @Override
    public void sendPrivateMessage(String message) {
        this.member.getUser().openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(message).queue()));
    }

    @Override
    public void updateRanks() {
        if(this.uuid != null) {
            this.bot.getEffectiveDatabase().getRanks(this.uuid).thenAccept(ranks -> {
                if(ranks != null) {
                    setRanks(ranks);
                }
            });
        } else {
            setRanks(new HashSet<>());
        }
    }

    @Override
    public void setRanks(Collection<Rank> ranks) {
        Collection<Role> roles = this.member.getRoles();

        Collection<Role> toRemove = new HashSet<>(roles);
        toRemove.removeIf(role -> ranks.contains(this.rankFactory.getRankFromRole(role)));

        Collection<Role> toAdd = this.rankFactory.getRolesFromRanks(ranks);
        toAdd.removeIf(roles::contains);

        if(!RankSyncApi.getApi().execute(new BotPlayerRanksUpdateEvent(this, this.rankFactory.getBot(), toRemove.stream().map(this.rankFactory::getRankFromRole).collect(Collectors.toSet()), toAdd.stream().map(this.rankFactory::getRankFromRole).collect(Collectors.toSet()))).cancelled()) {
            this.member.getGuild().getController().removeRolesFromMember(this.member, toRemove).submit();
            this.member.getGuild().getController().addRolesToMember(this.member, toAdd).submit();
        }
    }
}
