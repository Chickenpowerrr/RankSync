package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Player;
import com.gmail.chickenpowerrr.ranksync.api.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event will get called when a player changed
 * its status
 *
 * @author Chickenpowerrr
 * @since  1.0.0
 */
@AllArgsConstructor
public class PlayerUpdateOnlineStatusEvent implements PlayerEvent {

    @Getter private final Player player;
    @Getter private final Status from;
    @Getter private final Status to;
}
