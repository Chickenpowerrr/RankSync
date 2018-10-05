package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PlayerLinkCodeCreateEvent implements PlayerEvent {

    @Getter private final Player player;
    @Getter private final String code;
}
