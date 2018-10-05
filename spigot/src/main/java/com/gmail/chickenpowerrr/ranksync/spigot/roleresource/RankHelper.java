package com.gmail.chickenpowerrr.ranksync.spigot.roleresource;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.Rank;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class RankHelper {

    private final Map<String, Map<Bot, String>> ranks;

    @SuppressWarnings("unchecked")
    Rank getRank(Bot bot, String minecraftGroupName) {
        if(this.ranks.containsKey(minecraftGroupName) && this.ranks.get(minecraftGroupName).containsKey(bot)) {
            return bot.getRankFactory().getRankFromRole(bot.getRankFactory().getRoleFromName(this.ranks.get(minecraftGroupName).get(bot)));
        } else {
            return null;
        }
    }
}
