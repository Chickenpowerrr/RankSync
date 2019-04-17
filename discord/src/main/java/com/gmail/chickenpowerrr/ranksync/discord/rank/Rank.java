package com.gmail.chickenpowerrr.ranksync.discord.rank;

import lombok.Getter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

public class Rank implements com.gmail.chickenpowerrr.ranksync.api.rank.Rank {

    @Getter private final Role role;

    Rank(Guild guild, String name) {
        this.role = guild.getRolesByName(name, true).get(0);
    }

    Rank(Role role) {
        this.role = role;
    }

    @Override
    public String getName() {
        return this.role.getName();
    }

}
