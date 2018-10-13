package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Bot;
import com.gmail.chickenpowerrr.ranksync.api.Player;
import com.gmail.chickenpowerrr.ranksync.api.Rank;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * This event will get called when a bot updates
 * someones ranks
 *
 * @author Chickenpowerrr
 * @since  1.0.0
 */
public class BotPlayerRanksUpdateEvent implements PlayerEvent, BotEvent, CancelableEvent {

    @Getter private final Bot bot;
    @Getter private final Player player;
    @Getter private final Collection<Rank> toRemove;
    @Getter private final Collection<Rank> toAdd;
    @Setter private boolean cancelled = false;

    /**
     * Creates a new instance based on the action the bot wants to perform
     *
     * @param player   the player will get updated if the event isn't cancelled
     * @param bot      the bot that wants to change the ranks
     * @param toRemove the ranks that will get removed
     * @param toAdd    the ranks that will get added
     */
    public BotPlayerRanksUpdateEvent(Player player, Bot bot, Collection<Rank> toRemove, Collection<Rank> toAdd) {
        this.player = player;
        this.bot = bot;
        this.toRemove = toRemove;
        this.toAdd = toAdd;
    }

    /**
     * Get if the event got cancelled or not
     *
     * @return true if the event got cancelled, false if it didn't
     */
    @Override
    public boolean cancelled() {
        return this.cancelled;
    }
}
