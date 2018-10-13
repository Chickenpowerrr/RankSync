package com.gmail.chickenpowerrr.ranksync.discord;

import com.gmail.chickenpowerrr.ranksync.api.*;
import com.gmail.chickenpowerrr.ranksync.api.CommandFactory;
import com.gmail.chickenpowerrr.ranksync.api.DatabaseFactory;
import com.gmail.chickenpowerrr.ranksync.api.PlayerFactory;
import com.gmail.chickenpowerrr.ranksync.api.RankFactory;
import com.gmail.chickenpowerrr.ranksync.api.event.BotEnabledEvent;
import com.gmail.chickenpowerrr.ranksync.api.event.BotForceShutdownEvent;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import javax.security.auth.login.LoginException;
import java.util.HashSet;

class DiscordBot implements Bot<Member, Role> {

    @Getter private Guild guild;
    @Getter private final String name;

    @Getter @Setter private Database effectiveDatabase;
    @Getter private PlayerFactory<Member> playerFactory;
    @Getter private RankFactory<Role> rankFactory;
    @Getter private DatabaseFactory databaseFactory;
    @Getter private CommandFactory commandFactory;

    private final Properties properties;

    DiscordBot(Properties properties) {
        this.properties = properties;
        this.name = properties.getString("name");

        try {
            new JDABuilder(properties.getString("token")).addEventListener(new DiscordEventListeners(this)).build();
        } catch(LoginException e) {
            throw new RuntimeException(e);
        }
    }

    void enable(JDA jda) {
        this.guild = jda.getGuildById(properties.getLong("guild_id"));
        if(this.guild != null) {
            this.rankFactory = com.gmail.chickenpowerrr.ranksync.discord.RankFactory.getInstance(this, guild);
            this.playerFactory = com.gmail.chickenpowerrr.ranksync.discord.PlayerFactory.getInstance(this, guild);
            this.databaseFactory = com.gmail.chickenpowerrr.ranksync.discord.DatabaseFactory.getInstance(this, guild);
            this.commandFactory = com.gmail.chickenpowerrr.ranksync.discord.CommandFactory.getInstance(this, guild);
            this.effectiveDatabase = this.databaseFactory.getDatabase("type", properties);
            this.commandFactory.addCommand(new LinkCommand("link", new HashSet<>()));
            RankSyncApi.getApi().execute(new BotEnabledEvent(this));
        } else {
            RankSyncApi.getApi().execute(new BotForceShutdownEvent(this, "The given guild id is invalid"));
            jda.shutdownNow();
        }
    }
}
