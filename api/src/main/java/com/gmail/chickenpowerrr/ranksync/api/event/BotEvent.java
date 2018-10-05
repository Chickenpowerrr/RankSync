package com.gmail.chickenpowerrr.ranksync.api.event;

import com.gmail.chickenpowerrr.ranksync.api.Bot;

public interface BotEvent extends Event {

    Bot getBot();
}
