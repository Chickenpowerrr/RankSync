package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.Player;
import com.gmail.chickenpowerrr.ranksync.api.Rank;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

public class BotPlayerRanksUpdateEvent implements PlayerEvent, BotEvent, CancelableEvent {

    @Getter private final Bot bot;
    @Getter private final Player player;
    @Getter private final Collection<Rank> toRemove;
    @Getter private final Collection<Rank> toAdd;
    @Setter private boolean cancelled = false;

    public BotPlayerRanksUpdateEvent(Player player, Bot bot, Collection<Rank> toRemove, Collection<Rank> toAdd) {
        this.player = player;
        this.bot = bot;
        this.toRemove = toRemove;
        this.toAdd = toAdd;
    }

    public boolean cancelled() {
        return this.cancelled;
    }
}
