package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Player;

public interface PlayerEvent extends Event {

    Player getPlayer();
}
