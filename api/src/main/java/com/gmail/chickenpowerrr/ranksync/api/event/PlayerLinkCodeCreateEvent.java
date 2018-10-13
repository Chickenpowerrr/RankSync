package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event will get called when a player created
 * a code to link its account
 *
 * @author Chickenpowerrr
 * @since  1.0.0
 */
@AllArgsConstructor
public class PlayerLinkCodeCreateEvent implements PlayerEvent {

    @Getter private final Player player;
    @Getter private final String code;
}
