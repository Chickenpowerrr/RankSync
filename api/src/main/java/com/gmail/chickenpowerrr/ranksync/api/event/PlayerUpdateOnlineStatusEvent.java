package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Player;
import com.gmail.chickenpowerrr.ranksync.api.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PlayerUpdateOnlineStatusEvent implements PlayerEvent {

    @Getter private final Player player;
    @Getter private final Status from;
    @Getter private final Status to;
}
